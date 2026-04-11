package de.juyas.customtrader.model;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TradeOfferEntry implements ConfigurationSerializable {

    private ItemStack buy1;
    private ItemStack buy2;
    private ItemStack result;
    private int maxUses;

    public TradeOfferEntry(MerchantRecipe recipe) {
        this.result = recipe.getResult();
        this.buy1 = recipe.getIngredients().get(0);
        this.buy2 = recipe.getIngredients().size() > 1 ? recipe.getIngredients().get(1) : null;
        this.maxUses = recipe.getMaxUses();
    }

    public TradeOfferEntry(ItemStack buy1, ItemStack buy2, ItemStack result, int maxUses) {
        this.buy1 = buy1;
        this.buy2 = buy2;
        this.result = result;
        this.maxUses = maxUses;
    }

    public MerchantRecipe getRecipe() {
        MerchantRecipe recipe = new MerchantRecipe(result, maxUses);
        recipe.addIngredient(buy1);
        if (buy2 != null) recipe.addIngredient(buy2);
        recipe.setExperienceReward(false);
        return recipe;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("buy1", buy1);
        map.put("buy2", buy2);
        map.put("result", result);
        map.put("maxUses", maxUses);
        return map;
    }

    public static TradeOfferEntry deserialize(Map<String, Object> map) {
        ItemStack b1 = (ItemStack) map.get("buy1");
        ItemStack b2 = (ItemStack) map.get("buy2");
        ItemStack res = (ItemStack) map.get("result");
        int max = (int) map.get("maxUses");
        return new TradeOfferEntry(b1, b2, res, max);
    }
}