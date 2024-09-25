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
public class UpdateRepairAdminDto {
   private RepairType repairType;
   private Date scheduledStartDate;
   private Date scheduledEndDate;
   private String description;
   private String repairAddress;
   private RepairStatus repairStatus;
   private BigDecimal proposedCost; 
}
