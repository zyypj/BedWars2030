package me.zypj.bedwars.systems.visual.hologram.adapter;

import me.zypj.bedwars.common.model.Hologram;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface HologramAdapter {
    Hologram create(Location location, List<String> lines);

    void delete(UUID hologramId);

    void updateLines(UUID hologramId, List<String> lines);

    void move(UUID hologramId, Location newLocation);
}
