package com.moroccantube.app.service;

import com.moroccantube.app.dao.UpdatePasswordDao;
import com.moroccantube.app.exception.user.InvalidFullNameException;
import com.moroccantube.app.exception.user.InvalidPasswordException;
import com.moroccantube.app.model.User;
import com.moroccantube.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final int MIN_FULLNAME_LENGTH = 3;
    private static final int MAX_FULLNAME_LENGTH = 40;

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<?> handleUpdateFullname(String newFullname, String username) {
        validateFullName(newFullname);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this username: " + username));

        user.setFullName(newFullname);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Full name updated successfully!"));
    }



    public ResponseEntity<?> handleUpdatePassword(UpdatePasswordDao updatePasswordDao) {
        validatePassword(updatePasswordDao.getNewPassword());

        User user = userRepository.findByUsername(updatePasswordDao.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user found with this username: " + updatePasswordDao.getUsername()));

        if (!passwordEncoder.matches(updatePasswordDao.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDao.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password updated successfully!"));
    }





    // Helpers
    private void validateFullName(String fullname) {
        if (fullname == null || fullname.trim().isEmpty()) {
            throw new InvalidFullNameException("Full name cannot be null or empty");
        }

        String trimmed = fullname.trim();
        if (trimmed.length() < MIN_FULLNAME_LENGTH || trimmed.length() > MAX_FULLNAME_LENGTH) {
            throw new InvalidFullNameException(
                    String.format("Full name must be between %d and %d characters", MIN_FULLNAME_LENGTH, MAX_FULLNAME_LENGTH)
            );
        }
    }

    private void validatePassword(String password) {
        if (password == null) {
            throw new InvalidPasswordException("Password cannot be null");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(
                    String.format("Password must be at least %d characters long", MIN_PASSWORD_LENGTH)
            );
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(
                    String.format("Password cannot exceed %d characters", MAX_PASSWORD_LENGTH)
            );
        }

        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        if (!hasUppercase) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        }

        if (!hasLowercase) {
            throw new InvalidPasswordException("Password must contain at least one lowercase letter");
        }

        if (!hasDigit) {
            throw new InvalidPasswordException("Password must contain at least one digit");
        }

        if (!hasSpecialChar) {
            throw new InvalidPasswordException("Password must contain at least one special character (!@#$%^&*()_+-=[]{}|;:,.<>?)");
        }

        if (password.contains(" ")) {
            throw new InvalidPasswordException("Password cannot contain spaces");
        }

        String lowerPassword = password.toLowerCase();
        if (lowerPassword.contains("password") || lowerPassword.contains("123456") ||
                lowerPassword.contains("qwerty") || lowerPassword.contains("admin")) {
            throw new InvalidPasswordException("Password contains common weak patterns");
        }
    }
}
