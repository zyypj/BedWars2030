package me.zypj.bedwars.common.enums.game.generator;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public enum GeneratorType {
    IRON(Material.IRON_INGOT),
    GOLD(Material.GOLD_INGOT),
    UPGRADE(Material.NETHER_STAR),
    DIAMOND(Material.DIAMOND),
    EMERALD(Material.EMERALD);

    private final Material material;

    GeneratorType(Material material) {
        this.material = material;
    }
}
