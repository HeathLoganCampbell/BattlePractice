package com.battlechunk.practice.commons.level.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

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
}
