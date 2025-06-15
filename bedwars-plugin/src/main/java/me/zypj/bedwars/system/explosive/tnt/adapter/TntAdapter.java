package me.zypj.bedwars.system.explosive.tnt.adapter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public interface TntAdapter {

    TNTPrimed spawnTnt(Player shooter,
                       Location location,
                       double knockbackHorizontal,
                       double knockbackVertical,
                       float explosionPower,
                       boolean breakBlocks,
                       double damageSelf,
                       double damageOthers,
                       int fuseTicks) throws IllegalAccessException;
}
