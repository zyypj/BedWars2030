package me.zypj.bedwars.common.model.game.arena.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeneratorSettings {
    private final double itemsPerSecond;
    private final int stackCap;
    private final double splitRadius;
    private final int initialDelaySeconds;
}

