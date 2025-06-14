package me.zypj.bedwars.system.projectil.eggbridge.adapter;

import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;

public interface EggBridgeAdapter {
    Egg spawnEgg(Player shooter,
                 Location location,
                 double speedMultiplier,
                 boolean gravityEnabled);
}
