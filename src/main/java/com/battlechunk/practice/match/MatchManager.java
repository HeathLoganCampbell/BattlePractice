package com.battlechunk.practice.match;

import com.battlechunk.practice.commons.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MatchManager
{
    private HashMap<Integer, Match> matches = new HashMap<>();

    public MatchManager(JavaPlugin plugin)
    {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::update, 20L, 20L);
    }

    public void registerMatch(Match match)
    {
        this.matches.put(match.getId(), match);
    }

    public Match getMatch(int id)
    {
        return this.matches.get(id);
    }

    public Set<Map.Entry<Integer, Match>> getAllMatches()
    {
        return this.matches.entrySet();
    }

    public void unregisterMatch(Match match)
    {
        this.unregisterMatch(match.getId());
    }

    public void unregisterMatch(int id)
    {
        this.matches.remove(id);
    }

    public void update()
    {
        for (Map.Entry<Integer, Match> allMatch : this.getAllMatches())
        {
            Match match = allMatch.getValue();
            if(match.getState() == MatchState.REMOVING)
                continue;

            if(match.getState() == MatchState.SPAWN)
                match.spawnAllPlayers();


            int seconds = match.getSeconds() - 1;
            if(match.getState() == MatchState.COUNTDOWN && seconds > -1)
            {
                match.countdown(seconds);
                match.setSeconds(seconds);
            }

            match.update();
        }

        final Iterator<Map.Entry<Integer, Match>> each = this.getAllMatches().iterator();
        while (each.hasNext()) {
            Match match = each.next().getValue();
            if (match.getState() == MatchState.REMOVING) {
                FileUtils.deleteDirectory(match.getLevel().getFolder());
                match.destroy();
                each.remove();
            }
        }

    }
}
