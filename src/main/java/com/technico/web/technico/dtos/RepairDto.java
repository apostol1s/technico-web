package com.technico.web.technico.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RepairDto {

    private Long id;
    private String vat;
    private String e9;
    private RepairType repairType;
    private String shortDescription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date submissionDate;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date scheduledStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date scheduledEndDate;
    private BigDecimal proposedCost;
    private Boolean acceptanceStatus;
    private RepairStatus repairStatus;
    private String repairAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date actualStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date actualEndDate;
    private Boolean isDeleted;
}
