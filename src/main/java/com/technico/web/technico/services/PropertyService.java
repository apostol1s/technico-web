package com.technico.web.technico.services;

import com.technico.web.technico.dtos.PropertyDto;
import com.technico.web.technico.exceptions.CustomException;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.PropertyType;
import java.util.List;

public interface PropertyService {
    PropertyDto createProperty(String e9, String address, int year, PropertyType propertyType, String vat) throws CustomException;
    PropertyDto updateProperty(Long id, String propertyAddress, int constructionYear, PropertyType propertyType) throws CustomException;
//    Property updatePropertyE9(Property property, String e9) throws CustomException;
//    Property updatePropertyAddress(Property property, String address) throws CustomException;
//    Property updatePropertyConstructionYear(Property property, int year) throws CustomException;
//    Property updatePropertyType(Property property, PropertyType propertyType) throws CustomException;
    Property findByE9(String e9) throws CustomException;
//    Property findByE9ForCreate(String e9) throws CustomException;
    List<Property> findByVAT(String vat) throws CustomException;
    List<Property> findByVATExcludeDeleted(String vat) throws CustomException;
    List<Property> findAllProperties();
    List<Property> findPropertyByOwnerID(Long id) throws CustomException;
    Property findByID(Long id) throws CustomException;
    boolean safelyDeleteByID(Long id) throws CustomException;
    boolean permenantlyDeleteByID(Long id) throws CustomException;
    void validateE9(String e9) throws CustomException;    
    void validateConstructionYear(String yearInput) throws CustomException;
    void validatePropertyType(PropertyType propertyType) throws CustomException;  
}
