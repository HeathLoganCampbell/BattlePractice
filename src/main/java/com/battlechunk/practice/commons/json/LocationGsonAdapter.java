package com.battlechunk.practice.commons.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LocationGsonAdapter extends TypeAdapter<Location>
{
    private static Type seriType = new TypeToken<Map<String, Object>>(){}.getType();

    private static String WORLD = "world";
    private static String X = "x";
    private static String Y = "y";
    private static String Z = "z";
    private static String YAW = "yaw";
    private static String PITCH = "pitch";

    private static Gson g;

    public LocationGsonAdapter( Gson g )
    {
        this.g = g;
    }

    @Override
    public void write(JsonWriter jsonWriter, Location location) throws IOException {
        if(location == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(getRaw(location));
    }

    @Override
    public Location read(JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return fromRaw(jsonReader.nextString());
    }

    private String getRaw (Location location) {
        Map<String, Object> serial = new HashMap<String, Object>();
        serial.put(WORLD, location.getWorld().getName());
        serial.put(X, Double.toString(location.getX()));
        serial.put(Y, Double.toString(location.getY()));
        serial.put(Z, Double.toString(location.getZ()));
        serial.put(YAW, Float.toString(location.getYaw()));
        serial.put(PITCH, Float.toString(location.getPitch()));
        return g.toJson(serial);
    }

    private Location fromRaw (String raw) {
        Map<String, Object> keys = g.fromJson(raw, seriType);
        World w = Bukkit.getWorld((String) keys.get(WORLD));
        return new Location(w, Double.parseDouble((String) keys.get(X)), Double.parseDouble((String) keys.get(Y)), Double.parseDouble((String) keys.get(Z)),
                Float.parseFloat((String) keys.get(YAW)), Float.parseFloat((String) keys.get(PITCH)));
    }
}