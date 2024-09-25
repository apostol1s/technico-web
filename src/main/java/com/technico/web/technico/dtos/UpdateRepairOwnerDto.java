package com.technico.web.technico.dtos;

import com.technico.web.technico.models.RepairType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRepairOwnerDto {
    private RepairType repairType;
    private String description;
    private String repairAddress;
}
