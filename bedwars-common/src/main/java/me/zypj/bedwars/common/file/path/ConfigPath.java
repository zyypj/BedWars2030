package me.zypj.bedwars.common.file.path;

public class ConfigPath {
    // TNT
    public static String TNT_PREFIX = "tnt.";

    public static String TNT_KNOCKBACK_PREFIX = TNT_PREFIX + "knockback.";
    public static String TNT_KNOCKBACK_HORIZONTAL = TNT_KNOCKBACK_PREFIX + "horizontal";
    public static String TNT_KNOCKBACK_VERTICAL = TNT_KNOCKBACK_PREFIX + "vertical";

    public static String TNT_EXPLOSION_PREFIX = TNT_PREFIX + "explosion.";
    public static String TNT_EXPLOSION_POWER = TNT_EXPLOSION_PREFIX + "power";
    public static String TNT_EXPLOSION_BREAK_BLOCKS = TNT_EXPLOSION_PREFIX + "break-blocks";

    public static String TNT_DAMAGE_PREFIX = TNT_PREFIX + "damage.";
    public static String TNT_DAMAGE_SELF = TNT_DAMAGE_PREFIX + "self";
    public static String TNT_DAMAGE_OTHERS = TNT_DAMAGE_PREFIX + "others";

    public static String TNT_FUSE_PREFIX = TNT_PREFIX + "fuse-time.";
    public static String TNT_FUSE_TICK = TNT_FUSE_PREFIX + "ticks";

    // Fireball
    public static String FIREBALL_PREFIX = "fireball.";

    public static String FIREBALL_KNOCKBACK_PREFIX = FIREBALL_PREFIX + "knockback.";
    public static String FIREBALL_KNOCKBACK_HORIZONTAL = FIREBALL_KNOCKBACK_PREFIX + "horizontal";
    public static String FIREBALL_KNOCKBACK_VERTICAL = FIREBALL_KNOCKBACK_PREFIX + "vertical";
    public static String FIREBALL_KNOCKBACK_RADIUS = FIREBALL_KNOCKBACK_PREFIX + "radius";

    public static String FIREBALL_EXPLOSION_PREFIX = FIREBALL_PREFIX + "explosion.";
    public static String FIREBALL_EXPLOSION_POWER = FIREBALL_EXPLOSION_PREFIX + "power";
    public static String FIREBALL_EXPLOSION_BREAK_BLOCKS = FIREBALL_EXPLOSION_PREFIX + "break-blocks";
    public static String FIREBALL_EXPLOSION_MAKE_FIRE = FIREBALL_EXPLOSION_PREFIX + "make-fire";

    public static String FIREBALL_DAMAGE_PREFIX = FIREBALL_PREFIX + "damage.";
    public static String FIREBALL_DAMAGE_SELF = FIREBALL_DAMAGE_PREFIX + "self";
    public static String FIREBALL_DAMAGE_OTHERS = FIREBALL_DAMAGE_PREFIX + "others";

    public static String FIREBALL_SPEED_MULTIPLIER = FIREBALL_PREFIX + "speed-multiplier";
    public static String FIREBALL_COOLDOWN = FIREBALL_PREFIX + "cooldown";
    public static String FIREBALL_HIT_ENABLED = FIREBALL_PREFIX + "hit-enabled";

    // Egg-Bridge
    public static String EGG_BRIDGE_PREFIX = "egg-bridge.";
    public static String EGG_BRIDGE_DURATION = EGG_BRIDGE_PREFIX + "duration";

    public static String EGG_BRIDGE_BRIDGE_PREFIX = EGG_BRIDGE_PREFIX + "bridge.";
    public static String EGG_BRIDGE_BRIDGE_MAX_LENGTH = EGG_BRIDGE_BRIDGE_PREFIX + "max-length";
    public static String EGG_BRIDGE_BRIDGE_WIDTH = EGG_BRIDGE_BRIDGE_PREFIX + "width";
    public static String EGG_BRIDGE_BRIDGE_HEIGHT_OFFSET = EGG_BRIDGE_BRIDGE_PREFIX + "height-offset";

    public static String EGG_BRIDGE_PROJECTILE_PREFIX = EGG_BRIDGE_PREFIX + "projectile.";
    public static String EGG_BRIDGE_PROJECTILE_SPEED_MULTIPLIER = EGG_BRIDGE_PROJECTILE_PREFIX + "speed-multiplier";
    public static String EGG_BRIDGE_PROJECTILE_GRAVITY_ENABLED = EGG_BRIDGE_PROJECTILE_PREFIX + "gravity-enabled";
    public static String EGG_BRIDGE_PROJECTILE_COOLDOWN = EGG_BRIDGE_PROJECTILE_PREFIX + "cooldown";

    public static String EGG_BRIDGE_PARTICLES_PREFIX = EGG_BRIDGE_PREFIX + "particles.";
    public static String EGG_BRIDGE_PARTICLES_ENABLED = EGG_BRIDGE_PARTICLES_PREFIX + "enabled";
    public static String EGG_BRIDGE_PARTICLES_TYPE = EGG_BRIDGE_PARTICLES_PREFIX + "type";
    public static String EGG_BRIDGE_PARTICLES_DATA = EGG_BRIDGE_PARTICLES_PREFIX + "data";
    public static String EGG_BRIDGE_PARTICLES_INTERVAL_TICKS = EGG_BRIDGE_PARTICLES_PREFIX + "interval-ticks";
}
