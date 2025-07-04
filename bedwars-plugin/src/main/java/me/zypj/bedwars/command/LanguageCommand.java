package me.zypj.bedwars.command;

import lombok.RequiredArgsConstructor;
import lombok.var;
import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.common.file.path.MessagePath;
import me.zypj.bedwars.common.util.CooldownUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class LanguageCommand implements CommandExecutor {

    private final BedWarsPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if (!CooldownUtil.tryCooldown(p, "language_command", 5)) return false;

        if (args.length != 1) {
            p.sendMessage(
                    plugin.getPluginBootstrap()
                            .getMessageService()
                            .getMessage(p, MessagePath.LANGUAGE_COMMAND_ERROR_USAGE)
            );
            return false;
        }

        String iso = args[0].toLowerCase();
        var bootstrap = plugin.getPluginBootstrap();
        var cfg = bootstrap.getConfigAdapter();
        var msg = bootstrap.getMessageService();
        var langAdapter = bootstrap.getLanguageFileAdapter();

        if (!langAdapter.getAvailableLanguages().contains(iso)
                || cfg.getDisabledLanguages().stream().map(String::toLowerCase).anyMatch(iso::equals)
        ) {
            p.sendMessage(msg.getMessage(p, MessagePath.LANGUAGE_COMMAND_ERROR_NOT_FOUND));
            return false;
        }

        bootstrap.getLanguagePreferenceService().setLanguage(p, iso);

        p.sendMessage(
                msg.getMessage(iso, MessagePath.LANGUAGE_COMMAND_CHANGED)
                        .replace("{LANGUAGE-NAME}", msg.getMessage(iso, MessagePath.LANGUAGE_NAME))
        );

        return true;
    }
}
