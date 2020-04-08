package com.battlechunk.practice.listeners;

import com.battlechunk.practice.match.Match;
import com.battlechunk.practice.match.MatchManager;
import com.battlechunk.practice.playerdata.PlayerData;
import com.battlechunk.practice.playerdata.PlayerManager;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@AllArgsConstructor
public class DamageListener implements Listener
{
    private PlayerManager playerManager;
    private MatchManager matchManager;

    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            Player player = (Player) e.getEntity();
            PlayerData playerData = playerManager.getPlayerData(player);
            if(playerData.getMatchId() == -1)
            {
                //Not in a match
                e.setCancelled(true);
                return;
            }

            Match match = matchManager.getMatch(playerData.getMatchId());
            if(match == null)
            {
                e.setCancelled(true);
                System.out.println("Could not find Match " + match.getId() + " for " + player.getName() + " in DamageListener:onDamage");
                return;
            }

            if(!match.isLive())
            {
                //Match hasn't started
                e.setCancelled(true);
                return;
            }


        }
    }
}
