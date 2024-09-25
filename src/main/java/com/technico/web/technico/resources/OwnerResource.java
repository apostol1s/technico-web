package com.technico.web.technico.resources;

import com.technico.web.technico.dtos.CreateOwnerDto;
import com.technico.web.technico.dtos.UpdateOwnerDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import com.technico.web.technico.services.OwnerService;
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
@Path("owner")
public class OwnerResource {

    @Inject
    private OwnerService ownerService;

    /**
     * Saves a new owner.
     *
     * @param owner the owner data transfer object containing the details of the
     * new owner.
     * @return the created Owner object.
     */
    @Path("create")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Owner saveOwner(CreateOwnerDto owner) {
        try {
            return ownerService.createOwner(
                    owner.getVat(),
                    owner.getName(),
                    owner.getSurname(),
                    owner.getAddress(),
                    owner.getPhoneNumber(),
                    owner.getEmail(),
                    owner.getPassword()
            );
        } catch (CustomException e) {
            log.debug("Error whlie saving new owner " + e.getMessage());
        }
        return new Owner();
    }

    /**
     * Finds an owner by VAT number.
     *
     * @param vat the VAT number of the owner.
     * @return the Owner object associated with the given VAT number.
     */
    @Path("findByVat/{vat}")
    @GET
    @Produces("application/json")
    public Owner findOwnerByVat(@PathParam("vat") String vat) {
        return ownerService.searchOwnerByVat(vat).get();
    }

    /**
     * Finds an owner by email.
     *
     * @param email the email address of the owner.
     * @return the Owner object associated with the given email address.
     */
    @Path("findByEmail/{email}")
    @GET
    @Produces("application/json")
    public Owner findOwnerByEmail(@PathParam("email") String email) {
        return ownerService.searchOwnerByEmail(email).get();
    }

    /**
     * Finds an owner by ID.
     *
     * @param id the unique ID of the owner.
     * @return the Owner object associated with the given ID.
     */
    @Path("findByID/{id}")
    @GET
    @Produces("application/json")
    public Owner findOwnerByID(@PathParam("id") Long id) {
        return ownerService.searchOwnerByID(id).get();
    }

    /**
     * Retrieves all owners.
     *
     * @return a list of Owner objects.
     */
    @Path("findAll")
    @GET
    @Produces("application/json")
    public List<Owner> getCustomers() {
        return ownerService.findAllOwners();
    }

    /**
     * Updates an existing owner.
     *
     * @param id the ID of the owner to update.
     * @param owner the updated owner data transfer object.
     * @return the updated Owner object.
     */
    @Path("update/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Owner updateOwner(@PathParam("id") Long id, UpdateOwnerDto owner) {
        try {
            return ownerService.updateOwner(
                    id,
                    owner.getAddress(),
                    owner.getPhoneNumber(),
                    owner.getEmail(),
                    owner.getPassword()
            );
        } catch (CustomException e) {
            log.debug("Error whlie updating owner " + e.getMessage());
        }
        return new Owner();
    }

    /**
     * Soft deletes an owner.
     *
     * @param id the ID of the owner to soft delete.
     * @return true if the owner was successfully soft deleted, false otherwise.
     */
    @Path("softDelete/{id}")
    @PUT
    @Produces("application/json")
    public boolean softDeleteOwner(@PathParam("id") Long id) {
        return ownerService.deleteOwnerSafely(id);
    }

    /**
     * Permanently deletes an owner.
     *
     * @param id the ID of the owner to delete.
     * @return true if the owner was successfully deleted, false otherwise.
     */
    @Path("hardDelete/{id}")
    @DELETE
    @Produces("application/json")
    public boolean deleteOwner(@PathParam("id") Long id) {
        try {
            return ownerService.deleteOwnerPermanently(id);
        } catch (CustomException e) {
            log.debug("Error in hard deleting" + e.getMessage());
        }
        return false;
    }
}
