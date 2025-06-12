package me.zypj.bedwars.listener.explosive;

import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.file.path.ConfigPath;
import me.zypj.bedwars.system.explosive.fireball.service.FireballService;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class FireballListener implements Listener {

    private final FireballService service;

    public FireballListener(BedWarsPlugin plugin) {
        this.service = plugin.getPluginBootstrap().getFireballService();
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        ItemStack inHand = e.getItem();
        if (inHand == null || inHand.getType() != Material.FIREBALL) return;

        Player player = e.getPlayer();
        e.setCancelled(true);
        if (service.isOnCooldown(player)) return;

        inHand.setAmount(inHand.getAmount() - 1);
        player.setItemInHand(inHand.getAmount() > 0 ? inHand : null);

        Vector dir = player.getLocation().getDirection().clone();
        dir.setY(0);
        dir.normalize();
        Location spawnLoc = player.getLocation().subtract(dir.multiply(0.5)).add(0, 1.0, 0);

        service.launchFireball(player, spawnLoc);
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof Fireball) {
            float r = (float) service.getConfig()
                    .getConfigDouble(ConfigPath.FIREBALL_EXPLOSION_POWER);
            e.setRadius(r);
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Fireball)) return;
        Fireball fireBall = (Fireball) e.getEntity();

        double radius = service.getConfig()
                .getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_RADIUS);
        double kbH    = service.getConfig()
                .getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_HORIZONTAL);
        double kbV    = service.getConfig()
                .getConfigDouble(ConfigPath.FIREBALL_KNOCKBACK_VERTICAL);
        double dmgSelf   = service.getConfig()
                .getConfigDouble(ConfigPath.FIREBALL_DAMAGE_SELF);
        double dmgOthers = service.getConfig()
                .getConfigDouble(ConfigPath.FIREBALL_DAMAGE_OTHERS);

        UUID shooterUuid = fireBall.hasMetadata("fireball-shooter")
                ? UUID.fromString(fireBall.getMetadata("fireball-shooter").get(0).asString())
                : null;

        Location loc = fireBall.getLocation();
        for (Player player : loc.getWorld().getPlayers()) {
            double dist = player.getLocation().distance(loc);
            if (dist > radius) continue;

            boolean isShooter = shooterUuid != null && player.getUniqueId().equals(shooterUuid);
            double dmg = isShooter ? dmgSelf : dmgOthers;
            player.damage(dmg, fireBall);

            Vector v = player.getLocation().toVector()
                    .subtract(loc.toVector())
                    .normalize()
                    .multiply(kbH);
            v.setY(kbV);
            player.setVelocity(v);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION
                || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (!(e.getEntity() instanceof Fireball)) return;

        e.blockList().clear();
    }
}
