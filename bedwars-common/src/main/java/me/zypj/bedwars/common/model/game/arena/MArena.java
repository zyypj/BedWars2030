package me.zypj.bedwars.common.model.game.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.game.generator.GeneratorType;
import me.zypj.bedwars.common.enums.game.shopkeeper.ShopKeeperType;
import me.zypj.bedwars.common.model.game.arena.config.BuildLimits;
import me.zypj.bedwars.common.model.game.arena.config.ProtectionConfig;
import me.zypj.bedwars.common.model.game.arena.shopkeeper.MShopKeeper;
import me.zypj.bedwars.common.model.game.arena.team.MTeam;
import me.zypj.bedwars.common.util.location.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class MArena {
    private final String name;
    private final String displayName;
    private final String group;

    private final World world;

    private final int playersPerTeam;
    private final int minimumPlayers;
    private final int baseSize;
    private final double generatorSplitRange;

    private final Cuboid waitingArena;
    private final Location waitingLocation;
    private final Location spectatorLocation;

    private final Map<MTeam, Location> bedLocations, spawnLocations;

    private final List<MShopKeeper> shopKeepers;

    private final Map<GeneratorType, Map<MGenerator, MTeam>> generatorAssignments;
    private final Map<GeneratorType, Map<MGenerator, Location>> generatorLocations;

    private final ProtectionConfig protectionConfig;
    private final BuildLimits buildLimits;

    public Optional<Map<MGenerator, MTeam>> getTeamGeneratorsByType(GeneratorType type) {
        if (type == null) return Optional.empty();
        Map<MGenerator, MTeam> map = generatorAssignments.get(type);
        if (map == null || map.isEmpty()) return Optional.empty();

        Map<MGenerator, MTeam> onlyTeamOwned = new java.util.HashMap<>();
        for (Map.Entry<MGenerator, MTeam> e : map.entrySet()) {
            if (e.getKey() != null && !e.getKey().isNeutral())
                onlyTeamOwned.put(e.getKey(), e.getValue());
        }

        return onlyTeamOwned.isEmpty()
                ? Optional.empty()
                : Optional.of(Collections.unmodifiableMap(onlyTeamOwned));
    }

    public List<MGenerator> getNeutralGeneratorsByType(GeneratorType type) {
        Map<MGenerator, Location> locs = generatorLocations.get(type);
        if (locs == null || locs.isEmpty()) return Collections.emptyList();

        List<MGenerator> result = new ArrayList<>();
        for (MGenerator gen : locs.keySet()) {
            if (gen != null && gen.isNeutral()) result.add(gen);
        }

        return Collections.unmodifiableList(result);
    }

    public List<MShopKeeper> getShopKeepersByTeam(MTeam team) {
        if (team == null || shopKeepers == null || shopKeepers.isEmpty()) return Collections.emptyList();

        List<MShopKeeper> result = new ArrayList<>();
        for (MShopKeeper sk : shopKeepers) {
            if (sk != null && team.equals(sk.getTeam())) result.add(sk);
        }

        return result.isEmpty()
                ? Collections.emptyList()
                : Collections.unmodifiableList(result);
    }

    public List<MShopKeeper> getShopKeepersByType(ShopKeeperType type) {
        if (type == null || shopKeepers == null || shopKeepers.isEmpty()) return Collections.emptyList();

        List<MShopKeeper> result = new ArrayList<>();
        for (MShopKeeper sk : shopKeepers) {
            if (sk != null && type == sk.getShopKeeperType()) result.add(sk);
        }

        return result.isEmpty()
                ? Collections.emptyList()
                : Collections.unmodifiableList(result);
    }
}
