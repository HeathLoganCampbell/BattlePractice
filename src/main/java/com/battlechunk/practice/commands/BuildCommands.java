package com.battlechunk.practice.commands;

import com.battlechunk.practice.PracticeAPI;
import com.battlechunk.practice.commons.CC;
import com.battlechunk.practice.commons.commandframework.Command;
import com.battlechunk.practice.commons.commandframework.CommandArgs;
import com.battlechunk.practice.commons.level.data.Level;
import com.battlechunk.practice.level.PraticeLevelData;
import com.grinderwolf.swm.api.exceptions.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;

public class BuildCommands
{
    @Command(name = "build", inGameOnly = true)
    public void onBuild(CommandArgs args)
    {
        Player player = args.getPlayer();
        String worldName = args.getArgs(0);

        for (World world : Bukkit.getWorlds()) {
            Bukkit.broadcastMessage(world.getName());
            if(world.getName().contains(worldName))
            {
                args.error(worldName + " already is a thing " + world.getName());
                return;
            }
        }

        try {
            Level<PraticeLevelData> worldbuild = PracticeAPI.get().getBuildLevelManager().load(worldName);

            World world = worldbuild.getWorld();
            world.getSpawnLocation().subtract(0, 1, 0).getBlock().setType(Material.BEDROCK);
            player.teleport(world.getSpawnLocation());
            args.message(world.getName() + " has been loaded");
        } catch (IOException | WorldAlreadyExistsException | NewerFormatException | CorruptedWorldException | UnknownWorldException | WorldInUseException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "build.s", inGameOnly = true)
    public void onBuildSave(CommandArgs args)
    {
        Player player = args.getPlayer();

        Bukkit.broadcastMessage("==========");
        for (World world : Bukkit.getWorlds()) {
            Bukkit.broadcastMessage(world.getName());
        }
        Bukkit.broadcastMessage("==========");

        Level<PraticeLevelData> level = PracticeAPI.get().getBuildLevelManager().getLevel(args.getPlayer().getWorld());

        if(level == null)
        {
            args.error("Level not a build level " + args.getPlayer().getWorld().getName());
            return;
        }

        PracticeAPI.get().getBuildLevelManager().save(level);
        args.message(level.getWorld().getName() + " has been saved");
    }

    @Command(name = "build.tp", inGameOnly = true)
    public void onBuildTP(CommandArgs args)
    {
        Player player = args.getPlayer();
        String worldName = args.getArgs(0);

        Bukkit.broadcastMessage("==========");
        for (World world : Bukkit.getWorlds()) {
            Bukkit.broadcastMessage(world.getName());
        }
        Bukkit.broadcastMessage("==========");

        Level<PraticeLevelData> level = PracticeAPI.get().getBuildLevelManager().getLevel(worldName);

        Location spawnLocation = level.getWorld().getSpawnLocation();
        player.teleport(spawnLocation);
        args.message("Now on " + level.getWorld().getName());
    }

    @Command(name = "build.u", inGameOnly = true)
    public void onBuildUnload(CommandArgs args)
    {
        Player player = args.getPlayer();
        Level<PraticeLevelData> level = PracticeAPI.get().getBuildLevelManager().getLevel(args.getPlayer().getWorld());
        if(args.length() == 1)
        {
            String worldName = args.getArgs(0);
            level = PracticeAPI.get().getBuildLevelManager().getLevel(worldName);
        }

        if(level == null)
        {
            args.error("Failure to unload");
            return;
        }

        PracticeAPI.get().getBuildLevelManager().unload(level);
        args.message(level.getLevelData().getWorldName() + " has been unloaded");
    }


    @Command(name = "build.spawn.add", inGameOnly = true)
    public void onSpawnAdd(CommandArgs args)
    {
        Player player = args.getPlayer();
        Level<PraticeLevelData> level = PracticeAPI.get().getBuildLevelManager().getLevel(args.getPlayer().getWorld());

        if(level == null)
        {
            args.error("Failure to load level");
            return;
        }

        level.getLevelData().addSpawn(player.getLocation());

        PracticeAPI.get().getBuildLevelManager().save(level);
        args.message(level.getLevelData().getWorldName() + " Add spawn ( " + level.getLevelData().getSpawn().size() + " Spawns )" );
    }

    @Command(name = "build.spawn.list", inGameOnly = true)
    public void onSpawnList(CommandArgs args)
    {
        Player player = args.getPlayer();
        World world = args.getPlayer().getWorld();
        Level<PraticeLevelData> level = PracticeAPI.get().getBuildLevelManager().getLevel(world);

        if(level == null)
        {
            args.error("Failure to load level");
            return;
        }

        args.message(level.getLevelData().getWorldName() + " ]======-----");
        ArrayList<Location> spawn = level.getLevelData().getSpawn();
        for (int i = 0; i < spawn.size(); i++) {
            Location location = level.getLevelData().getSpawn(i, world);
            TextComponent message = new TextComponent(CC.green  + "#" + i + CC.gray  + " [ Click Me to Teleport ]" );
            message.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to teleport to spawn").create() ) );
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/build spawn tp " + i));
            args.getPlayer().spigot().sendMessage(message);
        }

    }

    @Command(name = "build.spawn.tp", inGameOnly = true)
    public void onSpawnTP(CommandArgs args)
    {
        Player player = args.getPlayer();
        Level<PraticeLevelData> level = PracticeAPI.get().getBuildLevelManager().getLevel(args.getPlayer().getWorld());

        if(level == null)
        {
            args.error("Failure to load level");
            return;
        }

        if(args.length() != 1)
        {
            args.error("Please use /build spawn goto <Id>");
            return;
        }

        String idStr = args.getArgs(0);
        int id = Integer.parseInt(idStr);

        Location location = level.getLevelData().getSpawn().get(id);

        player.teleport(location);
        args.message(level.getLevelData().getWorldName() + " Telported to spawn " + id);
    }
}
