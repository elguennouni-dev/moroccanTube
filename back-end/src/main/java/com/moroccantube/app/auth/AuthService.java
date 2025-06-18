package com.moroccantube.app.auth;

import com.moroccantube.app.dao.AuthDao;
import com.moroccantube.app.dao.SignupDao;
import com.moroccantube.app.dto.AuthResponseDto;
import com.moroccantube.app.dto.UserDto;
import com.moroccantube.app.exception.custom.DuplicateResourceException;
import com.moroccantube.app.exception.custom.InvalidInputException;
import com.moroccantube.app.mapper.UserMapper;
import com.moroccantube.app.model.User;
import com.moroccantube.app.repository.UserRepository;
import com.moroccantube.app.security.JwtUtil;
import com.moroccantube.app.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;


    public AuthResponseDto login(AuthDao authDao) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDao.getUsername(), authDao.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String accessToken = jwtUtil.generateToken(userDetails);
            LocalDateTime accessTokenExpiryDate = jwtUtil.extractExpiration(accessToken);

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String refreshToken = refreshTokenService.createRefreshToken(user);

            return new AuthResponseDto(
                    accessToken,
                    refreshToken,
                    userDetails.getUsername(),
                    accessTokenExpiryDate
            );

        } catch (BadCredentialsException ex) {
            throw new InvalidInputException("Invalid username or password.");
        }
    }



    public UserDto signup(SignupDao request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered.");
        }

        User userToSave = new User();
        userToSave.setFullName(request.getFullName());
        userToSave.setUsername(request.getUsername());
        userToSave.setEmail(request.getEmail());
        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        userToSave.setEnabled(true);

        User createdUser = userRepository.save(userToSave);
        return userMapper.toDto(createdUser);
    }


}
