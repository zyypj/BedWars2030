package me.zypj.bedwars.common.model.game.arena.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BuildLimits {
    private final int yKillHeight;
    private final int maxY;
    private final int minY;
}
