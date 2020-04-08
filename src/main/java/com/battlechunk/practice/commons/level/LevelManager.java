package com.battlechunk.practice.commons.level;

import com.battlechunk.practice.commons.json.GsonUtil;
import com.battlechunk.practice.commons.level.data.Level;
import com.battlechunk.practice.commons.level.data.LevelData;
import com.battlechunk.practice.commons.level.loader.FileLoader;
import com.google.gson.stream.JsonReader;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Load world
 * Save world
 * Have custom data with world
 * Each world can have a different folder
 */
public class LevelManager<T extends LevelData>
{
    private JavaPlugin plugin;
    private String unqiueId;
    private Class<T> levelDataClazz;

    private File activeWorlds;
    private HashMap<String, Level<T>> levels;
    private boolean saveToMap = true;

    public LevelManager(JavaPlugin plugin, String unqiueId, Class<T> levelDataClazz, File activeWorlds, boolean saveToMap)
    {
        this.plugin = plugin;
        this.unqiueId = unqiueId;
        this.levelDataClazz = levelDataClazz;

        this.activeWorlds = activeWorlds;
        this.saveToMap = saveToMap;

        if(saveToMap)
            this.levels = new HashMap<>();

        SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        slimePlugin.registerLoader(unqiueId, new FileLoader(this.activeWorlds));
    }

    /**
     * Loads a world from a directory, if no world is found there
     * we will create a new one
     *
     * @return
     */
    public Level<T> load(String worldName) throws IOException, WorldAlreadyExistsException, NewerFormatException, CorruptedWorldException, WorldInUseException, UnknownWorldException, IllegalAccessException, InstantiationException {
        SlimePlugin slimeWorldManager = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader loader = slimeWorldManager.getLoader(this.unqiueId);
        SlimeWorld slimeWorld = null;

        SlimePropertyMap properties = new SlimePropertyMap();

        properties.setString(SlimeProperties.DIFFICULTY, "normal");
        properties.setInt(SlimeProperties.SPAWN_X, 0);
        properties.setInt(SlimeProperties.SPAWN_Y, 100);
        properties.setInt(SlimeProperties.SPAWN_Z, 0);
        File baseFile = new File(this.activeWorlds, worldName);
        File file = baseFile;
        if(!file.exists())
        {
            while(!file.mkdirs());
            file = new File(file, worldName);
            slimeWorld = slimeWorldManager.createEmptyWorld(loader,worldName + File.separator + worldName, false, properties);
        }
        else
        {
            slimeWorld = slimeWorldManager.loadWorld(loader, worldName + File.separator + worldName, false, properties);
        }

        slimeWorldManager.generateWorld(slimeWorld);
        World world = Bukkit.getWorld(worldName + File.separator + worldName);

        File levelDataFile = new File(new File(this.activeWorlds, worldName), "level-data.json");
        T loadLevelData = null;
        if(levelDataFile.exists())
        {
            loadLevelData = this.loadLevelData(levelDataFile, this.levelDataClazz);
            loadLevelData.setWorldName(worldName);
        }
        else
        {
            loadLevelData = this.levelDataClazz.newInstance();
            this.saveLevelData(levelDataFile, loadLevelData);
        }

        Level level = new Level<T>(world, loadLevelData, baseFile);

        if(saveToMap)
            this.levels.put(worldName.toLowerCase(), level);

        return level;
    }

    public File getLevelFolder(String worldName)
    {
        return new File(this.activeWorlds, worldName);
    }

    public void save(String worldName)
    {
        Level level = this.getLevel(worldName);
        if(level == null) return;
        save(level);
    }

    public void save(Level level)
    {
        level.getWorld().save();
    }

    public void unload(String worldName)
    {
        Level level = this.getLevel(worldName);
        if(level == null) return;
        this.unload(level);
    }

    public void unload(Level level)
    {
        String name = level.getLevelData().getWorldName();
        World world = level.getWorld();
        for (Player player : world.getPlayers()) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }
        Bukkit.unloadWorld(world, true);
        if(saveToMap)
            this.levels.remove(name);
    }

    public Level<T> getLevel(String worldName)
    {
        return this.levels.get(worldName.toLowerCase());
    }

    public Level<T> getLevel(World world)
    {
        String levelName = world.getName().split(Pattern.quote(File.separator))[0];
        return this.levels.get(levelName);
    }

    private T loadLevelData(File file, Class<T> levelDataClazz)
    {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
            return GsonUtil.getPrettyGson().fromJson(reader, levelDataClazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveLevelData(File file, T levelData)
    {
        String json = GsonUtil.getPrettyGson().toJson(levelData);

        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(json);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
