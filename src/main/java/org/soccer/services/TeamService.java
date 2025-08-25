package org.soccer.services;

import org.soccer.interfaces.ITeam;
import org.soccer.models.Team;
import org.soccer.repositories.TeamRepository;

import java.util.List;

public class TeamService implements ITeam {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team createTeam(Team team) {
        teamRepository.save(team);
        return team;
    }

    @Override
    public Team updateTeam(Long id, Team team) {
        Team existing = teamRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Team not found");
        }
        existing.setName(team.getName());
        existing.setCoachName(team.getCoachName());
        existing.setTotalPoints(team.getTotalPoints());
        existing.setScore(team.getScore());
        existing.setLeague(team.getLeague());
        teamRepository.update(existing);
        return existing;
    }

    @Override
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id);
        if (team == null) {
            throw new RuntimeException("Team not found");
        }
        teamRepository.delete(team);
    }

    @Override
    public Team getTeamById(Long id) {
        Team team = teamRepository.findById(id);
        if (team == null) {
            throw new RuntimeException("Team not found");
        }
        return team;
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Additional methods not in interface but useful
    public List<Team> getTeamsByLeague(Long leagueId) {
        return teamRepository.findByLeagueId(leagueId);
    }

    public List<Team> getTeamsByName(String name) {
        return teamRepository.findByName(name);
    }

    public List<Team> getTeamsByCoach(String coachName) {
        return teamRepository.findByCoachName(coachName);
    }

    public void addPointsToTeam(Long teamId, int points) {
        Team team = getTeamById(teamId);
        team.setTotalPoints(team.getTotalPoints() + points);
        teamRepository.update(team);
    }

    public void resetTeamScore(Long teamId) {
        Team team = getTeamById(teamId);
        team.resetScore();
        teamRepository.update(team);
    }
}
