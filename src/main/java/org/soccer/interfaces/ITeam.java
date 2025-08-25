package org.soccer.interfaces;

import org.soccer.models.Team;
import java.util.List;

public interface ITeam {
    Team createTeam(Team team);
    Team updateTeam(Long id, Team team);
    void deleteTeam(Long id);
    Team getTeamById(Long id);
    List<Team> getAllTeams();
}
