package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import java.util.ArrayList;
import java.util.List;

public class Presets {

    public static List<TradeOfferEntry> getPreset(String name) {
        List<TradeOfferEntry> offers = new ArrayList<>();
        switch (name.toLowerCase()) {
            case "normal1":
                add(offers, Material.HAY_BLOCK, 64, Material.GOLD_BLOCK, 1);
                add(offers, Material.GOLD_NUGGET, 5, Material.WHEAT_SEEDS, 64);
                add(offers, Material.GOLD_INGOT, 1, Material.APPLE, 32);
                add(offers, Material.GOLD_BLOCK, 1, Material.IRON_BLOCK, 32);
                add(offers, Material.GOLD_INGOT, 1, Material.BOW, 1);
                add(offers, Material.GOLD_INGOT, 1, Material.OBSIDIAN, 16);
                add(offers, Material.GOLD_BLOCK, 3, Material.DIAMOND, 3);
                add(offers, Material.GOLD_INGOT, 3, Material.SADDLE, 1);
                add(offers, Material.GOLD_INGOT, 1, Material.COAL, 64);
                break;
            case "normal2":
                add(offers, Material.GOLD_INGOT, 5, Material.COW_SPAWN_EGG, 1);
                add(offers, Material.GOLD_INGOT, 10, Material.HORSE_SPAWN_EGG, 1);
                add(offers, Material.GOLD_INGOT, 4, Material.SPLASH_POTION, 1);
                add(offers, Material.GOLD_INGOT, 3, Material.RED_CONCRETE_POWDER, 64);
                add(offers, Material.GOLD_BLOCK, 1, Material.YELLOW_CONCRETE_POWDER, 5);
                add(offers, Material.OBSIDIAN, 64, Material.GOLD_INGOT, 3);
                add(offers, Material.RED_CONCRETE_POWDER, 64, Material.GOLD_INGOT, 2);
                add(offers, Material.YELLOW_CONCRETE_POWDER, 4, Material.GOLD_INGOT, 4);
                break;
            case "lucastro":
                add(offers, Material.GOLD_INGOT, 1, Material.BOW, 1);
                add(offers, Material.GOLD_INGOT, 1, Material.OBSIDIAN, 16);
                add(offers, Material.GOLD_BLOCK, 3, Material.DIAMOND, 3);
                add(offers, Material.GOLD_INGOT, 3, Material.SADDLE, 1);
                add(offers, Material.GOLD_INGOT, 1, Material.COAL, 64);
                add(offers, Material.GOLD_INGOT, 10, Material.HORSE_SPAWN_EGG, 1);
                add(offers, Material.GOLD_INGOT, 5, Material.COW_SPAWN_EGG, 1);
                add(offers, Material.GOLD_INGOT, 5, Material.SPLASH_POTION, 1);
                break;
            case "ochsfurth":
                add(offers, Material.GOLD_INGOT, 1, Material.BOW, 1);
                add(offers, Material.GOLD_INGOT, 1, Material.OBSIDIAN, 16);
                add(offers, Material.GOLD_BLOCK, 3, Material.DIAMOND, 3);
                add(offers, Material.GOLD_INGOT, 3, Material.SADDLE, 1);
                add(offers, Material.GOLD_INGOT, 1, Material.COAL, 64);
                add(offers, Material.GOLD_INGOT, 10, Material.HORSE_SPAWN_EGG, 1);
                add(offers, Material.GOLD_INGOT, 5, Material.COW_SPAWN_EGG, 1);
                add(offers, Material.GOLD_INGOT, 3, Material.SPLASH_POTION, 1);
                break;
        }
        return offers;
    }

    private static void add(List<TradeOfferEntry> list, Material m1, int c1, Material m2, int c2) {
        MerchantRecipe recipe = new MerchantRecipe(new ItemStack(m2, c2), 999999);
        recipe.addIngredient(new ItemStack(m1, c1));
        list.add(new TradeOfferEntry(recipe));
    }
}