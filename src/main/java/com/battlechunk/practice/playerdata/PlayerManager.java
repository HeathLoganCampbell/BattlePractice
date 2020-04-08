package com.battlechunk.practice.playerdata;

import com.battlechunk.practice.playerdata.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager
{
    private HashMap<UUID, PlayerData> playerdata = new HashMap<>();

    public PlayerManager(JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), plugin);
    }

    public PlayerData getPlayerData(Player player)
    {
        return playerdata.get(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid)
    {
        return playerdata.get(uuid);
    }

    public void registerPlayerData(PlayerData playerData)
    {
        this.playerdata.put(playerData.getUniqueId(), playerData);
    }

    public void unregisterPlayerData(UUID uuid)
    {
        this.playerdata.remove(uuid);
    }

    public void unregisterPlayerData(Player player)
    {
        this.unregisterPlayerData(player.getUniqueId());
    }

    public void onLogin(String username, UUID unquieId)
    {
        PlayerData playerData = new PlayerData(unquieId);
        this.registerPlayerData(playerData);
    }

    public void onQuit(UUID unquieId)
    {
        this.unregisterPlayerData(unquieId);
    }
}
