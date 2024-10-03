package com.technico.web.technico.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDto {

    private Long id;
    private String vat;
    private String name;
    private String surname;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isDeleted;
}
