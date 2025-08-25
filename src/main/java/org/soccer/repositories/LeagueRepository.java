package org.soccer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.soccer.models.League;

import java.util.List;

public class LeagueRepository {

    private final EntityManager em;

    public LeagueRepository(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
    public void save(League league) {
        em.getTransaction().begin();
        em.persist(league);
        em.getTransaction().commit();
    }

    public League findById(Long id) {
        return em.find(League.class, id);
    }

    public List<League> findAll() {
        TypedQuery<League> query = em.createQuery("SELECT l FROM League l", League.class);
        return query.getResultList();
    }

    public void update(League league) {
        em.getTransaction().begin();
        em.merge(league);
        em.getTransaction().commit();
    }

    public void delete(League league) {
        em.getTransaction().begin();
        em.remove(league);
        em.getTransaction().commit();
    }

    // Custom queries
    public List<League> findByName(String name) {
        TypedQuery<League> query = em.createQuery(
                "SELECT l FROM League l WHERE l.name LIKE :name", League.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
}
