package me.zypj.bedwars.api.language;

import org.bukkit.entity.Player;

public interface MessageService {
    String getMessage(Player player, String path);
    String getMessage(String iso, String path);
}
