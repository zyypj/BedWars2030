package me.zypj.bedwars.systems.projectil.eggbridge.adapter.provider;

import me.zypj.bedwars.systems.projectil.eggbridge.adapter.EggBridgeAdapter;
import me.zypj.bedwars.systems.projectil.eggbridge.adapter.common.EggBridgeAdapterCommon;
import org.bukkit.Bukkit;

public class EggBridgeAdapterProvider {
    public static EggBridgeAdapter getAdapter() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_8_R3":
                return new EggBridgeAdapterCommon();
            // TODO: More versions support
            default:
                throw new UnsupportedOperationException("Unsupported NMS version: " + version);
        }
    }
}
