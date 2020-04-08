package com.battlechunk.practice;

import com.battlechunk.practice.commands.BuildCommands;
import com.battlechunk.practice.commands.MatchCommands;
import com.battlechunk.practice.commons.commandframework.CommandFramework;
import com.battlechunk.practice.commons.level.LevelManager;
import com.battlechunk.practice.commons.level.data.Level;
import com.battlechunk.practice.level.PraticeLevelData;
import com.battlechunk.practice.listeners.DamageListener;
import com.battlechunk.practice.listeners.DeathListener;
import com.battlechunk.practice.lobby.LobbyManager;
import com.battlechunk.practice.match.Match;
import com.battlechunk.practice.match.MatchManager;
import com.battlechunk.practice.match.Team;
import com.battlechunk.practice.playerdata.PlayerManager;
import com.grinderwolf.swm.api.exceptions.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class PracticeAPI
{
    private static AtomicInteger MATCH_ID = new AtomicInteger(1);
    private static PracticeAPI practiceAPI;

    private MatchManager matchManager;
    private PlayerManager playerManager;
    private LobbyManager lobbyManager;
    private LevelManager<PraticeLevelData> levelManager;
    private LevelManager<PraticeLevelData> buildLevelManager;

    private CommandFramework commandFramework;

    public PracticeAPI(JavaPlugin plugin)
    {
        this.playerManager = new PlayerManager(plugin);
        this.matchManager = new MatchManager(plugin);
        this.lobbyManager = new LobbyManager(plugin);
        this.levelManager = new LevelManager<PraticeLevelData>(plugin, "Practice", PraticeLevelData.class, new File("practice" + File.separator + "active"), false);
        this.buildLevelManager =  new LevelManager<PraticeLevelData>(plugin, "PracticeBuild", PraticeLevelData.class, new File("practice" + File.separator + "build"), true);

        this.commandFramework = new CommandFramework(plugin);
        this.commandFramework.registerCommands(new MatchCommands());
        this.commandFramework.registerCommands(new BuildCommands());

        Bukkit.getPluginManager().registerEvents(new DamageListener(this.playerManager, this.matchManager), plugin);
        Bukkit.getPluginManager().registerEvents(new DeathListener(this.playerManager, this.matchManager), plugin);


        practiceAPI = this;
    }


    public void createMatch(List<Player> teamOnePlayers, List<Player> teamTwoPlayers, Class<? extends Match> matchClazz) throws IllegalAccessException,
            InstantiationException, WorldAlreadyExistsException, IOException, NewerFormatException, CorruptedWorldException,
            UnknownWorldException, WorldInUseException, NoSuchMethodException, InvocationTargetException {
        //Load Map hee
        final int finalId = MATCH_ID.getAndAdd(1);

        Level<PraticeLevelData> level = this.levelManager.load("example "  + finalId);

        Match match = matchClazz.getConstructor(int.class).newInstance(finalId);
        Team teamOne = match.getTeamOne();
        Team teamTwo = match.getTeamTwo();

        match.setLevel(level);

        for (Player teamPlayer : teamOnePlayers) {
            match.addPlayer(teamOne, teamPlayer);
        }

        for (Player teamPlayer : teamTwoPlayers) {
            match.addPlayer(teamTwo, teamPlayer);
        }

        this.matchManager.registerMatch(match);
    }

    public static PracticeAPI get()
    {
        return practiceAPI;
    }
}
