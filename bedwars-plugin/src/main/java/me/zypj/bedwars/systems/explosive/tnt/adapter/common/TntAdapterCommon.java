package me.zypj.bedwars.systems.explosive.tnt.adapter.common;

import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.systems.explosive.tnt.adapter.TntAdapter;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class TntAdapterCommon implements TntAdapter {

    private static final Constructor<EntityTNTPrimed> TNT_PRIMED_CTOR;
    private static final Field IGNITER_FIELD;
    private static final String METADATA_KEY = "tnt-shooter";

    static {
        Constructor<EntityTNTPrimed> ctor = null;
        try {
            ctor = EntityTNTPrimed.class.getConstructor(World.class, double.class, double.class, double.class, EntityLiving.class);
        } catch (NoSuchMethodException ignored) {
        }
        Field igniter = null;
        for (Field f : EntityTNTPrimed.class.getDeclaredFields()) {
            if (EntityLiving.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                igniter = f;
                break;
            }
        }
        TNT_PRIMED_CTOR = ctor;
        IGNITER_FIELD = igniter;
    }

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
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        EntityLiving igniter = shooter instanceof CraftPlayer ? ((CraftPlayer) shooter).getHandle() : null;
        EntityTNTPrimed nmsTnt;

        if (TNT_PRIMED_CTOR != null) {
            try {
                nmsTnt = TNT_PRIMED_CTOR.newInstance(world, location.getX(), location.getY(), location.getZ(), igniter);
            } catch (ReflectiveOperationException e) {
                Debug.log("&cFailed to instantiate EntityTNTPrimed with full constructor, falling back: " + e.getMessage(), false);
                nmsTnt = new EntityTNTPrimed(world);
                nmsTnt.setPosition(location.getX(), location.getY(), location.getZ());
                if (igniter != null && IGNITER_FIELD != null) {
                    try {
                        IGNITER_FIELD.set(nmsTnt, igniter);
                    } catch (IllegalAccessException ex) {
                        Debug.log("&cFailed to set igniter field on EntityTNTPrimed: " + ex.getMessage(), false);
                    }
                }
            }
        } else {
            nmsTnt = new EntityTNTPrimed(world);
            nmsTnt.setPosition(location.getX(), location.getY(), location.getZ());
            if (igniter != null && IGNITER_FIELD != null) {
                try {
                    IGNITER_FIELD.set(nmsTnt, igniter);
                } catch (IllegalAccessException ex) {
                    Debug.log("&cFailed to set igniter field on EntityTNTPrimed: " + ex.getMessage(), false);
                }
            }
        }

        nmsTnt.fuseTicks = fuseTicks;
        nmsTnt.yield = explosionPower;
        nmsTnt.isIncendiary = breakBlocks;
        world.addEntity(nmsTnt);

        TNTPrimed bukkitTnt = (TNTPrimed) nmsTnt.getBukkitEntity();
        Plugin plugin = Bukkit.getPluginManager().getPlugin("BedWars2030");
        bukkitTnt.setMetadata(METADATA_KEY, new FixedMetadataValue(plugin, shooter.getUniqueId().toString()));
        return bukkitTnt;
    }
}
