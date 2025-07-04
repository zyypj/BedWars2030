package me.zypj.bedwars.common.model.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.zypj.bedwars.common.enums.GameState;
import me.zypj.bedwars.common.model.game.arena.MArena;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class MGame {
    private final String id;

    private final MArena arena;

    private final GameState gameState;
    private final Instant startTime;
}
