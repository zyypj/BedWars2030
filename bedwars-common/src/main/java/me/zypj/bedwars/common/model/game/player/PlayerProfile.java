package me.zypj.bedwars.common.model.game.player;

import lombok.Getter;
import lombok.Setter;
import me.zypj.bedwars.common.enums.player.PlayerState;
import me.zypj.bedwars.common.model.game.MGame;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerProfile {

    private final Player player;
    private final UUID uuid;

    private final MGame game;

    @Setter
    private PlayerState playerState;

    public PlayerProfile(Player player, MGame game, PlayerState playerState) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.game = game;
        this.playerState = playerState;
    }
}
