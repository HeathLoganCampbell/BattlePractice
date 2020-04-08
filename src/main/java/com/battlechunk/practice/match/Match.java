package com.battlechunk.practice.match;


import com.battlechunk.practice.PracticeAPI;
import com.battlechunk.practice.PracticePlugin;
import com.battlechunk.practice.commons.PlayerUtils;
import com.battlechunk.practice.playerdata.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * What makes up a Match
 * 1. 2 Teams
 * 2. A Map
 * 3. A check for who wins
 *
 * 1. Load Map
 * 2. onSpawn - Spawns players in to world
 * 3. onEquipt - Gives players kits
 * 4. onCountdown - counts down 5, 4, 3, 2 1.. fight
 * 5. onStart - called when onCount == 0
 *
 * 6. isDone
 * 7. onEnd
 * 8. onChampion
 */
@Getter
@Setter
public class Match
{

    private int id = 0;
    private String displayName;
    //A team can contain 1 to many players
    private Team teamOne;
    private Team teamTwo;

    private Team winnerTeam;
    private Team loserTeam;

    private boolean done = false;
    private MatchState state = MatchState.SPAWN;

    private int seconds = 6;
    // START Match Options
        // Team Options
    public boolean friendlyfire = false;

    public boolean blockPlace = false;
    public boolean blockBreak = false;
    //Can you break blocks not place by players?
    public boolean mapDestruction = false;
    // END Match Options

    public Match(String displayName)
    {
        this.displayName = displayName;

        this.teamOne = new Team("One" , 1);
        this.teamTwo = new Team("Two" , 2);
    }


    public Integer getId()
    {
        return id;
    }

    public Team getTeamById(int id)
    {
        if(id == 1) return this.teamOne;
        if(id == 2) return this.teamTwo;
        return null;
    }

    public void addPlayer(Team team, Player player)
    {
        team.addPlayer(player);
        PlayerData playerData = PracticeAPI.get().getPlayerManager().getPlayerData(player);
        if(playerData == null)
        {
            System.out.println("ERROR playerdata was null in Pratice Match:addPlayer");
            return;
        }

        playerData.setMatchId(this.getId());
        playerData.setTeamId(team.getId());
    }

    public void removePlayer(Player player)
    {
        PlayerData playerData = PracticeAPI.get().getPlayerManager().getPlayerData(player);
        if(playerData == null)
        {
            System.out.println("ERROR playerdata was null in Pratice Match:removePlayer");
            return;
        }

        int matchId = playerData.getMatchId();
        if(matchId == this.getId())
        {
            System.out.println(player.getName() + " Player is not in " + this.getId() + " they are in " + matchId);
            return;
        }

        int teamId = playerData.getTeamId();
        Team curTeam = this.getTeamById(teamId);
        if(curTeam == null)
        {
            System.out.println(player.getName() + " Player Not in team");
            return;
        }

        curTeam.removePlayer(player);
        playerData.setTeamId(PlayerData.NO_TEAM);
        playerData.setMatchId(PlayerData.NO_MATCH);
    }

    public void setWinner(Team winnerTeam)
    {
        if(winnerTeam != teamOne)
        {
            this.winnerTeam = teamTwo;
            this.loserTeam = teamOne;
        }
        else if(winnerTeam != teamTwo)
        {
            this.winnerTeam = teamOne;
            this.loserTeam = teamTwo;
        }
        else
        {
            //Draw?? tf? how?
        }
        this.done = true;
    }

    public void spawnAllPlayers()
    {
        this.forPlayers(this::spawn);
        this.setState(MatchState.COUNTDOWN);
    }

    public void spawn(Player player)
    {
        onSpawn(player);
        onEquipt(player);
    }

    public void countdown(int seconds)
    {
        this.onCountdown(seconds);
        if(seconds == 0)
            this.start();
    }

    public void update()
    {
        if(this.isDone() && this.isLive())
            this.end();
    }

    public void start()
    {
        this.setState(MatchState.LIVE);

        this.onStart();
    }

    public void end()
    {
        this.onEnd();
        this.setState(MatchState.CHAMPION);
        this.champion();
    }

    public void forPlayers(Consumer<Player> playeConsumer)
    {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(this.teamOne.hasPlayer(onlinePlayer) || this.teamTwo.hasPlayer(onlinePlayer))
            {
                playeConsumer.accept(onlinePlayer);
            }
        }
    }

    public void champion()
    {
        this.onChampion();

        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(PracticePlugin.class), () -> {
            //Teleport players back to lobby
            this.forPlayers((player) -> PracticeAPI.get().getLobbyManager().toLobby(player));

            this.setState(MatchState.REMOVING);
            this.destroy();
        }, 20L * 5L);
    }

    public void broadcast(String message)
    {
        this.teamOne.broadcast(message);
        this.teamTwo.broadcast(message);
    }

    public boolean isLive()
    {
        return this.state == MatchState.LIVE;
    }

    public void setState(MatchState state)
    {
        this.state = state;
    }

    protected void onSpawn(Player player)
    {
        PlayerUtils.reset(player);
    }

    protected void onEquipt(Player player)
    {
        player.getInventory().setItem(0, new ItemStack(Material.STONE_SWORD));
    }

    protected void onCountdown(int seconds)
    {
        if(seconds >= 0)
            this.broadcast(seconds > 0 ?
                                seconds + "..." :
                                "Fight!");
    }

    protected void onStart()
    {

    }

    protected boolean isDone()
    {
        return done;
    }

    protected void onEnd()
    {
        this.setState(MatchState.CHAMPION);
    }

    public void onChampion()
    {
        if(this.winnerTeam == null)
        {
            this.broadcast("No one Won");
        }
        else
        {
            this.broadcast(this.winnerTeam.getName());
        }
    }

    public void destroy()
    {
        this.teamOne = null;
        this.teamTwo = null;

        this.winnerTeam = null;
        this.loserTeam = null;
    }
}
