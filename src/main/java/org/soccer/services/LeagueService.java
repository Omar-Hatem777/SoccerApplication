package org.soccer.services;

import org.soccer.interfaces.ILeague;
import org.soccer.models.League;
import org.soccer.repositories.LeagueRepository;

import java.util.List;

public class LeagueService implements ILeague {
    private final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    @Override
    public League createLeague(League league) {
        leagueRepository.save(league);
        return league;
    }

    @Override
    public League updateLeague(Long id, League league) {
        League existing = leagueRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("League not found");
        }
        existing.setName(league.getName());
        leagueRepository.update(existing);
        return existing;
    }

    @Override
    public void deleteLeague(Long id) {
        League league = leagueRepository.findById(id);
        if (league == null) {
            throw new RuntimeException("League not found");
        }
        leagueRepository.delete(league);
    }

    @Override
    public League getLeagueById(Long id) {
        League league = leagueRepository.findById(id);
        if (league == null) {
            throw new RuntimeException("League not found");
        }
        return league;
    }

    @Override
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    // Additional methods not in interface but useful
    public List<League> getLeaguesByName(String name) {
        return leagueRepository.findByName(name);
    }
}
