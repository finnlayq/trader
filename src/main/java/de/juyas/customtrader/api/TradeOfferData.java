package de.juyas.customtrader.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public record TradeOfferData(ItemStack ingredient1, ItemStack ingredient2, ItemStack result, boolean experienceReward,
                             boolean ignoreDiscounts, int order, int tradeCapacity) implements TradeOffer
{

    @Override
    public long id()
    {
        return -1;
    }

    @Override
    public MerchantRecipe recipe()
    {
        MerchantRecipe recipe = new MerchantRecipe( result, 0, tradeCapacity, experienceReward );
        recipe.setIgnoreDiscounts( ignoreDiscounts );
        recipe.setIngredients( List.of( ingredient1, ingredient2 ) );
        return recipe;
    }

    public TradeOfferData reordered( int order )
    {
        return new TradeOfferData( ingredient1, ingredient2, result, experienceReward, ignoreDiscounts, order, tradeCapacity );
    }

    public TradeOfferData modified( boolean experienceReward, boolean ignoreDiscounts, int tradeCapacity )
    {
        return new TradeOfferData( ingredient1, ingredient2, result, experienceReward, ignoreDiscounts, order, tradeCapacity );
    }

    public static TradeOfferData fromRecipe( MerchantRecipe recipe )
    {
        ItemStack i1 = recipe.getIngredients().get( 0 );
        ItemStack i2 = recipe.getIngredients().size() > 1 ? recipe.getIngredients().get( 1 ) : ItemStack.empty();
        return new TradeOfferData( i1, i2, recipe.getResult(), recipe.hasExperienceReward(), recipe.shouldIgnoreDiscounts(), 0, recipe.getMaxUses() );
    }


}
