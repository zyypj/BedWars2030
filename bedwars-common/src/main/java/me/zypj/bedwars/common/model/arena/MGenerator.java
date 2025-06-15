package me.zypj.bedwars.common.model.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.GeneratorType;
import me.zypj.bedwars.common.model.arena.team.MTeam;
import org.bukkit.Location;

@Getter
@RequiredArgsConstructor
public class MGenerator {
    private final MArena arena;
    private final MTeam team;

    private final GeneratorType generatorType;

    private final Location location;
}
