package me.zypj.bedwars.common.model.game.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.game.generator.GeneratorOwnerType;
import me.zypj.bedwars.common.enums.game.generator.GeneratorType;
import me.zypj.bedwars.common.model.game.arena.config.GeneratorSettings;
import me.zypj.bedwars.common.model.game.arena.team.MTeam;
import org.bukkit.Location;

import java.util.Objects;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class MGenerator {
    private final MArena arena;
    private final MTeam team;

    private final GeneratorType generatorType;
    private final GeneratorSettings generatorSettings;

    private final Location location;

    public Optional<MTeam> getTeam() {
        return Optional.ofNullable(team);
    }

    public boolean isNeutral() {
        return team == null;
    }

    public GeneratorOwnerType getOwnerType() {
        return team == null ? GeneratorOwnerType.NEUTRAL : GeneratorOwnerType.TEAM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MGenerator)) return false;
        MGenerator that = (MGenerator) o;
        return Objects.equals(arena, that.arena)
                && Objects.equals(team, that.team)
                && generatorType == that.generatorType
                && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arena, team, generatorType, location);
    }
}
