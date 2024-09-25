package com.technico.web.technico.services;

import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.Repair;
import com.technico.web.technico.models.RepairStatus;
import com.technico.web.technico.models.RepairType;
import com.technico.web.technico.repositories.RepairRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class RepairServiceImpl implements RepairService {

    private RepairRepository repairRepository;
    private PropertyService propertyServiceInterface;

    @Inject
    public RepairServiceImpl(RepairRepository repairRepository, PropertyServiceImpl propertyService) {
        this.repairRepository = repairRepository;
        this.propertyServiceInterface = propertyService;
    }

    public RepairServiceImpl() {
    }

    /**
     * Creates a new repair for a specified property
     *
     * @param repairType
     * @param description
     * @param scheduledStartDate
     * @param scheduledEndDate
     * @param proposedCost
     * @param id
     * @return The created Repair object with the specified details.
     * @throws CustomException If the provided property is deleted, or if any of
     * the validation fails for the repair type or description.
     */
    @Override
    public Repair createRepair(RepairType repairType, String description,
            Date scheduledStartDate, Date scheduledEndDate, BigDecimal proposedCost, Long id) throws CustomException {
        Property property = propertyServiceInterface.findByID(id);

        if (property == null) {
            throw new CustomException("Property id not inserted.");
        }
        validateRepairType(repairType);
        validateDesc(description);
        Repair repair = new Repair();
        repair.setScheduledStartDate(scheduledStartDate);
        repair.setScheduledEndDate(scheduledEndDate);
        repair.setRepairType(repairType);
        repair.setDescription(description);
        repair.setRepairAddress(property.getPropertyAddress());
        repair.setRepairStatus(RepairStatus.PENDING);
        repair.setProperty(property);
        repair.setProposedCost(proposedCost);
        repairRepository.save(repair);
        return repair;
    }

    /**
     * Updates the type of the repair identified by the given ID.
     *
     * @param id
     * @param repairType
     * @param scheduledEndDate
     * @param scheduledStartDate
     * @param description
     * @param repairAddress
     * @param repairStatus
     * @param proposedCost
     * @return The updated Repair object with the specified details.
     * @throws CustomException If the provided repair is deleted, or if any of
     * the validation fails for the repair type or description.
     */
    @Override
    @Transactional
    public Repair updateRepairAdmin(Long id, RepairType repairType, Date scheduledStartDate, Date scheduledEndDate,
            String description, String repairAddress, RepairStatus repairStatus, BigDecimal proposedCost) throws CustomException {
        Repair repair = repairRepository.findById(id).get();
        if (repair.isDeleted()) {
            throw new CustomException("Cannot update a deleted property.");
        }

        repair.setScheduledStartDate(scheduledStartDate);
        repair.setScheduledEndDate(scheduledEndDate);
        validateRepairType(repairType);
        repair.setRepairType(repairType);
        validateDesc(description);
        repair.setDescription(description);
        repair.setRepairAddress(repairAddress);
        repair.setRepairStatus(repairStatus);
        repair.setProposedCost(proposedCost);

        return repairRepository.save(repair).get();
    }

    /**
     * Updates the type of the repair identified by the given ID. The main scope
     * of this method is to show that the owner has limited permissions compared
     * to the admin.
     *
     * @param id
     * @param repairType
     * @param description
     * @param repairAddress
     * @return The updated Repair object with the specified details.
     * @throws CustomException If the provided repair is deleted, or if any of
     * the validation fails for the repair type or description.
     */
    @Override
    @Transactional
    public Repair updateRepairOwner(Long id, RepairType repairType, String description, String repairAddress) throws CustomException {
        Repair repair = repairRepository.findById(id).get();
        if (repair.isDeleted()) {
            throw new CustomException("Cannot update a deleted repair.");
        }

        validateRepairType(repairType);
        repair.setRepairType(repairType);
        validateDesc(description);
        repair.setDescription(description);
        repair.setRepairAddress(repairAddress);

        return repairRepository.save(repair).get();
    }

    /**
     * Updates the short description of the repair identified by the given ID.
     *
     * @param id
     * @param shortDescription
     */
    public void updshortDesc(Long id, String shortDescription) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (shortDescription != null) {
            repairFound.setShortDescription(shortDescription);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the detailed description of the repair identified by the given
     * ID.
     *
     * @param id
     * @param description
     */
    @Override
    public void updDesc(Long id, String description) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (description != null) {
            repairFound.setDescription(description);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the proposed cost, start date, and end date of the repair
     * identified by the given ID.
     *
     * @param id
     * @param proposedCost
     * @param proposedStartDate
     * @param proposedEndDateTime
     */
    @Override
    public void updCostDates(Long id, BigDecimal proposedCost, Date proposedStartDate, Date proposedEndDateTime) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (proposedCost != null) {
            repairFound.setProposedCost(proposedCost);
        }
        repairFound.setScheduledStartDate(proposedStartDate);
        repairFound.setScheduledEndDate(proposedEndDateTime);
        repairRepository.save(repairFound);
    }

    /**
     * Updates the acceptance status of the repair identified by the given ID.
     *
     * @param id
     * @param response
     */
    @Override
    public void updAcceptance(Long id, int response) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        if (response == 1) {
            repairFound.setAcceptanceStatus(Boolean.TRUE);
            //repairFound.setRepairStatus(RepairStatus.INPROGRESS);
            //repairFound.setActualStartDate(LocalDateTime.now());
        } else {
            repairFound.setAcceptanceStatus(Boolean.FALSE);
            repairFound.setRepairStatus(RepairStatus.DECLINED);
        }
        repairRepository.save(repairFound);
    }

    /**
     * Updates the status of the repair identified by the given ID to INPROGRESS
     * and sets the actual start date to the current time.
     *
     * @param id
     */
    @Override
    public void updateStatus(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();

        repairFound.setRepairStatus(RepairStatus.INPROGRESS);
        repairFound.setActualStartDate(Date.from(Instant.now()));

        repairRepository.save(repairFound);
    }

    /**
     * Marks the repair identified by the given ID as complete by setting its
     * status to COMPLETE and its actual end date to the current time.
     *
     * @param id
     */
    @Override
    public void updComplete(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        Repair repairFound = repair.get();
        repairFound.setActualEndDate(Date.from(Instant.now()));
        repairFound.setRepairStatus(RepairStatus.COMPLETE);
        repairRepository.save(repairFound);
    }

    /**
     * Saves the given Repair to the repository.
     *
     * @param repair
     * @return
     * @throws CustomException
     */
    @Override
    public Long saveRepair(Repair repair) throws CustomException {
        repairRepository.save(repair);
        return repair.getId();
    }

    /**
     * Retrieves all repairs from the repository.
     *
     * @return A list of all Repair instances.
     * @throws CustomException
     */
    @Override
    public List<Repair> getRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findAll();
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs with a status of PENDING from the repository.
     *
     * @return
     * @throws CustomException
     */
    @Override
    public List<Repair> getPendingRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findPendingRepairs();
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found.");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all pending repairs for a given owner.
     *
     * @param owner
     * @return A list of all pending Repair instances associated with the given
     * owner.
     * @throws CustomException
     */
    @Override
    public List<Repair> getPendingRepairsByOwner(Owner owner) throws CustomException {
        List<Repair> repairs = repairRepository.findPendingRepairsByOwner(owner).stream()
                .filter(repair -> !repair.isDeleted())
                .collect(Collectors.toList());
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs with a status of INPROGRESS from the repository.
     *
     * @return A list of all in-progress Repair instances.
     * @throws CustomException
     */
    @Override
    public List<Repair> getInProgressRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findInProgressRepairs();
        if (repairs.isEmpty()) {
            throw new CustomException("In Progress Repairs not found");
        } else {
            return repairs;
        }

    }

    /**
     * Retrieves all repairs that have been accepted.
     *
     * @return A list of all accepted Repair instances.
     * @throws CustomException
     */
    @Override
    public List<Repair> getAcceptedRepairs() throws CustomException {
        List<Repair> repairs = repairRepository.findAcceptedRepairs();
        if (repairs.isEmpty()) {
            throw new CustomException("Accepted Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs associated with a given owner.
     *
     * @param id
     * @return A list of all Repair instances associated with the given owner.
     * @throws CustomException if no repairs are found for the properties owned
     * by the given owner.
     */
    @Override
    public List<Repair> findRepairsByOwner(Long id) throws CustomException {
        List<Property> properties = propertyServiceInterface.findPropertyByOwnerID(id);
        List<Repair> repairs = properties.stream()
                .flatMap(property -> property.getRepairs().stream())
                .collect(Collectors.toList());
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Retrieves all repairs for a given owner on a specific date.
     *
     * @param date
     * @param owner
     *
     * @return A list of all {@link Repair} instances associated with the given
     * owner on the specified date. Returns an empty list if the date format is
     * invalid.
     */
//    @Override
//    public List<Repair> findRepairsByDate(Date date) {
//        LocalDate localDate;
//        try {
//            localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
//        } catch (DateTimeParseException e) {
//            System.out.println("Invalid date format: " + date);
//            return Collections.emptyList();
//        }
//
//        LocalDateTime localDateTimeStart = localDate.atStartOfDay();
//        LocalDateTime localDateTimeEnd = localDate.atTime(LocalTime.MAX);
//        return repairRepository.findRepairsByDates(localDateTimeStart, localDateTimeEnd, owner);
//    }
    /**
     * Retrieves all repairs for a given owner within a specified range of
     * dates.
     *
     * @param startDate
     * @param endDate
     * @param owner
     *
     * @return A list of all Repair instances associated with the given owner
     * within the specified date range. Returns an empty list if any of the date
     * formats are invalid
     */
//    @Override
    public List<Repair> findRepairsByRangeOfDates(String startDate, String endDate, Owner owner) {
        LocalDate startLocalDate;
        LocalDate endLocalDate;

        try {
            startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
            endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return Collections.emptyList();
        }

        LocalDateTime localDateTimeStart = startLocalDate.atStartOfDay();
        LocalDateTime localDateTimeEnd = endLocalDate.atTime(LocalTime.MAX);
        return repairRepository.findRepairsByDates(localDateTimeStart, localDateTimeEnd, owner);
    }

    /**
     * Retrieves a repair by its ID.
     *
     * @param id
     *
     * @return An Optional containing the found Repair, or an empty Optional if
     * no repair is found.
     */
    @Override
    public Optional<Repair> findRepairById(Long id) {

        return repairRepository.findById(id);
    }

    /**
     * Permanently deletes a repair by its ID.
     *
     * @param id
     *
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Override
    public boolean deletePermantlyById(Long id) {
        return repairRepository.deleteById(id);
    }

    /**
     * Safely deletes a repair by its ID, checking if it exists first.
     *
     * @param id
     *
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Override
    @Transactional
    public boolean deleteSafely(Long id) {
        Optional<Repair> repair = repairRepository.findById(id);
        if (repair.isEmpty()) {
            System.out.println("Repair no found");
            return false;
        }
        Repair repairFound = repair.get();
        return repairRepository.safeDelete(repairFound);
    }

    /**
     * Retrieves all repairs associated with a specific property.
     *
     * @param property
     *
     * @return A list of all Repair instances associated with the specified
     * property.
     */
    @Override
    public List<Repair> getRepairByPropertyId(Property property) throws CustomException {
        List<Repair> repairs = repairRepository.findRepairsByPropertyId(property).stream()
                .filter(repair -> !repair.isDeleted())
                .collect(Collectors.toList());
        if (repairs.isEmpty()) {
            throw new CustomException("Repairs not found");
        } else {
            return repairs;
        }
    }

    /**
     * Validates the description of a repair. The description must not be null,
     * blank, or exceed 400 characters.
     *
     * @param description
     * @throws CustomException
     */
    @Override
    public void validateDesc(String description) throws CustomException {
        if (description == null || description.length() > 400 || description.isBlank()) {
            throw new CustomException("Description cannot be empty nor exceed 400 characters.");
        }
    }

    /**
     * Validates the short description of a repair. The short description must
     * not be null, blank, or exceed 100 characters.
     *
     * @param shortDescription
     * @throws CustomException
     */
    @Override
    public void validateShortDesc(String shortDescription) throws CustomException {
        if (shortDescription == null || shortDescription.length() > 100 || shortDescription.isBlank()) {
            throw new CustomException("Short Description cannot be empty nor exceed 100 characters.");
        }
    }

    /**
     * Validates the repair type based on an integer value. The value must be
     * between 1 and 5.
     *
     * @param repairType
     * @throws CustomException
     */
    @Override
    public void validateType(int repairType) throws CustomException {
        if (repairType < 1 || repairType > 5) {
            throw new CustomException("Invalid input. Please enter a number between 1-5.");
        }
    }

    /**
     * Determines the RepairType based on an integer value.
     *
     * @param repairType
     *
     * @return The corresponding RepairType.
     * @throws CustomException
     */
    @Override
    public RepairType checkType(int repairType) throws CustomException {
        switch (repairType) {
            case 1:
                return RepairType.PAINTING;
            case 2:
                return RepairType.INSULATION;
            case 3:
                return RepairType.FRAMES;
            case 4:
                return RepairType.PLUMBING;
            case 5:
                return RepairType.ELECTRICALWORK;
            default:
                throw new CustomException("Invalid input.");
        }
    }

    public void validateRepairType(RepairType repairType) throws CustomException {

        if (repairType != RepairType.valueOf("PAINTING")
                && repairType != RepairType.valueOf("INSULATION")
                && repairType != RepairType.valueOf("FRAMES")
                && repairType != RepairType.valueOf("PLUMBING")
                && repairType != RepairType.valueOf("ELECTRICALWORK")) {
            throw new CustomException("Invalid input.");
        }

    }

}
