package me.zypj.bedwars.common.model.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.GameState;
import me.zypj.bedwars.common.enums.GeneratorType;
import me.zypj.bedwars.common.model.arena.config.BuildLimits;
import me.zypj.bedwars.common.model.arena.config.ProtectionConfig;
import me.zypj.bedwars.common.model.arena.team.MTeam;
import me.zypj.bedwars.common.util.location.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;

import java.time.Instant;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class MArena {
    private final String id;

    private final String name;
    private final String displayName;
    private final String group;

    private final World world;

    private final GameState gameState;
    private final Instant startTime;

    private final int playersPerTeam;
    private final int minimumPlayers;
    private final int baseSize;
    private final double generatorSplitRange;

    private final Cuboid waitingArena;
    private final Location waitingLocation;
    private final Location spectatorLocation;

    private final Map<MTeam, Location> bedLocations, shopLocations, upgradeLocations, spawnLocations;

    private final Map<GeneratorType, Map<MGenerator, MTeam>> generatorAssignments;
    private final Map<GeneratorType, Map<MGenerator, Location>> generatorLocations;

    private final ProtectionConfig protectionConfig;
    private final BuildLimits buildLimits;
}
