package me.zypj.bedwars.api.factory.explosive.tnt.adapter.provider;


import me.zypj.bedwars.api.factory.explosive.tnt.adapter.TntAdapter;
import me.zypj.bedwars.api.factory.explosive.tnt.adapter.common.TntAdapterCommon;
import org.bukkit.Bukkit;

public class TntAdapterProvider {
    public static TntAdapter getAdapter() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_8_R3":
                return new TntAdapterCommon();
            // TODO: More versions support
            default:
                throw new UnsupportedOperationException("Unsupported NMS version: " + version);
        }
    }
}
