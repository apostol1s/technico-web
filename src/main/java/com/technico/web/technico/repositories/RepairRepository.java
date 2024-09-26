package com.technico.web.technico.repositories;

import com.technico.web.technico.models.Owner;
import com.technico.web.technico.models.Property;
import com.technico.web.technico.models.Repair;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@NoArgsConstructor
public class RepairRepository implements Repository<Repair, Long> {

    @PersistenceContext(unitName = "Persistence")
    private EntityManager entityManager;

    /**
     * Saves a new repair entity to the database.
     *
     * @param repair the repair entity to save.
     * @return an Optional containing the saved repair.
     */
    @Override
    @Transactional
    public Optional<Repair> save(Repair repair) {
        entityManager.persist(repair);
        return Optional.of(repair);
    }

    /**
     * Retrieves all Repair entities from the database.
     *
     * @return A List of all Repair entities.
     */
    @Override
    public List<Repair> findAll() {
        TypedQuery<Repair> query = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Finds a Repair entity by its ID.
     *
     * @param id The ID of the repair to be found.
     * @return An Optional containing the found Repair, or Optional.empty() if
     * no repair was found.
     */
    @Override
    public Optional<Repair> findById(Long id) {
        Repair repair;
        try {
            repair = entityManager.find(getEntityClass(), id);
            return Optional.of(repair);
        } catch (Exception e) {
            log.debug("An exception occured");
            return Optional.empty();
        }
    }

    /**
     * Deletes a Repair entity by its ID.
     *
     * @param id The ID of the repair to be deleted.
     * @return true if the repair was successfully deleted, false otherwise.
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        Repair repair = entityManager.find(getEntityClass(), id);
        if (repair != null) {
            Property property = repair.getProperty();
            if (property != null) {
                property.getRepairs().remove(repair);
            }
            entityManager.remove(repair);
            return true;

        }
        return false;
    }

    /**
     * Marks a Repair entity as deleted by setting its deleted flag to true and
     * saving the updated entity to the database.
     *
     * @param repair The repair entity to be safely deleted. Must not be null.
     * @return true if the repair was successfully marked as deleted, false
     * otherwise.
     */
    public boolean safeDelete(Repair repair) {
        repair.setDeleted(true);
        Optional<Repair> safelyDeletedRepair = save(repair);
        if (safelyDeletedRepair.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * Finds all Repair entities associated with the given owner.
     *
     * @param owner The owner whose repairs are to be retrieved. Must not be
     * null.
     * @return A List of Repair entities associated with the specified owner.
     */
//    public List<Repair> findRepairsByOwner() {
//        TypedQuery<Repair> query
//                = entityManager.createQuery("from " + getEntityClassName()
//                        + " where property.owner.vat  like :vat ",
//                        getEntityClass())
//                        .setParameter("vat", vat);
//        return query.getResultList();
//    }
    /**
     * Finds all Repair entities with a status of "PENDING".
     *
     * @return A List of pending Repair entities.
     */
//    public List<Repair> findPendingRepairs() {
//        TypedQuery<Repair> query
//                = entityManager.createQuery("from " + getEntityClassName()
//                        + " where repair_status  like :repair_status ",
//                        getEntityClass())
//                        .setParameter("repair_status", "PENDING");
//        return query.getResultList();
//    }

    /**
     *
     * Finds all Repair entities with a status of "PENDING" for a specific
     * owner.
     *
     * @param owner The owner whose pending repairs are to be retrieved. Must
     * not be null.
     * @return A List of pending Repair entities associated with the specified
     * owner.
     */
//    public List<Repair> findPendingRepairsByOwner(Owner owner) {
//        TypedQuery<Repair> query
//                = entityManager.createQuery("from " + getEntityClassName()
//                        + " where repair_status  like :repair_status "
//                        + " and owner like :owner "
//                        + " and proposed_cost != null",
//                        getEntityClass())
//                        .setParameter("owner", owner)
//                        .setParameter("repair_status", "PENDING");
//        return query.getResultList();
//    }

    /**
     * Finds all Repair entities associated with a specific property.
     *
     * @param property The property whose repairs are to be retrieved. Must not
     * be null.
     * @return A List of Repair entities associated with the specified property.
     */
//    public List<Repair> findRepairsByPropertyId(Property property) {
//        TypedQuery<Repair> query
//                = entityManager.createQuery("from " + getEntityClassName()
//                        + " where property  like :property ",
//                        getEntityClass())
//                        .setParameter("property", property);
//        return query.getResultList();
//    }

    /**
     * Finds all Repair entities with a status of "INPROGRESS".
     *
     * @return A List of in-progress Repair entities.
     */
//    public List<Repair> findInProgressRepairs() {
//        TypedQuery<Repair> query
//                = entityManager.createQuery("from " + getEntityClassName()
//                        + " where repair_status  like :repair_status ",
//                        getEntityClass())
//                        .setParameter("repair_status", "INPROGRESS");
//        return query.getResultList();
//    }

    /**
     * Finds all Repair entities that have been accepted.
     *
     * @return A List of accepted Repair entities.
     */
//    public List<Repair> findAcceptedRepairs() {
//        TypedQuery<Repair> query
//                = entityManager.createQuery("from " + getEntityClassName()
//                        + " where acceptance_status  = 1 ",
//                        getEntityClass());
//        return query.getResultList();
//    }

    /**
     * Finds all Repair entities within a specific date.
     *
     * @param repairDate the specific date.
     * @return a List if repair objects that exists that specific date.
     */
    public List<Repair> findRepairsByDate(Date repairDate) {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where scheduledStartDate <= :repairDate AND scheduledEndDate >= :repairDate",
                        getEntityClass())
                        .setParameter("repairDate", repairDate);

        return query.getResultList();
    }

    /**
     * Finds all Repair entities within a specific date range. Optionally
     * filters by owner.
     *
     * @param startDate The start date of the range. Must not be null.
     * @param endDate The end date of the range. Must not be null.
     * @param owner The owner to filter the repairs by. Can be null if no
     * filtering by owner is needed.
     * @return 
     */
//    public List<Repair> findRepairsByDates(LocalDateTime startDate, LocalDateTime endDate, Owner owner) {
//        if (owner == null) {
//            TypedQuery<Repair> query
//                    = entityManager.createQuery("from " + getEntityClassName()
//                            + " where submission_date between :startDate and :endDate",
//                            getEntityClass())
//                            .setParameter("startDate", startDate)
//                            .setParameter("endDate", endDate);
//
//            return query.getResultList();
//        } else {
//            TypedQuery<Repair> query
//                    = entityManager.createQuery("from " + getEntityClassName()
//                            + " where submission_date between :startDate and :endDate"
//                            + " and owner like :owner ",
//                            getEntityClass())
//                            .setParameter("startDate", startDate)
//                            .setParameter("endDate", endDate)
//                            .setParameter("owner", owner);
//            return query.getResultList();
//        }
//
//    }

    /**
     * Returns the Class object representing the Repair entity.
     *
     */
    private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    /**
     * Returns the name of the Repair entity class.
     *
     * @return The name of the Repair entity class.
     */
    private String getEntityClassName() {
        return Repair.class.getName();
    }
}
