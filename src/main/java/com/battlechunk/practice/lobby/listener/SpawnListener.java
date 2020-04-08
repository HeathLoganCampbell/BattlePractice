package com.battlechunk.practice.lobby.listener;

import com.battlechunk.practice.commons.PlayerUtils;
import com.battlechunk.practice.lobby.LobbyManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

@AllArgsConstructor
public class SpawnListener implements Listener
{
    private LobbyManager lobbyManager;

    @EventHandler
    public void onJoinSpawn(PlayerSpawnLocationEvent e)
    {
        Player player = e.getPlayer();
        PlayerUtils.reset(player);
        e.setSpawnLocation(lobbyManager.getSpawn());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        Player player = e.getPlayer();
        PlayerUtils.reset(player);
        e.setRespawnLocation(lobbyManager.getSpawn());
    }
}
