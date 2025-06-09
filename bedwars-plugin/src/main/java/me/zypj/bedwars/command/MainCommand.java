package me.zypj.bedwars.command;

import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.BedWarsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MainCommand implements CommandExecutor {

    private final BedWarsPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0]) {
            case "reload":
                plugin.getPluginBootstrap().getSubInstanceManager().getReloadCommand().execute(player);
            default:
                // TODO: Send no found command
        }

        return false;
    }

    private void sendHelp(Player player) {
        // TODO: Send help message
    }
}
