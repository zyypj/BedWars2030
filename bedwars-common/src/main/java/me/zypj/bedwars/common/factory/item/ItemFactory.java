package me.zypj.bedwars.common.factory.item;

import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFactory {

    private final ItemStack item;
    private ItemMeta itemMeta;

    public ItemFactory(Material material) {
        this.item = new ItemStack(material);
        this.itemMeta = item.getItemMeta();
    }

    public ItemFactory(ItemStack item) {
        this.item = item;
        this.itemMeta = item.getItemMeta();
    }

    @Deprecated
    public ItemFactory(int id) {
        this.item = new ItemStack(Material.getMaterial(id));
        this.itemMeta = item.getItemMeta();
    }

    public ItemFactory setName(String name) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemFactory setGlow(boolean glow) {
        if (glow) {
            try {
                itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(
                        "§c[ItemFactory] Failed to apply effect “glow”: " + e.getMessage()
                );
            }
        }
        return this;
    }

    public ItemFactory setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemFactory addEnchant(Enchantment enchantment, int level) {
        if (enchantment == null) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Could not add enchantment: The provided enchantment is null."
            );
            return this;
        }

        try {
            itemMeta.addEnchant(enchantment, level, true);
        } catch (IllegalArgumentException e) {
            String enchantName = enchantment.getName();
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Failed to add enchantment ‘" + enchantName + "’: " + e.getMessage()
            );
        }
        return this;
    }

    public ItemFactory addUnsafeEnchant(Enchantment enchantment, int level) {
        if (enchantment == null) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Unable to add unsafe enchantment: The provided enchantment is null."
            );
            return this;
        }

        try {
            item.addUnsafeEnchantment(enchantment, level);
        } catch (IllegalArgumentException e) {
            String enchantName = enchantment.getName();
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Failed to add unsafe enchantment ‘" + enchantName + "’: " + e.getMessage()
            );
        }
        return this;
    }

    public ItemFactory setLore(String... lore) {
        List<String> formatted = Arrays.stream(lore)
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
        itemMeta.setLore(formatted);
        return this;
    }

    public ItemFactory setLore(List<String> lore) {
        List<String> formatted = lore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
        itemMeta.setLore(formatted);
        return this;
    }

    public ItemFactory setSkullOwner(String owner) {
        if (!(itemMeta instanceof SkullMeta)) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Unable to set head owner: Item is not a SkullMeta."
            );
            return this;
        }

        try {
            XSkull.of(item)
                    .profile(Profileable.username(owner))
                    .apply();
            itemMeta = item.getItemMeta();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Failed to set head owner ‘" + owner + "’: " + e.getMessage()
            );
        }
        return this;
    }

    public ItemFactory setSkullValue(String base64) {
        if (!(itemMeta instanceof SkullMeta)) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Cannot set head value: Item is not a SkullMeta."
            );
            return this;
        }

        try {
            XSkull.of(item)
                    .profile(Profileable.of(ProfileInputType.BASE64, base64))
                    .apply();
            itemMeta = item.getItemMeta();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Failed to set head value: " + e.getMessage()
            );
        }
        return this;
    }

    public ItemFactory setMaterial(Material material) {
        item.setType(material);
        itemMeta = item.getItemMeta();
        return this;
    }

    public ItemFactory setData(short data) {
        item.setDurability(data);
        return this;
    }

    public ItemFactory setDurability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemFactory addItemFlags(ItemFlag... flags) {
        if (flags == null || flags.length == 0) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] No item flag provided; skipping addItemFlags."
            );
            return this;
        }

        try {
            itemMeta.addItemFlags(flags);
        } catch (IllegalArgumentException e) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Failed to add item flags: " + e.getMessage()
            );
        }
        return this;
    }

    public ItemFactory setLeatherColor(Color color) {
        if (!(itemMeta instanceof LeatherArmorMeta)) {
            Bukkit.getConsoleSender().sendMessage(
                    "§c[ItemFactory] Unable to set leather armor color: Item is not a LeatherArmorMeta."
            );
            return this;
        }

        ((LeatherArmorMeta) itemMeta).setColor(color);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(itemMeta);
        return item;
    }
}
