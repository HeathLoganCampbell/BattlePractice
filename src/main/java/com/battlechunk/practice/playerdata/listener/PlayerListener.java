package com.battlechunk.practice.playerdata.listener;

import com.battlechunk.practice.playerdata.PlayerManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

@AllArgsConstructor
public class PlayerListener implements Listener
{
    private PlayerManager playerManager;

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e)
    {
        this.playerManager.onLogin(e.getName(), e.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        UUID uniqueId = player.getUniqueId();
        this.playerManager.onQuit(uniqueId);
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        Player player = e.getPlayer();
        UUID uniqueId = player.getUniqueId();
        this.playerManager.onQuit(uniqueId);
    }
}
