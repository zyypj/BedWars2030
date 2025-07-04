package me.zypj.bedwars.listener.projectil;

import me.zypj.bedwars.BedWarsPlugin;
import me.zypj.bedwars.systems.projectil.eggbridge.service.EggBridgeService;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class EggBridgeListener implements Listener {

    private final EggBridgeService service;

    public EggBridgeListener(BedWarsPlugin plugin) {
        this.service = plugin.getPluginBootstrap().getEggBridgeService();
    }

    @EventHandler
    public void onEggUse(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK
                && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (e.getItem() == null
                || e.getItem().getType() != Material.EGG) return;

        ItemStack itemInHand = e.getItem();
        Player player = e.getPlayer();

        e.setCancelled(true);
        if (service.isOnCooldown(player)) return;

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
            player.setItemInHand(itemInHand.getAmount() > 0 ? itemInHand : null);
        }

        service.launchEgg(player);
    }

    @EventHandler
    public void onChickenSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.CHICKEN
                && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
            e.setCancelled(true);
        }
    }
}
