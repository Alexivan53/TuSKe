package com.github.tukenuke.tuske.util;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * Minimal, version-stable translate helpers using registry keys.
 * Avoids legacy Material IDs, data/durability, and removed enums.
 */
public final class Translate {
    private Translate() {}

    public static String getIDTranslate(Enchantment enchantment) {
        if (enchantment == null) return "enchantment.none";
        NamespacedKey key = enchantment.getKey();
        return "enchantment." + (key != null ? key.getKey() : enchantment.getName().toLowerCase());
    }

    public static String getIDTranslate(EntityType entityType) {
        if (entityType == null) return "entity.generic.name";
        NamespacedKey key = entityType.getKey();
        return "entity." + (key != null ? key.getKey() : entityType.name().toLowerCase()) + ".name";
    }

    public static String getIDTranslate(Block block) {
        if (block == null) return "tile.air.name";
        NamespacedKey key = block.getType().getKey();
        return "tile." + (key != null ? key.getKey() : block.getType().name().toLowerCase()) + ".name";
    }

    public static String getIDTranslate(ItemStack item) {
        if (item == null) return "item.air.name";
        NamespacedKey key = item.getType().getKey();
        return (item.getType().isBlock() ? "tile." : "item.")
                + (key != null ? key.getKey() : item.getType().name().toLowerCase())
                + ".name";
    }
}


