package com.technico.web.technico.services;

import com.technico.web.technico.dtos.RepairDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.Repair;
import com.technico.web.technico.models.RepairStatus;
import com.technico.web.technico.models.RepairType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RepairService {

    RepairDto createRepair(String e9, RepairType repairType, String description,
            Date scheduledStartDate, Date scheduledEndDate, BigDecimal proposedCost)throws CustomException;

    RepairDto updateRepairAdmin(Long id, RepairType repairType, Date scheduledEndDate, Date scheduledStartDate,
            String description, String repairAddress, RepairStatus repairStatus, BigDecimal proposedCost)throws CustomException;
        

    RepairDto updateRepairOwner(Long id, RepairType repairType, String description, String repairAddress)throws CustomException;

//    void updateRepairType(Long id, RepairType repairType);
//    
//    void updDesc(Long id, String description);
//    
//    void updshortDesc(Long id, String shortDescription);
//
//    void updCostDates(Long id, BigDecimal proposedCost, Date proposedStartDate, Date proposedEndDateTime);
//
//    void updAcceptance(Long id, int response) throws Exception;
//
//    void updComplete(Long id);
//
//    void updateStatus(Long id);

//    Long saveRepair(Repair repair) throws CustomException;

    List<Repair> getRepairs() throws CustomException;

//    public List<Repair> getPendingRepairs() throws CustomException;
//
//    public List<Repair> getPendingRepairsByOwner(Owner owner) throws CustomException;
//
//    public List<Repair> getInProgressRepairs() throws CustomException;

//    List<Repair> findRepairsByOwner(String vat) throws CustomException;

//    public List<Repair> getRepairByPropertyId(Property property)throws CustomException;

//    public List<Repair> getAcceptedRepairs() throws CustomException;

//    List<Repair> findRepairsByDate(String date, Owner owner);
//
//    List<Repair> findRepairsByRangeOfDates(String startDate, String endDate, Owner owner);
    
    List<Repair> findRepairsByDate(Date repairDate);
    
    public List<Repair> findRepairsByOwner(Long id) throws CustomException;

    boolean deletePermantlyById(Long id);

    boolean deleteSafely(Long id);
    
    Optional<Repair> findRepairById(Long id);

    void validateType(int repairType) throws CustomException;

    public void validateDesc(String description) throws CustomException;

    public void validateShortDesc(String shortDescription) throws CustomException;

    public RepairType checkType(int repairType) throws CustomException;

}
