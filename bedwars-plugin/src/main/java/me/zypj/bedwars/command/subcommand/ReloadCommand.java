package me.zypj.bedwars.command.subcommand;

import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.file.path.MessagePath;
import me.zypj.bedwars.common.logger.Debug;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ReloadCommand {

    private final BedWarsPlugin plugin;

    public void execute(Player player) {
        if (!player.hasPermission("bw.setup") && !player.hasPermission("bw.*")) {
            player.sendMessage(plugin.getPluginBootstrap().getMessageService().getMessage(player, MessagePath.NO_PERMISSION));
            return;
        }

        plugin.reloadConfig();
        plugin.getPluginBootstrap().getLanguageFileAdapter().reload();
        player.sendMessage("§aSettings reloaded successfully");
        Debug.log("&e[ReloadCommand] Settings reloaded by " + player.getName() + ".", true);
    }
}
