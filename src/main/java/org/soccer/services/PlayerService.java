package org.soccer.services;

import java.util.List;

import org.soccer.interfaces.IPlayer;
import org.soccer.models.Player;
import org.soccer.repositories.PlayerRepository;

public class PlayerService implements IPlayer {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player createPlayer(Player player) {
        playerRepository.save(player);
        return player;
    }

    @Override
    public Player updatePlayer(Long id, Player player) {
        Player existing = playerRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Player not found");
        }
        existing.setName(player.getName());
        existing.setPosition(player.getPosition());
        existing.setShirtNumber(player.getShirtNumber());
        existing.setAge(player.getAge());
        existing.setGoalsScored(player.getGoalsScored());
        existing.setTeam(player.getTeam());
        playerRepository.update(existing);
        return existing;
    }

    @Override
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id);
        if (player == null) {
            throw new RuntimeException("Player not found");
        }
        playerRepository.delete(player);
    }

    @Override
    public Player getPlayerById(Long id) {
        Player player = playerRepository.findById(id);
        if (player == null) {
            throw new RuntimeException("Player not found");
        }
        return player;
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // Additional methods not in interface but useful
    public List<Player> getPlayersByTeam(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    public List<Player> getPlayersByPosition(String position) {
        return playerRepository.findByPosition(position);
    }
}
