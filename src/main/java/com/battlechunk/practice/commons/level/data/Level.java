package com.battlechunk.practice.commons.level.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.World;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Level<T extends LevelData>
{
    private World world;
    private T levelData;
    private File folder;
}
