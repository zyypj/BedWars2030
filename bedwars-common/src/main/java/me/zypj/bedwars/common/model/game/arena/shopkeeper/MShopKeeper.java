package me.zypj.bedwars.common.model.game.arena.shopkeeper;

import lombok.Getter;
import lombok.Setter;
import me.zypj.bedwars.common.enums.game.shopkeeper.ShopKeeperSkinType;
import me.zypj.bedwars.common.enums.game.shopkeeper.ShopKeeperType;
import me.zypj.bedwars.common.model.game.arena.MArena;
import me.zypj.bedwars.common.model.game.arena.team.MTeam;
import org.bukkit.Location;

@Getter
@Setter
public class MShopKeeper {

    private final MArena arena;
    private final MTeam team;

    private final Location location;

    private ShopKeeperSkinType skinType;
    private ShopKeeperType shopKeeperType;

    public MShopKeeper(MArena arena, MTeam team, Location location, ShopKeeperSkinType skinType, ShopKeeperType shopKeeperType) {
        this.arena = arena;
        this.team = team;
        this.location = location;
        this.skinType = skinType;
        this.shopKeeperType = shopKeeperType;
    }
}
