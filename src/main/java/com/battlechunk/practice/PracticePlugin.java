package com.battlechunk.practice;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Spawn the players in an match together
 */
public class PracticePlugin extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        new PracticeAPI(this);
    }

    @Override
    public void onDisable()
    {

    }
}
