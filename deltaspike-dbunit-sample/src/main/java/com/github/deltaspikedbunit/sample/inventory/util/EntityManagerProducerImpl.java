package com.github.deltaspikedbunit.sample.inventory.util;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Produces (and disposes of) a JPA entity manager that can be injected.
 *
 * @author Luigi Bitonti
 */
@ApplicationScoped
public class EntityManagerProducerImpl implements EntityManagerProducer {

    @Inject
    @PersistenceUnitName("inventoryTest")
    private EntityManagerFactory entityManagerFactory;


    @Override
    @Produces
    @TransactionScoped
    public EntityManager create() {
        return this.entityManagerFactory.createEntityManager();
    }

    @Override
    public void dispose(@Disposes EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

}
