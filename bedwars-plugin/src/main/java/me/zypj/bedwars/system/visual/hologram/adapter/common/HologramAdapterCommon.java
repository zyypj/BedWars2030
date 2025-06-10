package me.zypj.bedwars.system.visual.hologram.adapter.common;

import me.zypj.bedwars.system.visual.hologram.adapter.HologramAdapter;
import me.zypj.bedwars.common.model.Hologram;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import java.util.*;

public class HologramAdapterCommon implements HologramAdapter {
    private static final double LINE_HEIGHT = 0.25D;
    private final Map<UUID, List<ArmorStand>> hologramStands = new HashMap<>();
    private final Map<UUID, Location> baseLocations = new HashMap<>();

    @Override
    public Hologram create(Location location, List<String> lines) {
        UUID id = UUID.randomUUID();
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location world cannot be null");
        }

        List<ArmorStand> stands = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            Location spawnLoc = location.clone().add(0, -i * LINE_HEIGHT, 0);
            ArmorStand stand = world.spawn(spawnLoc, ArmorStand.class);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setCustomName(lines.get(i));
            stand.setCustomNameVisible(true);
            stand.setSmall(true);
            stand.setBasePlate(false);
            stand.setArms(false);
            stands.add(stand);
        }

        hologramStands.put(id, stands);
        baseLocations.put(id, location.clone());
        return new Hologram(id, location, lines);
    }

    @Override
    public void delete(UUID id) {
        List<ArmorStand> stands = hologramStands.remove(id);
        baseLocations.remove(id);
        if (stands != null) {
            for (ArmorStand stand : stands) {
                stand.remove();
            }
        }
    }

    @Override
    public void updateLines(UUID id, List<String> lines) {
        Location base = requireBaseLocation(id);
        World world = base.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Base location world cannot be null");
        }

        List<ArmorStand> stands = hologramStands.get(id);
        if (stands == null) {
            throw new IllegalArgumentException("Hologram id not found: " + id);
        }

        int oldSize = stands.size();
        int newSize = lines.size();
        int minSize = Math.min(oldSize, newSize);

        for (int i = 0; i < minSize; i++) {
            ArmorStand stand = stands.get(i);
            stand.teleport(getLineLocation(base, i));
            stand.setCustomName(lines.get(i));
        }

        if (oldSize > newSize) {
            for (int i = newSize; i < oldSize; i++) {
                stands.get(i).remove();
            }
            stands.subList(newSize, oldSize).clear();
        }

        else if (newSize > oldSize) {
            for (int i = oldSize; i < newSize; i++) {
                ArmorStand stand = spawnStand(world, getLineLocation(base, i), lines.get(i));
                stands.add(stand);
            }
        }
    }

    @Override
    public void move(UUID id, Location newLoc) {
        List<ArmorStand> stands = hologramStands.get(id);
        if (stands == null) {
            throw new IllegalArgumentException("Hologram id not found: " + id);
        }

        for (int i = 0; i < stands.size(); i++) {
            ArmorStand stand = stands.get(i);
            stand.teleport(newLoc.clone().add(0, -i * LINE_HEIGHT, 0));
        }
        baseLocations.put(id, newLoc.clone());
    }

    private Location getLineLocation(Location base, int index) {
        return base.clone().add(0, -index * LINE_HEIGHT, 0);
    }

    private ArmorStand spawnStand(World world, Location loc, String text) {
        ArmorStand stand = world.spawn(loc, ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setSmall(true);
        stand.setBasePlate(false);
        stand.setArms(false);
        return stand;
    }

    private Location requireBaseLocation(UUID id) {
        Location base = baseLocations.get(id);
        if (base == null) {
            throw new IllegalArgumentException("Hologram id not found: " + id);
        }
        return base;
    }
}
