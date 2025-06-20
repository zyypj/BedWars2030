package me.zypj.bedwars.systems.explosive.fireball.adapter;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public interface FireballAdapter {
    Fireball spawnFireball(Player shooter,
                           Location location,
                           double knockbackHorizontal,
                           double knockbackVertical,
                           float explosionPower,
                           boolean breakBlocks,
                           boolean makeFire,
                           double damageSelf,
                           double damageOthers,
                           double speedMultiplier);
}
