package org.soccer.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class Team {

    // Attributes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String coachName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    private int totalPoints; // league/tournament points

    private int score; // current match score

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league; // Many-to-One relationship with League


    // Constructors
    public Team() {
    }

    public Team(String name, String coachName) {
        this.name = name;
        this.coachName = coachName;
        this.score = 0;
        this.totalPoints = 0;
    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    //Helper methods

    public void addPlayer(Player player)
    {
        this.players.add(player);
        player.setTeam(this);
    }

    public void removePlayer(Player player)
    {
        this.players.remove(player);
        player.setTeam(null);
    }

    public void resetScore()
    {
        this.score = 0;
    }
}

