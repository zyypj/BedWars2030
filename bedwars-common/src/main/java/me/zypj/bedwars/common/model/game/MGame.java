package me.zypj.bedwars.common.model.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.game.GameState;
import me.zypj.bedwars.common.model.game.arena.MArena;
import me.zypj.bedwars.common.model.game.arena.team.MTeam;
import me.zypj.bedwars.common.model.game.player.PlayerProfile;
import org.bukkit.Color;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MGame {
    private final String id;

    private final MArena arena;

    private final List<MTeam> teams;
    private final List<PlayerProfile> players;

    private final GameState gameState;
    private final Instant startTime;

    public Optional<MTeam> getTeamByColor(Color color) {
        if (color == null) return Optional.empty();
        for (MTeam team : teams) {
            if (team != null && color.equals(team.getColor()))
                return Optional.of(team);
        }
        return Optional.empty();
    }

    public Optional<PlayerProfile> getPlayerProfileByName(String name) {
        if (name == null || name.isEmpty()) return Optional.empty();
        for (PlayerProfile pp : players) {
            if (pp != null && pp.getPlayer() != null &&
                    name.equalsIgnoreCase(pp.getPlayer().getName()))
                return Optional.of(pp);

        }
        return Optional.empty();
    }

    public Optional<PlayerProfile> getPlayerProfileByUuid(UUID uuid) {
        if (uuid == null) return Optional.empty();
        for (PlayerProfile pp : players) {
            if (pp != null && uuid.equals(pp.getUuid()))
                return Optional.of(pp);
        }
        return Optional.empty();
    }
}
