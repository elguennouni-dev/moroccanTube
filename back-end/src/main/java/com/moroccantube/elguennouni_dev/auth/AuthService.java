package com.moroccantube.elguennouni_dev.auth;

import com.moroccantube.elguennouni_dev.dao.SingupRequest;
import com.moroccantube.elguennouni_dev.mapper.UserMapper;
import com.moroccantube.elguennouni_dev.model.User;
import com.moroccantube.elguennouni_dev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;


    public ResponseEntity<?> signup(SingupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() || userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("message","Username or Email already used"));
        }

        User userToSave = new User();
        userToSave.setFullName(request.getFullName());
        userToSave.setUsername(request.getUsername());
        userToSave.setEmail(request.getEmail());
        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        User createdUser = userRepository.save(userToSave);
        return ResponseEntity.ok(userMapper.toDto(createdUser));
    }

}
