package me.zypj.bedwars.common.model.arena.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProtectionConfig {
    private final int spawn;
    private final int shop;
    private final int upgrades;
    private final int generator;
}
