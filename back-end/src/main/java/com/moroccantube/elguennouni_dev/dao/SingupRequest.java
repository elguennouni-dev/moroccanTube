package com.moroccantube.elguennouni_dev.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingupRequest {
    private String username;
    private String email;
    private String fullName;
    private String password;
}
