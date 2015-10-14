package com.github.deltaspikedbunit.sample.inventory.util;

import javax.persistence.EntityManager;


public interface EntityManagerProducer {

    EntityManager create();

    void dispose(EntityManager entityManager);
}
