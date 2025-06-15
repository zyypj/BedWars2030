package me.zypj.bedwars.common.util.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class Cuboid implements Iterable<Block> {

    private final int xMin, xMax;
    private final int yMin, yMax;
    private final int zMin, zMax;
    private final World world;

    public Cuboid(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            throw new IllegalArgumentException("Corner locations cannot be null");
        }
        if (!location1.getWorld().equals(location2.getWorld())) {
            throw new IllegalArgumentException("Corners must be in the same world");
        }
        this.world = location1.getWorld();
        this.xMin = Math.min(location1.getBlockX(), location2.getBlockX());
        this.xMax = Math.max(location1.getBlockX(), location2.getBlockX());
        this.yMin = Math.min(location1.getBlockY(), location2.getBlockY());
        this.yMax = Math.max(location1.getBlockY(), location2.getBlockY());
        this.zMin = Math.min(location1.getBlockZ(), location2.getBlockZ());
        this.zMax = Math.max(location1.getBlockZ(), location2.getBlockZ());
    }

    @Override
    public @NotNull Iterator<Block> iterator() {
        return new BlockIterator();
    }

    public Location getCenter() {
        double centerX = xMin + (xMax - xMin) / 2.0;
        double centerY = yMin + (yMax - yMin) / 2.0;
        double centerZ = zMin + (zMax - zMin) / 2.0;
        return new Location(world, centerX, centerY, centerZ);
    }

    public Location getCorner1() {
        return new Location(world, xMin, yMin, zMin);
    }

    public Location getCorner2() {
        return new Location(world, xMax, yMax, zMax);
    }

    public double distance() {
        return getCorner1().distance(getCorner2());
    }

    public double distanceSquared() {
        return getCorner1().distanceSquared(getCorner2());
    }

    public int getHeight() {
        return yMax - yMin + 1;
    }

    public int getWidthX() {
        return xMax - xMin + 1;
    }

    public int getWidthZ() {
        return zMax - zMin + 1;
    }

    public int getVolume() {
        return getWidthX() * getHeight() * getWidthZ();
    }

    public boolean contains(Location loc) {
        if (loc == null || !loc.getWorld().equals(world)) {
            return false;
        }
        int bx = loc.getBlockX(), by = loc.getBlockY(), bz = loc.getBlockZ();
        return bx >= xMin && bx <= xMax
                && by >= yMin && by <= yMax
                && bz >= zMin && bz <= zMax;
    }

    public boolean contains(Player player) {
        return contains(player.getLocation());
    }

    public boolean contains(Location loc, double margin) {
        if (loc == null || !loc.getWorld().equals(world)) {
            return false;
        }
        double x = loc.getX(), y = loc.getY(), z = loc.getZ();
        double minX = xMin - margin, maxX = xMax + margin;
        double minY = yMin - margin, maxY = yMax + margin;
        double minZ = zMin - margin, maxZ = zMax + margin;
        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }

    public Location getRandomLocation() {
        int rx = ThreadLocalRandom.current().nextInt(xMin, xMax + 1);
        int ry = ThreadLocalRandom.current().nextInt(yMin, yMax + 1);
        int rz = ThreadLocalRandom.current().nextInt(zMin, zMax + 1);
        return new Location(world, rx, ry, rz);
    }

    private class BlockIterator implements Iterator<Block> {
        private int x = xMin, y = yMin, z = zMin;

        @Override
        public boolean hasNext() {
            return x <= xMax && y <= yMax && z <= zMax;
        }

        @Override
        public Block next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Block block = world.getBlockAt(x, y, z);
            if (++z > zMax) {
                z = zMin;
                if (++y > yMax) {
                    y = yMin;
                    x++;
                }
            }
            return block;
        }
    }
}
