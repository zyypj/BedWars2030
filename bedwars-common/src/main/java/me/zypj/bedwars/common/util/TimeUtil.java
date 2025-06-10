package me.zypj.bedwars.common.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TimeUtil {

    @NotNull
    @Contract(pure = true)
    public static String formatTime(int ticks) {
        double seconds = ticks / 20.0;
        return String.format("§f%.1fs", seconds);
    }
}
