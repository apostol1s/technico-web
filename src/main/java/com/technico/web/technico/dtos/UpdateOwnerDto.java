package com.technico.web.technico.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOwnerDto {
    
    private String address;
    String phoneNumber;
    String email;
    String password;
}
