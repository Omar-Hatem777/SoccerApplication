package org.soccer.models;

import jakarta.persistence.*;
import org.soccer.enums.PlayerPosition;

@Entity
@Table(name = "players")
public class Player {

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)  // Correct way to mark non-null
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlayerPosition position; // Goalkeeper, Defender, Midfielder, Forward

    @Column(nullable = false)
    private int shirtNumber;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private int goalsScored = 0; // default value

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // Many-to-One relationship with Team


    // Constructors
    public Player() {}

    public Player(String name, PlayerPosition position, int shirtNumber, int age) {
        this.name = name;
        this.position = position;
        this.shirtNumber = shirtNumber;
        this.age = age;
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

    public PlayerPosition getPosition() {
        return position;
    }
    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }
    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public int getGoalsScored() {
        return goalsScored;
    }
    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }


    //Helper methods

    public void scoreGoal() {
        this.goalsScored++;
    }

    public void resetGoals() {
        this.goalsScored = 0;
    }


}
