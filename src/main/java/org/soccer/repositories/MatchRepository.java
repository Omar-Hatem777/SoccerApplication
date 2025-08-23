package org.soccer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.soccer.models.Match;

import java.util.List;

public class MatchRepository {

    private final EntityManager em;

    public MatchRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Match match) {
        em.getTransaction().begin();
        em.persist(match);
        em.getTransaction().commit();
    }

    public Match findById(Long id) {
        return em.find(Match.class, id);
    }

    public List<Match> findAll() {
        TypedQuery<Match> query = em.createQuery("SELECT m FROM Match m", Match.class);
        return query.getResultList();
    }

    public void update(Match match) {
        em.getTransaction().begin();
        em.merge(match);
        em.getTransaction().commit();
    }

    public void delete(Match match) {
        em.getTransaction().begin();
        em.remove(match);
        em.getTransaction().commit();
    }

    public List<Match> findByLeagueId(Long leagueId) {
        TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.league.id = :leagueId", Match.class);
        query.setParameter("leagueId", leagueId);
        return query.getResultList();
    }

    public List<Match> findByTeamId(Long teamId) {
        TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId", Match.class);
        query.setParameter("teamId", teamId);
        return query.getResultList();
    }
}
