package com.technico.web.technico.resources;

import com.technico.web.technico.dtos.CreatePropertyDto;
import com.technico.web.technico.dtos.UpdatePropertyDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Property;
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

    /**
     * Creates a new property using the provided property data.
     *
     * @param property A DTO containing property data.
     * @return The newly created property, or a null if an error
     * occurs.
     */
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
        return null;
    }

    /**
     * Finds a property by its E9 identifier.
     *
     * @param e9 The E9 identifier of the property.
     * @return The property with the specified E9.
     * @throws CustomException If no property is found with the given E9.
     */
    @Path("findByE9/{e9}")
    @GET
    @Produces("application/json")
    public Property findPropertyByE9(@PathParam("e9") String e9) throws CustomException {
        return propertyService.findByE9(e9);
    }

    /**
     * Finds a list of properties by the owner's VAT.
     *
     * @param vat The VAT number of the property owner.
     * @return A list of properties associated with the owner's VAT.
     * @throws CustomException If no properties are found for the given VAT.
     */
    @Path("findByVat/{vat}")
    @GET
    @Produces("application/json")
    public List<Property> findPropertyByVat(@PathParam("vat") String vat) throws CustomException {
        return propertyService.findByVAT(vat);
    }

    /**
     * Finds a list of properties by the owner's VAT, excluding deleted
     * properties.
     *
     * @param vat The VAT number of the property owner.
     * @return A list of non-deleted properties associated with the owner's VAT.
     * @throws CustomException If no non-deleted properties are found for the
     * given VAT.
     */
    @Path("findNonDeletedByVat/{vat}")
    @GET
    @Produces("application/json")
    public List<Property> findNonDeletedPropertyByVat(@PathParam("vat") String vat) throws CustomException {
        return propertyService.findByVATExcludeDeleted(vat);
    }

    /**
     * Finds a property by its unique ID.
     *
     * @param id The unique identifier of the property.
     * @return The property with the specified ID.
     * @throws CustomException If no property is found with the given ID.
     */
    @Path("findByID/{id}")
    @GET
    @Produces("application/json")
    public Property findPropertyByID(@PathParam("id") Long id) throws CustomException {
        return propertyService.findByID(id);
    }

    /**
     * Retrieves all properties from the system.
     *
     * @return A list of all properties.
     */
    @Path("findAll")
    @GET
    @Produces("application/json")
    public List<Property> allProperties() {
        return propertyService.findAllProperties();
    }

    /**
     * Updates the details of an existing property.
     *
     * @param id The ID of the property to update.
     * @param property A DTO containing the updated property details.
     * @return The updated property, or a null if an error occurs.
     */
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
        return null;
    }

    /**
     * Soft deletes a property by marking it as deleted, without physically
     * removing it.
     *
     * @param id The ID of the property to delete.
     * @return true if the property was successfully soft-deleted, false
     * otherwise.
     * @throws CustomException If no property is found with the given ID.
     */
    @Path("softDelete/{id}")
    @PUT
    @Produces("application/json")
    public boolean softDeleteProperty(@PathParam("id") Long id) throws CustomException {
        return propertyService.safelyDeleteByID(id);
    }

    /**
     * Hard deletes a property by permanently removing it from the database.
     *
     * @param id The ID of the property to delete.
     * @return true if the property was successfully hard-deleted, false
     * otherwise.
     */
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
