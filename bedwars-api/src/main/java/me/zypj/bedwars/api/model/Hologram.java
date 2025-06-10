package me.zypj.bedwars.api.model;

import lombok.Data;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Data
public class Hologram {
    private final UUID id;
    private final Location location;
    private final List<String> lines;
}
