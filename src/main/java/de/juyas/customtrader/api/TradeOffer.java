package de.juyas.customtrader.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

/**
 * @author Juyas
 * @version 22.11.2024
 * @since 22.11.2024
 */
public interface TradeOffer
{

    long id();

    ItemStack ingredient1();

    ItemStack ingredient2();

    ItemStack result();

    boolean experienceReward();

    boolean ignoreDiscounts();

    int order();

    int tradeCapacity();

    MerchantRecipe recipe();

}