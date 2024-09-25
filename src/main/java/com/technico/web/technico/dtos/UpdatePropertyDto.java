package com.technico.web.technico.dtos;

import com.technico.web.technico.models.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePropertyDto {
    private String propertyAddress;
    private int constructionYear;
    private PropertyType propertyType;
}
