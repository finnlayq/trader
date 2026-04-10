package de.juyas.customtrader.model;

import org.bukkit.inventory.MerchantRecipe;

public class TradeOfferEntry {

    private MerchantRecipe recipe;

    // Standard-Konstruktor
    public TradeOfferEntry(MerchantRecipe recipe) {
        this.recipe = recipe;
    }

    // DIESE METHODE SUCHT DER COMPILER:
    public MerchantRecipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(MerchantRecipe recipe) {
        this.recipe = recipe;
    }
}