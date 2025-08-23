package org.soccer.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.soccer.enums.MatchStatus;

@Entity
@Table(name = "matches")
public class Match {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(nullable = false)
    private int homeScore = 0;

    @Column(nullable = false)
    private int awayScore = 0;

    @Column(nullable = false)
    private int matchTime = 0; // minutes elapsed

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    // Constructors
    public Match() {}

    public Match(Team homeTeam, Team awayTeam, League league) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.league = league;
        this.status = MatchStatus.SCHEDULED;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(int matchTime) {
        this.matchTime = matchTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    // Helper methods
    public void startMatch() { this.status = MatchStatus.ONGOING; this.matchTime = 0; }
    public void playMinute() { if(status == MatchStatus.ONGOING && matchTime < 90) matchTime++; }
}
