package com.battlechunk.practice.commands;

import com.battlechunk.practice.PracticeAPI;
import com.battlechunk.practice.commons.commandframework.Command;
import com.battlechunk.practice.commons.commandframework.CommandArgs;
import com.battlechunk.practice.commons.level.data.Level;
import com.battlechunk.practice.level.PraticeLevelData;
import com.grinderwolf.swm.api.exceptions.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

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
}