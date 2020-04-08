package com.battlechunk.practice.commons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils
{
    public static void reset(Player player)
    {
        if(player == null) return;
        player.closeInventory();
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(13f);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
    }
}
