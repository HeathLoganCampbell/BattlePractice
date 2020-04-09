package com.battlechunk.practice.commons.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class ClickArgs
{
    private Player player;
    private int slot;
    private ItemStack clickedItem;
}