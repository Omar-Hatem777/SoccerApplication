package org.soccer.interfaces;

import org.soccer.models.Match;
import java.util.List;

public interface IMatch {
    Match createMatch(Match match);
    Match updateMatch(Long id, Match match);
    void deleteMatch(Long id);
    Match getMatchById(Long id);
    List<Match> getAllMatches();

    // Helper methods
    List<Match> getMatchesByTeam(Long teamId);
    List<Match> getMatchesByTournament(Long tournamentId);
}
