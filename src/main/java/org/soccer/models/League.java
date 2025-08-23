package org.soccer.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "leagues")
public class League {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();


    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    // Standings are not persisted directly; calculate or store in a separate entity if needed
    @Transient
    private Map<Team, Integer> standings; // Team points

    // Constructors
    public League() {}
    public League(String name) {
        this.name = name;
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Map<Team, Integer> getStandings() {
        return standings;
    }

    public void setStandings(Map<Team, Integer> standings) {
        this.standings = standings;
    }

    // Helper methods
    public void addTeam(Team team) {
        teams.add(team);
        team.setLeague(this);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
        team.setLeague(null);
    }
}
