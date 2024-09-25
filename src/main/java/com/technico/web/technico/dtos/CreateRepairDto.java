package com.technico.web.technico.dtos;

import com.technico.web.technico.models.RepairType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRepairDto {
    private RepairType repairType;
    private String description;
    private Date scheduledStartDate;
    private Date scheduledEndDate;
    private BigDecimal proposedCost;
    private Long propertyId;
}
