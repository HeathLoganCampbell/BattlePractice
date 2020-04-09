package com.battlechunk.practice.commons.menu;

import com.battlechunk.practice.commons.Callback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Menu implements Listener
{
    private UUID playerId;
    private Inventory inv;
    private HashMap<Integer, Callback<ClickArgs>> clickable;

    private boolean personal = false;

    public Menu(Player player, String title, int size, JavaPlugin plugin, boolean personal)
    {
        this.personal = personal;
        if(personal)
            this.playerId = player.getUniqueId();

        this.inv = Bukkit.createInventory(personal ? player : null, size, title);
        this.clickable = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void onClose(Player player)
    {
        HandlerList.unregisterAll(this);
        this.clickable.clear();
        inv = null;
        playerId = null;
    }

    public void addButton(int slot, ItemStack icon, Callback<ClickArgs> runnable)
    {
        this.inv.setItem(slot, icon);
        this.clickable.put(slot, runnable);
    }

    public void open(Player player)
    {
        player.openInventory(this.inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(e.getInventory() == null) return;
        if(this.inv == null) return;
        if(!e.getInventory().getName().equals(this.inv.getName())) return;
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        Callback<ClickArgs> clickArgsCallback = this.clickable.get(e.getRawSlot());
        if(clickArgsCallback == null) return;
        clickArgsCallback.done(new ClickArgs(player, e.getRawSlot(), e.getCurrentItem()));
    }

    @EventHandler
    public void onCloseInv(InventoryCloseEvent e)
    {
        if(e.getInventory() == null) return;
        if(this.inv == null) return;
        if(!e.getInventory().getName().equals(this.inv.getName())) return;
        if(!personal) return;
        if(playerId != e.getPlayer().getUniqueId()) return;
        this.onClose((Player) e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        if(!personal) return;
        if(playerId != e.getPlayer().getUniqueId()) return;
        this.onClose((Player) e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e)
    {
        if(!personal) return;
        if(playerId != e.getPlayer().getUniqueId()) return;
        this.onClose((Player) e.getPlayer());
    }
}