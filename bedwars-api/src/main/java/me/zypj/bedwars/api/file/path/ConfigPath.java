package me.zypj.bedwars.api.file.path;

public class ConfigPath {
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
}
