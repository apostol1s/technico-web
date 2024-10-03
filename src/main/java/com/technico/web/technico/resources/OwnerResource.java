package com.technico.web.technico.resources;

import com.technico.web.technico.dtos.OwnerDto;
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
import java.util.stream.Collectors;
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
     * @param owner the owner dto containing the details of the
     * new owner.
     * @return the created Owner dto.
     */
    @Path("create")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public OwnerDto saveOwner(OwnerDto owner) {
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
        return null;
    }

    /**
     * Finds an owner by VAT number.
     *
     * @param vat the VAT number of the owner.
     * @return the Owner dto object associated with the given VAT number.
     */
    @Path("findByVat/{vat}")
    @GET
    @Produces("application/json")
    public OwnerDto findOwnerByVat(@PathParam("vat") String vat) {
        Owner owner = ownerService.searchOwnerByVat(vat).get();
        return new OwnerDto(
                owner.getId(),
                owner.getVat(),
                owner.getName(),
                owner.getSurname(),
                owner.getAddress(),
                owner.getPhoneNumber(),
                owner.getEmail(),
                owner.getPassword(),
                owner.isDeleted()
        );
    }

    /**
     * Finds an owner by email.
     *
     * @param email the email address of the owner.
     * @return the Owner dto object associated with the given email address.
     */
    @Path("findByEmail/{email}")
    @GET
    @Produces("application/json")
    public OwnerDto findOwnerByEmail(@PathParam("email") String email) {
        Owner owner = ownerService.searchOwnerByEmail(email).get();
        return new OwnerDto(
                owner.getId(),
                owner.getVat(),
                owner.getName(),
                owner.getSurname(),
                owner.getAddress(),
                owner.getPhoneNumber(),
                owner.getEmail(),
                owner.getPassword(),
                owner.isDeleted()
        );
    }

    /**
     * Finds an owner by ID.
     *
     * @param id the unique ID of the owner.
     * @return the Owner dto object associated with the given ID.
     */
    @Path("findByID/{id}")
    @GET
    @Produces("application/json")
    public OwnerDto findOwnerByID(@PathParam("id") Long id) {
        Owner owner = ownerService.searchOwnerByID(id).get();
        return new OwnerDto(
                owner.getId(),
                owner.getVat(),
                owner.getName(),
                owner.getSurname(),
                owner.getAddress(),
                owner.getPhoneNumber(),
                owner.getEmail(),
                owner.getPassword(),
                owner.isDeleted()
        );
    }

    /**
     * Retrieves all owners.
     *
     * @return A list of OwnerDto objects representing all owners.
     */
    @Path("findAll")
    @GET
    @Produces("application/json")
    public List<OwnerDto> getCustomers() {
        List<Owner> owners = ownerService.findAllOwners();
        List<OwnerDto> allOwners = owners.stream()
                .map(owner -> new OwnerDto(
                owner.getId(),
                owner.getVat(),
                owner.getName(),
                owner.getSurname(),
                owner.getAddress(),
                owner.getPhoneNumber(),
                owner.getEmail(),
                owner.getPassword(),
                owner.isDeleted()
        ))
                .collect(Collectors.toList());
        return allOwners;
    }

    /**
     * Updates an existing owner.
     *
     * @param id the ID of the owner to update.
     * @param owner the updated owner dto.
     * @return the updated Owner dto.
     */
    @Path("update/{id}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public OwnerDto updateOwner(@PathParam("id") Long id, OwnerDto owner) {
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
        return null;
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

    /**
     * Endpoint to authenticate an owner based on email and password.
     *
     * @param email owner's email
     * @param password owner's password
     * @return the verified owner.
     */
    @Path("login/{email}/{password}")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Owner login(@PathParam("email") String email,
            @PathParam("password") String password) {
        try {
            return ownerService.verifyOwner(email, password).get();
        } catch (CustomException e) {
            log.debug("Error in hard deleting" + e.getMessage());
        }
        return null;
    }
}
