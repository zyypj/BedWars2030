package me.zypj.bedwars.system.projectil.eggbridge.service;

import lombok.Getter;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.event.projectil.EggBridgeUseEvent;
import me.zypj.bedwars.common.file.path.ConfigPath;
import me.zypj.bedwars.common.file.service.ConfigService;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.system.projectil.eggbridge.adapter.EggBridgeAdapter;
import me.zypj.bedwars.system.projectil.eggbridge.adapter.provider.EggBridgeAdapterProvider;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class EggBridgeService {

    private static final int FORWARD_OFFSET = 1;
    private static final int BACK_OFFSET = 2;

    private final BedWarsPlugin plugin;
    private final ConfigService config;
    private final EggBridgeAdapter adapter;

    private final Map<Player, Long> lastUse = new ConcurrentHashMap<>();
    private final Collection<BridgeTask> activeTasks = new CopyOnWriteArrayList<>();
    private final Set<Block> bridgeBlocks = ConcurrentHashMap.newKeySet();

    public EggBridgeService(BedWarsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getPluginBootstrap().getConfigService();
        this.adapter = EggBridgeAdapterProvider.getAdapter();

        new BridgeRunner().runTaskTimer(plugin, 1L, 1L);
    }

    public boolean isOnCooldown(Player player) {
        double cd = config.getConfigDouble(ConfigPath.EGG_BRIDGE_PROJECTILE_COOLDOWN);
        return lastUse.containsKey(player)
                && System.currentTimeMillis() - lastUse.get(player) < (long) (cd * 1000);
    }

    public void launchEgg(Player shooter) {
        lastUse.put(shooter, System.currentTimeMillis());

        Vector look = shooter.getLocation().getDirection();
        boolean vertical = Math.abs(look.getY()) >= 0.999;
        Settings s = loadSettings();

        Location spawnLoc = shooter.getEyeLocation().add(look.clone().multiply(FORWARD_OFFSET));
        Egg egg = adapter.spawnEgg(shooter, spawnLoc, s.speedMultiplier, s.gravityEnabled);

        EggBridgeUseEvent evt = new EggBridgeUseEvent(shooter, spawnLoc, egg);
        Bukkit.getPluginManager().callEvent(evt);
        if (evt.isCancelled()) {
            egg.remove();
            return;
        }

        Debug.log("[EggBridgeService] used by " + shooter.getName(), true);

        if (vertical) {
            if (look.getY() > 0) {
                Location behindEgg = spawnLoc.clone()
                        .subtract(look.clone().multiply(BACK_OFFSET));
                int baseY = shooter.getLocation().getBlockY() + 2;
                Location base = new Location(
                        shooter.getWorld(),
                        behindEgg.getBlockX(),
                        baseY,
                        behindEgg.getBlockZ()
                );
                activeTasks.add(new TowerTask(egg, base, s));
            } else {
                buildBridgeDown(shooter);
                egg.remove();
            }
        } else {
            activeTasks.add(new BridgeData(egg, look.clone().setY(0).normalize(), s));
        }
    }

    private void buildBridgeDown(Player shooter) {
        Block under = shooter.getLocation().clone()
                .subtract(0, 1, 0)
                .getBlock();
        placeBlock(under);
    }

    private void placeBlock(Block b) {
        if (b.getType() == Material.AIR) {
            // TODO: Change wool color based on player's team
            b.setType(Material.WOOL);
            bridgeBlocks.add(b);
        }
    }

    private class BridgeRunner extends BukkitRunnable {
        @Override
        public void run() {
            activeTasks.removeIf(task -> !task.tick());
        }
    }

    private interface BridgeTask {
        boolean tick();
    }

    private class BridgeData implements BridgeTask {
        final Egg egg;
        final Vector dir;
        final Settings s;
        Location last;
        int tick = 0, placed = 0;

        BridgeData(Egg egg, Vector dir, Settings s) {
            this.egg = egg;
            this.dir = dir;
            this.s = s;
            this.last = egg.getLocation().clone();
        }

        @Override
        public boolean tick() {
            if (tick >= s.durationSeconds * 20
                    || placed >= s.maxLength
                    || !egg.isValid()
                    || egg.isDead()) {
                egg.remove();
                return false;
            }

            Location cur = egg.getLocation();
            Vector diff = cur.toVector().subtract(last.toVector());
            double dist = diff.length();
            Vector step = diff.clone().normalize();
            Vector perp = new Vector(-step.getZ(), 0, step.getX());

            Player shooter = null;
            Projectile proj = egg;
            if (proj.getShooter() instanceof Player) {
                shooter = (Player) proj.getShooter();
            }
            Location feetLoc = null, headLoc = null;
            if (shooter != null) {
                feetLoc = shooter.getLocation().getBlock().getLocation();
                headLoc = shooter.getLocation().clone().add(0, 1, 0).getBlock().getLocation();
            }

            for (double d = 0; d <= dist && placed < s.maxLength; d += 0.5) {
                Location pt = last.clone().add(step.clone().multiply(d));
                Location center = pt.clone()
                        .subtract(dir.clone().multiply(BACK_OFFSET))
                        .add(0, s.heightOffset, 0);

                if (s.useParticles && tick % s.particleInterval == 0) {
                    center.getWorld().playEffect(center, Effect.valueOf(s.particleType), s.particleData);
                }

                int half = s.width / 2;
                for (int i = -half; i <= half && placed < s.maxLength; i++) {
                    Block b = center.clone().add(perp.clone().multiply(i)).getBlock();
                    if (b.getType() == Material.AIR) {
                        if (shooter != null
                                && (b.getLocation().equals(feetLoc) || b.getLocation().equals(headLoc))) {
                            continue;
                        }
                        // TODO: Change wool color based on player's team
                        b.setType(Material.WOOL);
                        bridgeBlocks.add(b);
                        placed++;
                    }
                }
            }

            last = cur.clone();
            tick++;
            return true;
        }
    }

    private class TowerTask implements BridgeTask {
        final Egg egg;
        final Location base;
        final Settings s;
        int height = 0;
        double maxEggY;

        TowerTask(Egg egg, Location base, Settings s) {
            this.egg = egg;
            this.base = base;
            this.s = s;
            this.maxEggY = egg.getLocation().getY();
        }

        @Override
        public boolean tick() {
            if (!egg.isValid() || egg.isDead()) {
                // preenche até a maior altura atingida pelo ovo
                int limit = (int) Math.floor(maxEggY - base.getY());
                for (; height <= limit; height++) {
                    Block b = base.clone().add(0, height, 0).getBlock();
                    if (b.getType() == Material.AIR) {
                        // TODO: Change wool color based on player's team
                        b.setType(Material.WOOL);
                        bridgeBlocks.add(b);
                    }
                }
                egg.remove();
                return false;
            }

            Location cur = egg.getLocation();
            if (cur.getY() > maxEggY) {
                maxEggY = cur.getY();
                int blockLimit = (int) Math.floor(maxEggY - base.getY());
                if (height <= blockLimit) {
                    Block b = base.clone().add(0, height, 0).getBlock();
                    if (b.getType() == Material.AIR) {
                        // TODO: Change wool color based on player's team
                        b.setType(Material.WOOL);
                        bridgeBlocks.add(b);
                    }
                    height++;
                }
            }
            return true;
        }
    }

    private Settings loadSettings() {
        return new Settings(
                config.getConfigDouble(ConfigPath.EGG_BRIDGE_PROJECTILE_SPEED_MULTIPLIER),
                config.getConfigBoolean(ConfigPath.EGG_BRIDGE_PROJECTILE_GRAVITY_ENABLED),
                config.getConfigDouble(ConfigPath.EGG_BRIDGE_DURATION),
                config.getConfigInt(ConfigPath.EGG_BRIDGE_BRIDGE_MAX_LENGTH),
                config.getConfigInt(ConfigPath.EGG_BRIDGE_BRIDGE_WIDTH),
                config.getConfigInt(ConfigPath.EGG_BRIDGE_BRIDGE_HEIGHT_OFFSET),
                config.getConfigBoolean(ConfigPath.EGG_BRIDGE_PARTICLES_ENABLED),
                config.getConfigString(ConfigPath.EGG_BRIDGE_PARTICLES_TYPE),
                config.getConfigInt(ConfigPath.EGG_BRIDGE_PARTICLES_INTERVAL_TICKS),
                config.getConfigInt(ConfigPath.EGG_BRIDGE_PARTICLES_DATA)
        );
    }

    private static class Settings {
        final double speedMultiplier;
        final boolean gravityEnabled;
        final double durationSeconds;
        final int maxLength;
        final int width;
        final int heightOffset;
        final boolean useParticles;
        final String particleType;
        final int particleInterval;
        final int particleData;

        Settings(double speedMultiplier,
                 boolean gravityEnabled,
                 double durationSeconds,
                 int maxLength,
                 int width,
                 int heightOffset,
                 boolean useParticles,
                 String particleType,
                 int particleInterval,
                 int particleData) {
            this.speedMultiplier = speedMultiplier;
            this.gravityEnabled = gravityEnabled;
            this.durationSeconds = durationSeconds;
            this.maxLength = maxLength;
            this.width = width;
            this.heightOffset = heightOffset;
            this.useParticles = useParticles;
            this.particleType = particleType.toUpperCase();
            this.particleInterval = particleInterval;
            this.particleData = particleData;
        }
    }
}
