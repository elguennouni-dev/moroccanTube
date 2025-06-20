package com.moroccantube.app.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDao {

    private String username;
    private String oldPassword;
    private String newPassword;

}
