package com.battlechunk.practice.commons.level.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class LevelData
{
    private String worldName;
    private String authors;
    private boolean buildMode = true;
    private ArrayList<Location> spawn;
    private HashMap<String, List<Location>> customs;

    public void addSpawn(Location loc)
    {
        if(spawn == null) spawn = new ArrayList<>();
        this.spawn.add(loc);
    }

    public void removeSpawn(Location loc)
    {
        if(this.spawn == null) this.spawn = new ArrayList<>();
        this.spawn.add(loc);
    }

    public Location getSpawn(int id, World world)
    {
        Location location = this.getSpawn().get(id);
        location.setWorld(world);
        return location;
    }
}
