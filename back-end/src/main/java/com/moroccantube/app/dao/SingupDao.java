package com.moroccantube.app.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingupDao {
    private String username;
    private String email;
    private String fullName;
    private String password;
}
