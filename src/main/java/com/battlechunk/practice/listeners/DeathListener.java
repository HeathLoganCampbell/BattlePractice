package com.battlechunk.practice.listeners;

import com.battlechunk.practice.match.Match;
import com.battlechunk.practice.match.MatchManager;
import com.battlechunk.practice.match.Team;
import com.battlechunk.practice.playerdata.PlayerData;
import com.battlechunk.practice.playerdata.PlayerManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

@AllArgsConstructor
public class DeathListener implements Listener
{
    private PlayerManager playerManager;
    private MatchManager matchManager;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();
            if(e.getFinalDamage() >= player.getHealth())
            {
                //DEAD
                PlayerData playerData = playerManager.getPlayerData(player);
                int matchId = playerData.getMatchId();
                Match match = this.matchManager.getMatch(matchId);
                int teamId = playerData.getTeamId();
                Team team = match.getTeamById(teamId);
                match.setWinner(team);
                match.update();

                player.getWorld().strikeLightningEffect(player.getLocation());
            }
        }
    }
}
