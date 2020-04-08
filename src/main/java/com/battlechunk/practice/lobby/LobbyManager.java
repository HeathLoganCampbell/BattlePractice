package com.battlechunk.practice.lobby;

import com.battlechunk.practice.commons.PlayerUtils;
import com.battlechunk.practice.lobby.listener.SpawnListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class LobbyManager
{
    private Location spawn;

    public LobbyManager(JavaPlugin plugin)
    {
        this.spawn = new Location(Bukkit.getWorlds().get(0), 0, 100, 0);

        Bukkit.getPluginManager().registerEvents(new SpawnListener(this), plugin);
    }

    public void toLobby(Player player)
    {
        PlayerUtils.reset(player);

        player.teleport(player);
    }
}
