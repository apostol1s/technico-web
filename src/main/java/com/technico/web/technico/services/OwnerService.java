package com.technico.web.technico.services;

import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Owner;
import java.util.List;
import java.util.Optional;

public interface OwnerService {

    // Create Owner
    Owner createOwner(String vat, String name, String surname, String address, String phoneNumber, String email, String password)
            throws CustomException;

    // Search Owner
    Optional<Owner> searchOwnerByVat(String vat);

    Optional<Owner> searchOwnerByEmail(String email);
    
    Optional<Owner> searchOwnerByID(Long id);
    
    List<Owner> findAllOwners();

    // Update Owner
    Owner updateOwner(Long id, String address, String phoneNumber, String email, String password) throws CustomException;
//    void updateOwnerAddress(String vat, String address) throws CustomException;
//
//    void updateOwnerEmail(String vat, String email) throws CustomException;
//
//    void updateOwnerPassword(String vat, String password) throws CustomException;

    // Delete Owner
    boolean deleteOwnerPermanently(Long id) throws CustomException;

    boolean deleteOwnerSafely(Long id);

    // Verify Owner
//    Optional<String> verifyOwner(String username, String password) throws CustomException;

    // Validations
    void validateVat(String vat) throws CustomException;

    void validateName(String name) throws CustomException;

    void validateSurname(String surname) throws CustomException;

    void validatePassword(String password) throws CustomException;

    void validatePhone(String phone) throws CustomException;

    void validateEmail(String email) throws CustomException;

    void checkVat(String vat) throws CustomException;

    void checkEmail(String email) throws CustomException;
}
