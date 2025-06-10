package me.zypj.bedwars.system.visual.hologram.factory;

import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.model.Hologram;
import me.zypj.bedwars.system.visual.hologram.service.HologramService;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class HologramFactory {

    private final HologramService service;
    private Location location;
    private final List<String> lines = new ArrayList<>();

    public HologramFactory location(Location location) {
        this.location = location;
        return this;
    }

    public HologramFactory line(String line) {
        this.lines.add(line);
        return this;
    }

    public HologramFactory lines(List<String> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
        return this;
    }

    public Hologram create() {
        if (location == null) {
            throw new IllegalStateException("Location must be set before create()");
        }
        return service.createHologramInternal(location, lines);
    }
}
