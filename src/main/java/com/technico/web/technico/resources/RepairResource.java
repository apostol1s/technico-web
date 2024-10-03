package com.technico.web.technico.resources;

import com.technico.web.technico.dtos.RepairDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.Repair;
import com.technico.web.technico.services.RepairService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Path("repair")
public class RepairResource {

    @Inject
    private RepairService repairService;

    /**
     * Creates a new repair using the provided property data.
     *
     * @param repair A DTO containing property data.
     * @return The newly created repairDto, or null if an error occurs.
     */
    @Path("create")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public RepairDto saveRepair(RepairDto repair) {
        try {
            return repairService.createRepair(
                    repair.getE9(),
                    repair.getRepairType(),
                    repair.getDescription(),
                    repair.getScheduledStartDate(),
                    repair.getScheduledEndDate(),
                    repair.getProposedCost()
            );
        } catch (CustomException e) {
            log.debug("Error whlie saving new property " + e.getMessage());
        }
        return null;
    }

    /**
     * Admin updates the details of an existing repaur.
     *
     * @param id The ID of the repair to update.
     * @param repair A DTO containing the updated property details.
     * @return The updated repairDto, or a null if an error occurs.
     */
    @Path("updateAdmin/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public RepairDto updateRepairAdmin(@PathParam("id") Long id, RepairDto repair) {
        try {
            return repairService.updateRepairAdmin(
                    id,
                    repair.getRepairType(),
                    repair.getScheduledStartDate(),
                    repair.getScheduledEndDate(),
                    repair.getDescription(),
                    repair.getRepairAddress(),
                    repair.getRepairStatus(),
                    repair.getProposedCost()
            );
        } catch (CustomException e) {
            log.debug("Error whlie updating repair " + e.getMessage());
        }
        return null;
    }

    /**
     * Owner updates the details of an existing repair.
     *
     * @param id The ID of the repair to update.
     * @param repair A DTO containing the updated property details.
     * @return The updated repairDto, or a null if an error occurs.
     */
    @Path("updateOwner/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public RepairDto updateRepairOwner(@PathParam("id") Long id, RepairDto repair) {
        try {
            return repairService.updateRepairOwner(
                    id,
                    repair.getRepairType(),
                    repair.getDescription(),
                    repair.getRepairAddress()
            );
        } catch (CustomException e) {
            log.debug("Error whlie updating repair " + e.getMessage());
        }
        return null;
    }

    /**
     * Finds repairs by owner's ID.
     *
     * @param id The unique identifier of the owner.
     * @return A list of repairsdto objects representing repairs of the
     * specified owner's id.
     * @throws CustomException If no property is found with the given ID.
     */
    @Path("findByOwnerID/{id}")
    @GET
    @Produces("application/json")
    public List<RepairDto> findRepairByOwnerID(@PathParam("id") Long id) throws CustomException {
        List<Repair> repairs = repairService.findRepairsByOwner(id);
        List<RepairDto> allRepairs = repairs.stream()
                .map(repair -> new RepairDto(
                repair.getId(),
                repair.getProperty().getOwner().getVat(),
                repair.getProperty().getE9(),
                repair.getRepairType(),
                repair.getShortDescription(),
                repair.getSubmissionDate(),
                repair.getDescription(),
                repair.getScheduledStartDate(),
                repair.getScheduledEndDate(),
                repair.getProposedCost(),
                repair.getAcceptanceStatus(),
                repair.getRepairStatus(),
                repair.getRepairAddress(),
                repair.getActualStartDate(),
                repair.getActualEndDate(),
                repair.isDeleted()
        ))
                .collect(Collectors.toList());
        return allRepairs;
    }

    /**
     * Finds a repair by its unique ID.
     *
     * @param id The unique identifier of the repair.
     * @return A DTO representing the repair with the specified ID..
     * @throws CustomException If no repair is found with the given ID.
     */
    @Path("findByID/{id}")
    @GET
    @Produces("application/json")
    public RepairDto findRepairByID(@PathParam("id") Long id) throws CustomException {
        Repair repair = repairService.findRepairById(id).get();
        return new RepairDto(
                repair.getId(),
                repair.getProperty().getOwner().getVat(),
                repair.getProperty().getE9(),
                repair.getRepairType(),
                repair.getShortDescription(),
                repair.getSubmissionDate(),
                repair.getDescription(),
                repair.getScheduledStartDate(),
                repair.getScheduledEndDate(),
                repair.getProposedCost(),
                repair.getAcceptanceStatus(),
                repair.getRepairStatus(),
                repair.getRepairAddress(),
                repair.getActualStartDate(),
                repair.getActualEndDate(),
                repair.isDeleted()
        );
    }

    /**
     * Retrieves all repairs scheduled for a specific date.
     *
     * @param repairDate The date to filter repairs by, formatted as
     * "yyyy-MM-dd'T'HH:mm:ss".
     * @return A list of RepairDto representing all repairs scheduled for the
     * given date.
     * @throws ParseException if the date format is invalid.
     */
    @Path("findByDate")
    @GET
    @Produces("application/json")
    public List<RepairDto> findRepairByDate(@QueryParam("repairDate") String repairDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = sdf.parse(repairDate);
        List<Repair> repairs = repairService.findRepairsByDate(date);
        List<RepairDto> repairsByDate = repairs.stream()
                .map(repair -> new RepairDto(
                repair.getId(),
                repair.getProperty().getOwner().getVat(),
                repair.getProperty().getE9(),
                repair.getRepairType(),
                repair.getShortDescription(),
                repair.getSubmissionDate(),
                repair.getDescription(),
                repair.getScheduledStartDate(),
                repair.getScheduledEndDate(),
                repair.getProposedCost(),
                repair.getAcceptanceStatus(),
                repair.getRepairStatus(),
                repair.getRepairAddress(),
                repair.getActualStartDate(),
                repair.getActualEndDate(),
                repair.isDeleted()
        ))
                .collect(Collectors.toList());
        return repairsByDate;
    }

    /**
     * Retrieves all repairs in the system.
     *
     * @return A list of all RepairDto objects.
     * @throws CustomException if an error occurs while retrieving the records.
     */
    @Path("findAll")
    @GET
    @Produces("application/json")
    public List<RepairDto> allRepairs() throws CustomException {
        List<Repair> repairs = repairService.getRepairs();
        List<RepairDto> allRepairs = repairs.stream()
                .map(repair -> new RepairDto(
                repair.getId(),
                repair.getProperty().getOwner().getVat(),
                repair.getProperty().getE9(),
                repair.getRepairType(),
                repair.getShortDescription(),
                repair.getSubmissionDate(),
                repair.getDescription(),
                repair.getScheduledStartDate(),
                repair.getScheduledEndDate(),
                repair.getProposedCost(),
                repair.getAcceptanceStatus(),
                repair.getRepairStatus(),
                repair.getRepairAddress(),
                repair.getActualStartDate(),
                repair.getActualEndDate(),
                repair.isDeleted()
        ))
                .collect(Collectors.toList());
        return allRepairs;
    }

    /**
     * Soft deletes a repair record by marking it as deleted.
     *
     * @param id The ID of the repair to soft delete.
     * @return true if the repair was successfully soft deleted, false
     * otherwise.
     * @throws CustomException if no repair is found with the given ID or an
     * error occurs.
     */
    @Path("softDelete/{id}")
    @PUT
    @Produces("application/json")
    public boolean softDeleteRepair(@PathParam("id") Long id) throws CustomException {
        return repairService.deleteSafely(id);
    }

    /**
     * Permanently deletes a repair
     *
     * @param id The ID of the repair to delete.
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Path("hardDelete/{id}")
    @DELETE
    @Produces("application/json")
    public boolean deleteRepair(@PathParam("id") Long id) {
        return repairService.deletePermantlyById(id);
    }
}
