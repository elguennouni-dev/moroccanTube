package com.moroccantube.app.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFullnameRequest {
    private String username;
    private String newFullname;
}