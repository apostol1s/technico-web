package com.technico.web.technico.dtos;

import com.technico.web.technico.models.RepairStatus;
import com.technico.web.technico.models.RepairType;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRepairsDto {

    private Long id;
    private String vat;
    private String e9;
    private RepairType repairType;
    private String shortDescription;
    private Date submissionDate;
    private String description;
    private Date scheduledStartDate;
    private Date scheduledEndDate;
    private BigDecimal proposedCost;
    private Boolean acceptanceStatus;
    private RepairStatus repairStatus;
    private String repairAddress;
    private Date actualStartDate;
    private Date actualEndDate;
    private Boolean deleted;
}
