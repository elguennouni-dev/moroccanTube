package com.moroccantube.app.auth;

import com.moroccantube.app.dao.SingupDao;
import com.moroccantube.app.dao.AuthDao;
import com.moroccantube.app.dto.AuthDto;
import com.moroccantube.app.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDao authDao) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDao.getUsername(), authDao.getPassword()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthDto(token, userDetails.getUsername(), "ROLE_USER", new Date(System.currentTimeMillis() + 86400000)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SingupDao request) {
        return authService.signup(request);
    }

}