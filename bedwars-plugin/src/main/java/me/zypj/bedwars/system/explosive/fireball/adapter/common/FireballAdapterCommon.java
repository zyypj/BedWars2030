package me.zypj.bedwars.system.explosive.fireball.adapter.common;

import me.zypj.bedwars.system.explosive.fireball.adapter.FireballAdapter;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class FireballAdapterCommon implements FireballAdapter {

    @Override
    public Fireball spawnFireball(Player shooter,
                                  Location location,
                                  double knockbackHorizontal,
                                  double knockbackVertical,
                                  float explosionPower,
                                  boolean breakBlocks,
                                  boolean makeFire,
                                  double damageSelf,
                                  double damageOthers,
                                  double speedMultiplier) {

        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityLiving nmsShooter = shooter instanceof CraftPlayer
                ? ((CraftPlayer) shooter).getHandle()
                : null;

        Vector dir = shooter.getLocation()
                .getDirection()
                .clone()
                .multiply(speedMultiplier);

        assert nmsShooter != null;
        EntityLargeFireball nmsFb = new EntityLargeFireball(
                nmsWorld,
                nmsShooter,
                dir.getX(),
                dir.getY(),
                dir.getZ()
        );

        nmsFb.yield = (int) explosionPower;
        nmsFb.isIncendiary = makeFire;

        nmsFb.setPosition(location.getX(), location.getY(), location.getZ());

        nmsWorld.addEntity(nmsFb);

        Fireball bukkitFb = (Fireball) nmsFb.getBukkitEntity();
        bukkitFb.setMetadata("fireball-shooter",
                new FixedMetadataValue(
                        Bukkit.getPluginManager().getPlugin("BedWars2030"),
                        shooter.getUniqueId().toString()
                )
        );

        return bukkitFb;
    }
}
