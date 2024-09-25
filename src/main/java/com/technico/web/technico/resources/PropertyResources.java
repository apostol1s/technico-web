package com.technico.web.technico.resources;

import com.technico.web.technico.dtos.CreatePropertyDto;
import com.technico.web.technico.dtos.UpdatePropertyDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.PropertyType;
import com.technico.web.technico.services.OwnerService;
import com.technico.web.technico.services.PropertyService;
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
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Path("property")
public class PropertyResources {

    @Inject
    private PropertyService propertyService;

    @Path("create")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Property saveProperty(CreatePropertyDto property) {
        try {
            return propertyService.createProperty(
                    property.getE9(),
                    property.getAddress(),
                    property.getYear(),
                    property.getPropertyType(),
                    property.getVat()
            );
        } catch (CustomException e) {
            log.debug("Error whlie saving new property " + e.getMessage());
        }
        return new Property();
    }

    @Path("findByE9/{e9}")
    @GET
    @Produces("application/json")
    public Property findPropertyByE9(@PathParam("e9") String e9) throws CustomException {
        return propertyService.findByE9(e9);
    }

    @Path("findByVat/{vat}")
    @GET
    @Produces("application/json")
    public List<Property> findPropertyByVat(@PathParam("vat") String vat) throws CustomException {
        return propertyService.findByVAT(vat);
    }
    
    @Path("findNonDeletedByVat/{vat}")
    @GET
    @Produces("application/json")
    public List<Property> findNonDeletedPropertyByVat(@PathParam("vat") String vat) throws CustomException {
        return propertyService.findByVATExcludeDeleted(vat);
    }

    @Path("findByID/{id}")
    @GET
    @Produces("application/json")
    public Property findPropertyByID(@PathParam("id") Long id) throws CustomException {
        return propertyService.findByID(id);
    }

    @Path("findAll")
    @GET
    @Produces("application/json")
    public List<Property> allProperties() {
        return propertyService.findAllProperties();
    }
    
    @Path("update/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Property updateProperty(@PathParam("id") Long id, UpdatePropertyDto property) {
        try {
            return propertyService.updateProperty(
                    id,
                    property.getPropertyAddress(),
                    property.getConstructionYear(),
                    property.getPropertyType()
            );
        } catch (CustomException e) {
            log.debug("Error whlie updating property " + e.getMessage());
        }
        return new Property();
    }

    @Path("softDelete/{id}")
    @PUT
    @Produces("application/json")
    public boolean softDeleteProperty(@PathParam("id") Long id) throws CustomException {
        return propertyService.safelyDeleteByID(id);
    }

    @Path("hardDelete/{id}")
    @DELETE
    @Produces("application/json")
    public boolean deleteProperty(@PathParam("id") Long id) {
        try {
            return propertyService.permenantlyDeleteByID(id);
        } catch (CustomException e) {
            log.debug("Error in soft deleting" + e.getMessage());
        }
        return false;
    }
}
