package com.technico.web.technico.resources;

import com.technico.web.technico.dtos.CreateRepairDto;
import com.technico.web.technico.dtos.UpdateRepairAdminDto;
import com.technico.web.technico.dtos.UpdateRepairOwnerDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.Repair;
import com.technico.web.technico.models.RepairStatus;
import com.technico.web.technico.models.RepairType;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Path("repair")
public class RepairResource {

    @Inject
    private RepairService repairService;

    @Path("create")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Repair saveRepair(CreateRepairDto repair) {
        try {
            return repairService.createRepair(
                    repair.getRepairType(),
                    repair.getDescription(),
                    repair.getScheduledStartDate(),
                    repair.getScheduledEndDate(),
                    repair.getProposedCost(),
                    repair.getPropertyId()
            );
        } catch (CustomException e) {
            log.debug("Error whlie saving new property " + e.getMessage());
        }
        return new Repair();
    }

    @Path("updateAdmin/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Repair updateRepairAdmin(@PathParam("id") Long id, UpdateRepairAdminDto repair) {
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
        return new Repair();
    }

    @Path("updateOwner/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Repair updateRepairOwner(@PathParam("id") Long id, UpdateRepairOwnerDto repair) {
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
        return new Repair();
    }

    @Path("findByOwnerID/{id}")
    @GET
    @Produces("application/json")
    public List<Repair> findRepairByOwnerID(@PathParam("id") Long id) throws CustomException {
        return repairService.findRepairsByOwner(id);
    }

    @Path("findByID/{id}")
    @GET
    @Produces("application/json")
    public Repair findRepairByID(@PathParam("id") Long id) throws CustomException {
        return repairService.findRepairById(id).get();
    }

    @Path("findByRangeDate")
    @GET
    @Produces("application/json")
    public List<Repair> findRepairByDateRange(@QueryParam("repairDate") String repairDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = sdf.parse(repairDate);
        return repairService.findRepairsByRangeDates(date);
    }
    
    @Path("findAll")
    @GET
    @Produces("application/json")
    public List<Repair> allRepairs() throws CustomException {
        return repairService.getRepairs();
    }
    
    @Path("softDelete/{id}")
    @PUT
    @Produces("application/json")
    public boolean softDeleteRepair(@PathParam("id") Long id) throws CustomException {
        return repairService.deleteSafely(id);
    }
    
    @Path("hardDelete/{id}")
    @DELETE
    @Produces("application/json")
    public boolean deleteRepair(@PathParam("id") Long id) {
        return repairService.deletePermantlyById(id);
    }
}
