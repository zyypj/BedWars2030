package me.zypj.bedwars.system.explosive.tnt.adapter.common;

import me.zypj.bedwars.system.explosive.tnt.adapter.TntAdapter;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;

public class TntAdapterCommon implements TntAdapter {

    @Override
    public TNTPrimed spawnTnt(Player shooter,
                              Location location,
                              double knockbackHorizontal,
                              double knockbackVertical,
                              float explosionPower,
                              boolean breakBlocks,
                              double damageSelf,
                              double damageOthers,
                              int fuseTicks) {
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityTNTPrimed nmsTnt = new EntityTNTPrimed(
                nmsWorld,
                location.getX(),
                location.getY(),
                location.getZ(),
                shooter instanceof CraftPlayer ? ((CraftPlayer) shooter).getHandle() : null
        );

        nmsTnt.fuseTicks = fuseTicks;
        nmsTnt.yield = explosionPower;
        nmsTnt.isIncendiary = false;

        nmsWorld.addEntity(nmsTnt);

        TNTPrimed bukkitTnt = (TNTPrimed) nmsTnt.getBukkitEntity();
        bukkitTnt.setMetadata("tnt-shooter",
                new FixedMetadataValue(
                        Bukkit.getPluginManager().getPlugin("BedWars2030"),
                        shooter.getUniqueId().toString()
                )
        );
        return bukkitTnt;
    }
}
