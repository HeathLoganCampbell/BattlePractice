package com.battlechunk.practice;

import com.battlechunk.practice.commands.MatchCommands;
import com.battlechunk.practice.commons.commandframework.CommandFramework;
import com.battlechunk.practice.lobby.LobbyManager;
import com.battlechunk.practice.match.Match;
import com.battlechunk.practice.match.MatchManager;
import com.battlechunk.practice.match.Team;
import com.battlechunk.practice.playerdata.PlayerManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class PracticeAPI
{
    private static PracticeAPI practiceAPI;

    private MatchManager matchManager;
    private PlayerManager playerManager;
    private LobbyManager lobbyManager;
    private CommandFramework commandFramework;

    public PracticeAPI(JavaPlugin plugin)
    {
        this.playerManager = new PlayerManager(plugin);
        this.matchManager = new MatchManager(plugin);
        this.lobbyManager = new LobbyManager(plugin);

        this.commandFramework = new CommandFramework(plugin);
        this.commandFramework.registerCommands(new MatchCommands());

        practiceAPI = this;
    }

    public void createMatch(List<Player> teamOnePlayers, List<Player> teamTwoPlayers, Class<? extends Match> matchClazz) throws IllegalAccessException, InstantiationException {
        //Load Map hee


        Match match = matchClazz.newInstance();
        Team teamOne = match.getTeamOne();
        Team teamTwo = match.getTeamTwo();

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

    //Map map = PracticeAPI.getMap("Flat Flight");
    //Match match = map.launchMatch(UHCBuildMatch.class);
        //Generates world
        //Teleports players to spawns
        //Start fight
    //match.addMatchEndHook((result) -> { result.getPlayers().foreach(player ->  player.sendMessage("Game Over")); });
}
