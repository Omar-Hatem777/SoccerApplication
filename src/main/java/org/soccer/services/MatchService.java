package org.soccer.services;

import java.util.List;

import org.soccer.enums.MatchStatus;
import org.soccer.interfaces.IMatch;
import org.soccer.models.Match;
import org.soccer.repositories.MatchRepository;

public class MatchService implements IMatch {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match createMatch(Match match) {
        matchRepository.save(match);
        return match;
    }

    @Override
    public Match updateMatch(Long id, Match match) {
        Match existing = matchRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Match not found");
        }
        existing.setHomeTeam(match.getHomeTeam());
        existing.setAwayTeam(match.getAwayTeam());
        existing.setHomeScore(match.getHomeScore());
        existing.setAwayScore(match.getAwayScore());
        existing.setMatchTime(match.getMatchTime());
        existing.setStatus(match.getStatus());
        existing.setLeague(match.getLeague());
        matchRepository.update(existing);
        return existing;
    }

    @Override
    public void deleteMatch(Long id) {
        Match match = matchRepository.findById(id);
        if (match == null) {
            throw new RuntimeException("Match not found");
        }
        matchRepository.delete(match);
    }

    @Override
    public Match getMatchById(Long id) {
        Match match = matchRepository.findById(id);
        if (match == null) {
            throw new RuntimeException("Match not found");
        }
        return match;
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public List<Match> getMatchesByTeam(Long teamId) {
        return matchRepository.findByTeamId(teamId);
    }

    @Override
    public List<Match> getMatchesByTournament(Long tournamentId) {
        // Assuming tournamentId refers to leagueId in this context
        return matchRepository.findByLeagueId(tournamentId);
    }

    // Additional methods not in interface but useful
    public List<Match> getMatchesByLeague(Long leagueId) {
        return matchRepository.findByLeagueId(leagueId);
    }

    public List<Match> getMatchesByStatus(MatchStatus status) {
        return matchRepository.findByStatus(status);
    }

    public List<Match> getOngoingMatches() {
        return matchRepository.findOngoingMatches();
    }

    public List<Match> getScheduledMatches() {
        return matchRepository.findScheduledMatches();
    }

    public List<Match> getCompletedMatches() {
        return matchRepository.findCompletedMatches();
    }

    public void startMatch(Long matchId) {
        Match match = getMatchById(matchId);
        match.startMatch();
        matchRepository.update(match);
    }

    public void updateMatchScore(Long matchId, int homeScore, int awayScore) {
        Match match = getMatchById(matchId);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        matchRepository.update(match);
    }

    public void finishMatch(Long matchId) {
        Match match = getMatchById(matchId);
        match.setStatus(MatchStatus.FINISHED);
        matchRepository.update(match);
    }
}
