package com.technico.web.technico.repositories;

import com.technico.web.technico.models.Owner;
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
public class OwnerRepository implements Repository<Owner, Long> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    /**
     * Saves a new owner entity to the database.
     *
     * @param owner the owner entity to save.
     * @return an Optional containing the saved owner.
     */
    @Override
    @Transactional
    public Optional<Owner> save(Owner owner) {
        entityManager.persist(owner);
        return Optional.of(owner);
    }

    /**
     * Finds an owner by their VAT number.
     *
     * @param vat the VAT number of the owner to find.
     * @return an Optional containing the found owner.
     */
    public Optional<Owner> findByVat(String vat) {
        TypedQuery<Owner> query = entityManager.createQuery("FROM Owner WHERE vat = :vat", Owner.class);
        query.setParameter("vat", vat);
        return query.getResultStream().findFirst();
    }

    /**
     * Finds an owner by their email.
     *
     * @param email the email of the owner to find.
     * @return an Optional containing the found owner.
     */
    public Optional<Owner> findByEmail(String email) {
        TypedQuery<Owner> query = entityManager.createQuery("FROM Owner WHERE email = :email", Owner.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

//    /**
//     * Finds an owner by their username.
//     *
//     * @param username the username of the owner to find.
//     * @return an Optional containing the found owner.
//     */
//    public Optional<Owner> findByUsername(String username) {
//        TypedQuery<Owner> query = entityManager.createQuery("FROM Owner WHERE username = :username", Owner.class);
//        query.setParameter("username", username);
//        return query.getResultStream().findFirst();
//    }

    /**
     * Finds an owner by their username and password.
     *
     * @param email the email of the owner.
     * @param password the password of the owner.
     * @return an Optional containing the found owner.
     */
    public Optional<Owner> findByUsernameAndPassword(String email, String password) {
        TypedQuery<Owner> query = entityManager.createQuery(
                "FROM Owner WHERE email = :email AND password = :password AND isDeleted = false",
                Owner.class
        );
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.getResultStream().findFirst();
    }

    /**
     * Permanently deletes an owner by their VAT number.
     *
     * @param vat the VAT number of the owner to delete
     * @return true if the owner was deleted successfully, false if not found
     */
//    @Transactional
//    public boolean deletePermanentlyByVat(String vat) {
//        Optional<Owner> optionalOwner = findByVat(vat);
//
//        if (optionalOwner.isPresent()) {
//            Owner owner = optionalOwner.get();
//            entityManager.remove(owner);
//            return true;
//        }
//        return false;
//    }

    /**
     * Finds an owner by their ID.
     *
     * @param id the ID of the owner to find.
     * @return an Optional containing the found owner if present, or empty if not found.
     */
    @Override
    public Optional<Owner> findById(Long id) {
        Owner owner;
        try {
            owner = entityManager.find(getEntityClass(), id);
            return Optional.of(owner);
        } catch (Exception e) {
            log.debug("An exception occured");
            return Optional.empty();
        }
    }

     /**
     * Retrieves all owner entities from the database.
     *
     * @return a list of all owners
     */
    @Override
    public List<Owner> findAll() {
        TypedQuery<Owner> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Deletes an owner by their ID.
     *
     * @param id the ID of the owner to delete.
     * @return true if the owner was deleted successfully, false if not found.
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Owner owner = entityManager.find(getEntityClass(), id);
        if (owner != null) {
            entityManager.remove(owner);
            return true;
        }
        return false;
    }

    private Class<Owner> getEntityClass() {
        return Owner.class;
    }

    private String getEntityClassName() {
        return Owner.class.getName();
    }
}
