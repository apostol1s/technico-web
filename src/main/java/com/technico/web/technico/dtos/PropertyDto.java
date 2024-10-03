package com.technico.web.technico.dtos;

import com.technico.web.technico.models.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {

    private Long id;
    private String e9;
    private String address;
    private int year;
    private PropertyType propertyType;
    private String vat;
    private boolean isDeleted;
}
