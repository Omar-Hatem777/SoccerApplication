package org.soccer.interfaces;

import org.soccer.models.Player;

import java.util.List;

public interface IPlayer {
    Player createPlayer(Player player);
    Player updatePlayer(Long id, Player player);
    void deletePlayer(Long id);
    Player getPlayerById(Long id);
    List<Player> getAllPlayers();
}
