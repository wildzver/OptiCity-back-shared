package svidnytskyy.glassesspring.repositories;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class BuildSearchIndex implements ApplicationListener<ApplicationReadyEvent> {

    @PersistenceContext
    private EntityManager entityManager;

    public void createIndexer() {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println("Error occured trying to build Hibernate Search indexes "
                    + e.toString());
        }
    }
    @Override
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        createIndexer();
    }
}