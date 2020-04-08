package com.battlechunk.practice.commands;

import com.battlechunk.practice.PracticeAPI;
import com.battlechunk.practice.commons.commandframework.Command;
import com.battlechunk.practice.commons.commandframework.CommandArgs;
import com.battlechunk.practice.match.matchtypes.BuildUHC;
import com.grinderwolf.swm.api.exceptions.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class MatchCommands
{
    // /Match <Username>
    @Command(name = "Match", inGameOnly = true)
    public void onCommand(CommandArgs args)
    {
        String targetPlayerName = args.getArgs(0);
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if(targetPlayer == null)
        {
            args.error("Player not online");
            return;
        }

        try {
            PracticeAPI.get().createMatch(Arrays.asList(targetPlayer), Arrays.asList(args.getPlayer()), BuildUHC.class);
        } catch (IllegalAccessException | InstantiationException | WorldAlreadyExistsException |
                IOException | NewerFormatException | CorruptedWorldException | UnknownWorldException |
                WorldInUseException | NoSuchMethodException | InvocationTargetException e) {
            args.error("Something went very wrong while setting up match...");
            e.printStackTrace();
        }
    }
}
