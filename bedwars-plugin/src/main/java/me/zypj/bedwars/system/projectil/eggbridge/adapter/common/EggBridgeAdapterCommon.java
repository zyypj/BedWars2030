package me.zypj.bedwars.system.projectil.eggbridge.adapter.common;

import me.zypj.bedwars.system.projectil.eggbridge.adapter.EggBridgeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class EggBridgeAdapterCommon implements EggBridgeAdapter {
    @Override
    public Egg spawnEgg(Player shooter,
                        Location location,
                        double speedMultiplier,
                        boolean gravityEnabled) {

        Egg egg = shooter.getWorld().spawn(location, Egg.class);

        egg.setBounce(false);
        egg.setMetadata("egg-bridge-shooter",
                new FixedMetadataValue(
                        Bukkit.getPluginManager().getPlugin("BedWars2030"),
                        shooter.getUniqueId().toString()
                )
        );

        Vector vel = shooter.getLocation()
                .getDirection()
                .clone()
                .multiply(speedMultiplier);
        egg.setVelocity(vel);

        return egg;
    }
}
