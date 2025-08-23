package org.soccer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.soccer.models.Player;

import java.util.List;

public class PlayerRepository {

    private final EntityManager em; // EntityManager for database operations

    public PlayerRepository(EntityManager em) {
        this.em = em;
    } // Constructor to initialize EntityManager

    // CRUD operations
    public void save(Player player) {
        em.getTransaction().begin();
        em.persist(player);
        em.getTransaction().commit();
    } // Save a new player

    public Player findById(Long id) {
        return em.find(Player.class, id);
    } // Find a player by ID

    public List<Player> findAll() {
        TypedQuery<Player> query = em.createQuery("SELECT p FROM Player p", Player.class);
        return query.getResultList();
    } // Find all players

    public void update(Player player) {
        em.getTransaction().begin();
        em.merge(player);
        em.getTransaction().commit();
    } // Update an existing player

    public void delete(Player player) {
        em.getTransaction().begin();
        em.remove(player);
        em.getTransaction().commit();
    } // Delete a player

    // Custom query
    public List<Player> findByPosition(String position) {
        TypedQuery<Player> query = em.createQuery(
                "SELECT p FROM Player p WHERE p.position = :position", Player.class);
        query.setParameter("position", position);
        return query.getResultList();
    } // Find players by position

    public List<Player> findByTeamId(Long teamId) {
        TypedQuery<Player> query = em.createQuery(
                "SELECT p FROM Player p WHERE p.team.id = :teamId", Player.class);
        query.setParameter("teamId", teamId);
        return query.getResultList();
    } // Find players by team ID
}
