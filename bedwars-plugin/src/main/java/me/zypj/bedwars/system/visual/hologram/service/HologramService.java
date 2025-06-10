package me.zypj.bedwars.system.visual.hologram.service;

import lombok.Getter;
import me.zypj.bedwars.system.visual.hologram.factory.HologramFactory;
import me.zypj.bedwars.system.visual.hologram.adapter.HologramAdapter;
import me.zypj.bedwars.system.visual.hologram.adapter.provider.HologramAdapterProvider;
import me.zypj.bedwars.common.model.Hologram;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class HologramService {

    private final HologramAdapter adapter;
    private final Map<UUID, Hologram> cache = new HashMap<>();

    public HologramService() {
        this.adapter = HologramAdapterProvider.getAdapter();
    }

    public HologramFactory factory() {
        return new HologramFactory(this);
    }

    public Hologram createHologramInternal(Location loc, List<String> lines) {
        Hologram holo = adapter.create(loc, lines);
        cache.put(holo.getId(), holo);
        return holo;
    }

    public void removeHologram(UUID id) {
        adapter.delete(id);
        cache.remove(id);
    }

    public void updateHologramLines(UUID id, List<String> lines) {
        adapter.updateLines(id, lines);
    }

    public void moveHologram(UUID id, Location newLoc) {
        adapter.move(id, newLoc);
    }
}
