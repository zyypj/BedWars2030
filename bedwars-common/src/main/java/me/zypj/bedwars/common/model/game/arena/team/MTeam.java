package me.zypj.bedwars.common.model.game.arena.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.GeneratorType;
import me.zypj.bedwars.common.model.game.arena.MArena;
import me.zypj.bedwars.common.model.game.arena.MGenerator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class MTeam {
    private final String name;
    private final Color color;
    private final MArena arena;

    private final List<Player> members;

    private final Location bedLocation, shopLocation, upgradeLocation;

    private final Map<GeneratorType, Map<MGenerator, Location>> generatorLocations;
}
