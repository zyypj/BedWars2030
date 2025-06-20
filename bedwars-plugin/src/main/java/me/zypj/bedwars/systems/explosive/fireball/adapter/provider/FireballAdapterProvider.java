package me.zypj.bedwars.systems.explosive.fireball.adapter.provider;

import me.zypj.bedwars.systems.explosive.fireball.adapter.FireballAdapter;
import me.zypj.bedwars.systems.explosive.fireball.adapter.common.FireballAdapterCommon;
import org.bukkit.Bukkit;

public class FireballAdapterProvider {
    public static FireballAdapter getAdapter() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_8_R3":
                return new FireballAdapterCommon();
            // TODO: More versions support
            default:
                throw new UnsupportedOperationException("Unsupported NMS version: " + version);
        }
    }
}
