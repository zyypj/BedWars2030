package me.zypj.bedwars.systems.visual.hologram.adapter.provider;

import me.zypj.bedwars.systems.visual.hologram.adapter.HologramAdapter;
import me.zypj.bedwars.systems.visual.hologram.adapter.common.HologramAdapterCommon;

public class HologramAdapterProvider {
    public static HologramAdapter getAdapter() {
        String version = org.bukkit.Bukkit.getServer()
                .getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_8_R3":
                return new HologramAdapterCommon();
            // TODO: More versions support
            default:
                throw new UnsupportedOperationException("Unsupported version: " + version);
        }
    }
}
