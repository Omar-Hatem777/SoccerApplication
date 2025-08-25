package org.soccer.repositories;

import java.util.List;

import org.soccer.enums.MatchStatus;
import org.soccer.models.Match;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class MatchRepository {

    private final EntityManager em;

    public MatchRepository(EntityManager em) {
        this.em = em;
    }

    // CRUD operations
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

    // Custom queries
    public List<Match> findByTeamId(Long teamId) {
        TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.homeTeam.id = :teamId OR m.awayTeam.id = :teamId", Match.class);
        query.setParameter("teamId", teamId);
        return query.getResultList();
    }

    public List<Match> findByLeagueId(Long leagueId) {
        TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.league.id = :leagueId", Match.class);
        query.setParameter("leagueId", leagueId);
        return query.getResultList();
    }

    public List<Match> findByStatus(MatchStatus status) {
        TypedQuery<Match> query = em.createQuery(
                "SELECT m FROM Match m WHERE m.status = :status", Match.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<Match> findOngoingMatches() {
        return findByStatus(MatchStatus.ONGOING);
    }

    public List<Match> findScheduledMatches() {
        return findByStatus(MatchStatus.SCHEDULED);
    }

    public List<Match> findCompletedMatches() {
        return findByStatus(MatchStatus.FINISHED);
    }
}
