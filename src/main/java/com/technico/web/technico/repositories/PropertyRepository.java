package com.technico.web.technico.repositories;

import com.technico.web.technico.models.Owner;
import com.technico.web.technico.models.Property;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@NoArgsConstructor
public class PropertyRepository implements Repository<Property, Long> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    private Class<Property> getEntityClass() {
        return Property.class;
    }

    private String getEntityClassName() {
        return Property.class.getName();
    }

    /**
     * Saves a new property entity to the database.
     *
     * @param property the property entity to save.
     * @return an Optional containing the saved property.
     */
    @Override
    @Transactional
    public Optional<Property> save(Property property) {
        entityManager.persist(property);
        return Optional.of(property);
    }

    /**
     * Finds and returns an optional property based on the given property ID.
     *
     * @param id the ID of the property to search for.
     * @return an Optional containing the found property, or an empty Optional
     * if no property is found or an exception occurs.
     */
    @Override
    public Optional<Property> findById(Long id) {
        Property property;
        try {
            property = entityManager.find(getEntityClass(), id);
            return Optional.of(property);
        } catch (Exception e) {
            log.debug("Exception: " + e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves all Property entities from the database.
     *
     *
     * @return a List containing all Property entities found in the database
     */
    @Override
    public List<Property> findAll() {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Deletes a Property entity by its ID.
     *
     * @param id the ID of the Property entity to be deleted
     * @return true if the Property entity was found and successfully deleted;
     * false otherwise
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Property property = entityManager.find(getEntityClass(), id);
        if (property != null) {
            Owner owner = property.getOwner();
            if (owner != null) {
                owner.getPropertyList().remove(property);
            }
            entityManager.remove(property);
            return true;

        }
        return false;
    }

    /**
     * Finds and returns an optional property based on the given E9.
     *
     * @param e9 the E9 of the property to search for.
     * @return an Optional containing the found property, or an empty Optional
     * if no property is found.
     */
    public Optional<Property> findPropertyByE9(String e9) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where e9 = :e9 ",
                        getEntityClass())
                        .setParameter("e9", e9);
        return query.getResultStream().findFirst();
    }

    /**
     * Finds a list of Property entities by the owner's VAT identifier.
     *
     * @param vat the VAT identifier of the owner whose properties are to be
     * found
     * @return a List of Property entities associated with the specified owner's
     * VAT
     */
    public List<Property> findPropertyByVAT(String vat) {
        TypedQuery<Property> query
                = entityManager.createQuery("from Property p"
                        + " where p.owner.vat = :vat", Property.class)
                        .setParameter("vat", vat);
        return query.getResultList();
    }

    /**
     * Finds a list of Property entities by the owner's ID.
     *
     * @param ownerId Finds a list of Property entities by the owner's VAT
     * identifier.
     * @return a List of Property entities associated with the specified owner's
     * ID
     */
    public List<Property> findPropertyByOwnerID(Long ownerId) {
        return entityManager.createQuery(
                "SELECT p FROM Property p "
                + "WHERE p.owner.id = :ownerId", Property.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
    }
}
