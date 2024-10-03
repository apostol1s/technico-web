package com.technico.web.technico.services;

import com.technico.web.technico.dtos.OwnerDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import com.technico.web.technico.repositories.OwnerRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@Slf4j
@RequestScoped
public class OwnerServiceImpl implements OwnerService {

    @Inject
    private OwnerRepository ownerRepository;

    /**
     * Creates a new owner with the given details
     *
     * @param vat
     * @param name
     * @param surname
     * @param address
     * @param phoneNumber
     * @param email
     * @param password
     * @return the new Owner dto.
     * @throws CustomException if any validation fails
     */
    @Override
    public OwnerDto createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String password)
            throws CustomException {
        validateVat(vat);
        validateName(name);
        validateSurname(surname);
        validatePhone(phoneNumber);
        validateEmail(email);
        validatePassword(password);

        checkVat(vat);
        checkEmail(email);

        Owner owner = new Owner();
        owner.setVat(vat);
        owner.setAddress(address);
        owner.setName(name);
        owner.setSurname(surname);
        owner.setPhoneNumber(phoneNumber);
        owner.setPassword(password);
        owner.setEmail(email);

        Owner savedOwner = save(owner);
        return new OwnerDto(
                savedOwner.getId(),
                savedOwner.getVat(),
                savedOwner.getName(),
                savedOwner.getSurname(),
                savedOwner.getAddress(),
                savedOwner.getPhoneNumber(),
                savedOwner.getEmail(),
                savedOwner.getPassword(),
                savedOwner.isDeleted()
        );
    }

    /**
     * Searches Owner by its VAT
     *
     * @param vat
     * @return an Optional containing the found Owner, or an empty Optional if
     * no Owner was found
     */
    @Override
    public Optional<Owner> searchOwnerByVat(String vat) {
        return ownerRepository.findByVat(vat);
    }

    /**
     * Searches Owner by its email
     *
     * @param email
     * @return an Optional containing the found Owner, or an empty Optional if
     * no Owner was found
     */
    @Override
    public Optional<Owner> searchOwnerByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    /**
     * Searches Owner by its id
     *
     * @param id
     * @return an Optional containing the found Owner, or an empty Optional if
     * no Owner was found
     */
    @Override
    public Optional<Owner> searchOwnerByID(Long id) {
        return ownerRepository.findById(id);
    }

    /**
     * Retrieves all owners from the repository.
     *
     * @return A list of all Owner objects in the repository.
     */
    @Override
    public List<Owner> findAllOwners() {
        return ownerRepository.findAll();
    }

    /**
     * Updates the details of an existing Owner.
     *
     * @param id The unique identifier of the Owner to be updated.
     * @param address The new address of the Owner.
     * @param phoneNumber The new phone number of the Owner.
     * @param email The new email address of the Owner.
     * @param password The new password for the Owner.
     * @return The updated Owner dto object.
     * @throws CustomException If the Owner is deleted, if the phone number, or
     * if any validation fails.
     */
    @Override
    @Transactional
    public OwnerDto updateOwner(Long id, String address, String phoneNumber, String email, String password)
            throws CustomException {
        Owner owner = searchOwnerByID(id).get();
        if (owner.isDeleted()) {
            throw new CustomException("Cannot update a deleted owner.");
        }

        owner.setAddress(address);
        validatePhone(phoneNumber);
        owner.setPhoneNumber(phoneNumber);
        validateEmail(email);
        if (!owner.getEmail().equals(email)) {
            checkEmail(email);
            owner.setEmail(email);
        }
        validatePassword(password);
        owner.setPassword(password);

        Owner savedOwner = save(owner);
        return new OwnerDto(
                savedOwner.getId(),
                savedOwner.getVat(),
                savedOwner.getName(),
                savedOwner.getSurname(),
                savedOwner.getAddress(),
                savedOwner.getPhoneNumber(),
                savedOwner.getEmail(),
                savedOwner.getPassword(),
                savedOwner.isDeleted()
        );
    }

//    /**
//     * Updates the address of an owner identified by VAT
//     *
//     * @param vat
//     * @param address
//     * @throws CustomException if the owner is not found or the update fails
//     */
//    @Override
//    public void updateOwnerAddress(String vat, String address) throws CustomException {
//        Owner owner = getOwnerByVat(vat);
//        owner.setAddress(address);
//        save(owner);
//    }
//
//    /**
//     * Updates the email of an owner identified by VAT
//     *
//     * @param vat
//     * @param email
//     * @throws CustomException if the email is invalid, already exists, or the
//     * update fails
//     */
//    @Override
//    public void updateOwnerEmail(String vat, String email) throws CustomException {
//        Owner owner = getOwnerByVat(vat);
//        validateEmail(email);
//        if (!email.equals(owner.getEmail())) {
//            checkEmail(email);
//            owner.setEmail(email);
//        }
//        save(owner);
//    }
//
//    /**
//     * Updates the password of an owner identified by VAT
//     *
//     * @param vat
//     * @param password
//     * @throws CustomException if the password is invalid or the update fails
//     */
//    @Override
//    public void updateOwnerPassword(String vat, String password) throws CustomException {
//        Owner owner = getOwnerByVat(vat);
//        validatePassword(password);
//        owner.setPassword(password);
//        save(owner);
//    }   
    /**
     * Permanently deletes an owner by ID
     *
     * @param id
     * @return true if the owner was deleted, false otherwise
     * @throws CustomException if the property could not be deleted
     */
    @Override
    public boolean deleteOwnerPermanently(Long id) throws CustomException {
        boolean success = ownerRepository.deleteById(id);
        if (!success) {
            throw new CustomException("Failed to permanently delete property with ID: " + id);
        }
        return true;
    }

    /**
     * Soft deletes an owner by ID, marking the owner as deleted
     *
     * @param id
     * @return true if the owner was marked as deleted, false otherwise
     */
    @Override
    @Transactional
    public boolean deleteOwnerSafely(Long id) {
        try {
            Owner owner = searchOwnerByID(id).get();
            owner.setDeleted(true);
            save(owner);

            return true;
        } catch (CustomException e) {
            return false;
        }
    }

    /**
     * Verifies the owner's credentials
     *
     * @param email
     * @param password
     * @return an Optional containing the VAT of the verified owner
     * @throws CustomException if the username or password is null/blank or
     * invalid
     */
    @Override
    public Optional<Owner> verifyOwner(String email, String password) throws CustomException {
        if (email == null || email.isBlank()) {
            throw new CustomException("Email cannot be null or blank.");
        }
        if (password == null || password.isBlank()) {
            throw new CustomException("Password cannot be null or blank.");
        }

        Owner owner = ownerRepository.findByUsernameAndPassword(email, password)
                .orElseThrow(() -> new CustomException("Invalid username or password."));
        return Optional.of(owner);
    }

    /**
     * Validates the VAT.
     *
     * @param vat
     * @throws CustomException if the VAT is null or not exactly 9 characters
     *
     */
    @Override
    public void validateVat(String vat) throws CustomException {
        if (vat == null || vat.length() != 9) {
            throw new CustomException("VAT must be exactly 9 characters.");
        }
    }

    /**
     * Validates the name
     *
     * @param name
     * @throws CustomException if the name is null or blank
     */
    @Override
    public void validateName(String name) throws CustomException {
        if (name == null || name.isBlank()) {
            throw new CustomException("Name cannot be null or blank.");
        }
    }

    /**
     * Validates the surname
     *
     * @param surname
     * @throws CustomException if the surname is null or blank
     */
    @Override
    public void validateSurname(String surname) throws CustomException {
        if (surname == null || surname.isBlank()) {
            throw new CustomException("Surname cannot be null or blank.");
        }
    }

    /**
     * Validates the password
     *
     * @param password
     * @throws CustomException if the password is less than 8 characters long
     */
    @Override
    public void validatePassword(String password) throws CustomException {
        if (password.length() < 8) {
            throw new CustomException("Password must be at least 8 characters.");
        }
    }

    /**
     * Validates the phone number
     *
     * @param phone
     * @throws CustomException if the phone number is more than 14 characters
     * long or contains non-numeric characters
     */
    @Override
    public void validatePhone(String phone) throws CustomException {
        if (phone.length() > 14) {
            throw new CustomException("Phone number must be at most 14 characters.");
        }
        if (!phone.matches("\\d+")) {
            throw new CustomException("Phone number must contain only numeric characters.");
        }
    }

    /**
     * Validates the email
     *
     * @param email
     * @throws CustomException if the email is invalid
     */
    @Override
    public void validateEmail(String email) throws CustomException {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (email == null || !emailPattern.matcher(email).matches()) {
            throw new CustomException("Invalid email format.");
        }
    }

    /**
     * Checks if the VAT already exists
     *
     * @param vat
     * @throws CustomException if the VAT already exists
     */
    @Override
    public void checkVat(String vat) throws CustomException {
        if (ownerRepository.findByVat(vat).isPresent()) {
            throw new CustomException("VAT already exists. If you have deleted your account, contact the admin!");
        }
    }

    /**
     * Checks if the email already exists
     *
     * @param email
     * @throws CustomException if the email already exists
     */
    @Override
    public void checkEmail(String email) throws CustomException {
        if (ownerRepository.findByEmail(email).isPresent()) {
            throw new CustomException("Email already exists.");
        }
    }

    /**
     * Saves the given Owner
     *
     * @param owner
     * @throws CustomException if the save operation fails
     */
    @Transactional
    private Owner save(Owner owner) throws CustomException {
        try {
            return ownerRepository.save(owner).get();
        } catch (Exception e) {
            throw new CustomException("Failed to save owner details: " + e.getMessage());
        }
    }

    /**
     * Retrieves the Owner by VAT
     *
     * @param vat
     * @return the Owner
     * @throws CustomException if the Owner with the given VAT is not found
     */
//    public Owner getOwnerByVat(String vat) throws CustomException {
//        return ownerRepository.findByVat(vat)
//                .orElseThrow(() -> new CustomException("Owner with the given VAT number not found."));
//    }
}
