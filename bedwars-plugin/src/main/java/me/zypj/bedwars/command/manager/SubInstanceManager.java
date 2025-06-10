package me.zypj.bedwars.command.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.logger.Debug;
import me.zypj.bedwars.command.subcommand.ReloadCommand;

@Getter
@RequiredArgsConstructor
public class SubInstanceManager {

    private final BedWarsPlugin plugin;
    private ReloadCommand reloadCommand;

    public void init() {
        reloadCommand = new ReloadCommand(plugin);

        Debug.log("&e[InstanceManager] Instances created.", true);
    }
}
