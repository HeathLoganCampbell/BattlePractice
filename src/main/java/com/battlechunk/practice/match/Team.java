package com.battlechunk.practice.match;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

@Getter
public class Team
{
    private int id;
    private String name = "Yeet";
    private HashSet<UUID> players = new HashSet<>();

    public Team(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public void broadcast(String message)
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(hasPlayer(onlinePlayer))
                onlinePlayer.sendMessage(message);
        }
    }

    public boolean hasPlayer(Player player)
    {
        return this.players.contains(player.getUniqueId());
    }

    public void addPlayer(Player player)
    {
        this.players.add(player.getUniqueId());
    }

    public void removePlayer(Player player)
    {
        this.players.remove(player.getUniqueId());
    }
}
