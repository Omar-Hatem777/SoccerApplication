package org.soccer.interfaces;

import org.soccer.models.League;

import java.util.List;

public interface ILeague {
    League createLeague(League league);
    League updateLeague(Long id, League league);
    void deleteLeague(Long id);
    League getLeagueById(Long id);
    List<League> getAllLeagues();
}
