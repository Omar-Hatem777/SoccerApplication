package org.soccer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.soccer.models.Team;

import java.util.List;

public class TeamRepository {

    private final EntityManager em;

    public TeamRepository(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
    public void save(Team team) {
        em.getTransaction().begin();
        em.persist(team);
        em.getTransaction().commit();
    }

    public Team findById(Long id) {
        return em.find(Team.class, id);
    }

    public List<Team> findAll() {
        TypedQuery<Team> query = em.createQuery("SELECT t FROM Team t", Team.class);
        return query.getResultList();
    }

    public void update(Team team) {
        em.getTransaction().begin();
        em.merge(team);
        em.getTransaction().commit();
    }

    public void delete(Team team) {
        em.getTransaction().begin();
        em.remove(team);
        em.getTransaction().commit();
    }

    // Custom queries
    public List<Team> findByLeagueId(Long leagueId) {
        TypedQuery<Team> query = em.createQuery(
                "SELECT t FROM Team t WHERE t.league.id = :leagueId", Team.class);
        query.setParameter("leagueId", leagueId);
        return query.getResultList();
    }

    public List<Team> findByName(String name) {
        TypedQuery<Team> query = em.createQuery(
                "SELECT t FROM Team t WHERE t.name LIKE :name", Team.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    public List<Team> findByCoachName(String coachName) {
        TypedQuery<Team> query = em.createQuery(
                "SELECT t FROM Team t WHERE t.coachName LIKE :coachName", Team.class);
        query.setParameter("coachName", "%" + coachName + "%");
        return query.getResultList();
    }
}
