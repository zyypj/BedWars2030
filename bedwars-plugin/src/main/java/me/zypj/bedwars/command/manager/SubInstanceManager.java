package me.zypj.bedwars.command.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.api.logger.Debug;

@Getter
@RequiredArgsConstructor
public class SubInstanceManager {

    private final BedWarsPlugin plugin;

    public void init() {

        Debug.log("&e[InstanceManager] Instances created.", true);
    }
}
