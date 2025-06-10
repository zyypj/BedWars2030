package me.zypj.bedwars.listener.explosive;

import lombok.var;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.file.path.ConfigPath;
import me.zypj.bedwars.api.util.TimeUtil;
import me.zypj.bedwars.system.explosive.tnt.service.TntService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.UUID;

public class TntListener implements Listener {

    private final BedWarsPlugin plugin;
    private final TntService tntService;

    public TntListener(BedWarsPlugin plugin) {
        this.plugin = plugin;
        this.tntService = plugin.getPluginBootstrap().getTntService();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlockPlaced();
        if (block.getType() != Material.TNT) return;
        e.setCancelled(true);

        Player player = e.getPlayer();
        ItemStack handItem = player.getItemInHand();
        if (handItem != null && handItem.getType() == Material.TNT) {
            int amount = handItem.getAmount() - 1;
            if (amount > 0) {
                handItem.setAmount(amount);
                player.setItemInHand(handItem);
            } else {
                player.setItemInHand(null);
            }
        }

        TNTPrimed tnt = tntService.spawnTnt(
                player,
                block.getLocation().add(0.5, 0, 0.5)
        );

        var holoService = plugin.getPluginBootstrap().getHologramService();
        UUID holoId = holoService.factory()
                .location(tnt.getLocation().add(0, 0.1, 0))
                .lines(Collections.singletonList(TimeUtil.formatTime(tnt.getFuseTicks())))
                .create()
                .getId();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!tnt.isValid() || tnt.isDead()) {
                    holoService.removeHologram(holoId);
                    cancel();
                    return;
                }

                Location followLoc = tnt.getLocation().add(0, 0.1, 0);
                String timeText = TimeUtil.formatTime(tnt.getFuseTicks());

                holoService.moveHologram(holoId, followLoc);
                holoService.updateHologramLines(
                        holoId,
                        Collections.singletonList(timeText)
                );
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            float r = (float) tntService.getConfig()
                    .getConfigDouble(ConfigPath.TNT_EXPLOSION_POWER);
            e.setRadius(r);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (!(e.getEntity() instanceof TNTPrimed)) return;

        TNTPrimed tnt = (TNTPrimed) e.getEntity();
        boolean breakBlocks = tntService.getConfig()
                .getConfigBoolean(ConfigPath.TNT_EXPLOSION_BREAK_BLOCKS);
        if (!breakBlocks) e.blockList().clear();

        e.setCancelled(true);

        Location loc = tnt.getLocation();
        double radius = tntService.getConfig().getConfigDouble(ConfigPath.TNT_EXPLOSION_POWER);
        double kbH = tntService.getConfig().getConfigDouble(ConfigPath.TNT_KNOCKBACK_HORIZONTAL);
        double kbV = tntService.getConfig().getConfigDouble(ConfigPath.TNT_KNOCKBACK_VERTICAL);
        double dmgSelf = tntService.getConfig().getConfigDouble(ConfigPath.TNT_DAMAGE_SELF);
        double dmgOthers = tntService.getConfig().getConfigDouble(ConfigPath.TNT_DAMAGE_OTHERS);

        UUID shooterUuid = null;
        if (tnt.hasMetadata("tnt-shooter")) {
            MetadataValue md = tnt.getMetadata("tnt-shooter").get(0);
            shooterUuid = UUID.fromString(md.asString());
        }

        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distance(loc) > radius) continue;
            boolean isShooter = shooterUuid != null && p.getUniqueId().equals(shooterUuid);
            double dmg = isShooter ? dmgSelf : dmgOthers;
            p.damage(dmg, tnt);
            Vector dir = p.getLocation().toVector().subtract(loc.toVector()).normalize();
            dir.multiply(kbH).setY(kbV);
            p.setVelocity(dir);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            e.setCancelled(true);
        }
    }
}
