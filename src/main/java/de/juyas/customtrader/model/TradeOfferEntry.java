package de.juyas.customtrader.model;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TradeOffer;
import de.juyas.customtrader.api.TradeOfferData;
import de.juyas.utils.api.data.base.ModelBase;
import de.juyas.utils.api.data.model.*;
import de.juyas.utils.api.item.ItemSerialize;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Juyas
 * @version 22.11.2024
 * @since 22.11.2024
 */
@Getter
@Model(schema = CustomTraderPlugin.DATABASE_SCHEMA, table = "TradeOffers")
public final class TradeOfferEntry extends ModelBase implements TradeOffer
{

    @DatabaseColumn(name = "offer_id", sqlType = "bigint")
    private PrimaryKey primaryKey;

    @DatabaseColumn(name = "trader_id", sqlType = "bigint")
    private Column<Long> traderId;

    @DatabaseColumn(name = "offer_order", sqlType = "int")
    private Column<Integer> order;

    @DatabaseColumn(name = "ingredient_1", sqlType = "text")
    private Column<String> ingredient1;

    @DatabaseColumn(name = "ingredient_2", sqlType = "text")
    private Column<String> ingredient2;

    @DatabaseColumn(name = "offer_result", sqlType = "text")
    private Column<String> result;

    @DatabaseColumn(name = "trade_experience", sqlType = "boolean")
    private Column<Boolean> experience;

    @DatabaseColumn(name = "ignore_discounts", sqlType = "boolean")
    private Column<Boolean> ignoreDiscounts;

    @DatabaseColumn(name = "trade_capacity", sqlType = "int")
    private Column<Integer> tradeCapacity;

    void load( ResultSet resultSet ) throws SQLException
    {
        this.primaryKey.value().set( resultSet.getLong( this.primaryKey.settings().columnName() ) );
        this.traderId.value().init( resultSet.getLong( this.traderId.settings().columnName() ) );
        this.order.value().init( resultSet.getInt( this.order.settings().columnName() ) );
        this.ingredient1.value().init( resultSet.getString( this.ingredient1.settings().columnName() ) );
        this.ingredient2.value().init( resultSet.getString( this.ingredient2.settings().columnName() ) );
        this.result.value().init( resultSet.getString( this.result.settings().columnName() ) );
        this.experience.value().init( resultSet.getBoolean( this.experience.settings().columnName() ) );
        this.ignoreDiscounts.value().init( resultSet.getBoolean( this.ignoreDiscounts.settings().columnName() ) );
        this.tradeCapacity.value().init( resultSet.getInt( this.tradeCapacity.settings().columnName() ) );
    }

    @Override
    public long id()
    {
        return primaryKey.value().get().orElse( -1L );
    }

    @Override
    public ItemStack ingredient1()
    {
        if ( ingredient1.value().get().isEmpty() ) return null;
        return ItemSerialize.deserializeItem( ingredient1.value().get().get() );
    }

    @Override
    public ItemStack ingredient2()
    {
        if ( ingredient2.value().get().isEmpty() ) return null;
        return ItemSerialize.deserializeItem( ingredient2.value().get().get() );
    }

    @Override
    public ItemStack result()
    {
        if ( result.value().get().isEmpty() ) return null;
        return ItemSerialize.deserializeItem( result.value().get().get() );
    }

    @Override
    public int order()
    {
        return order.value().get().orElse( 0 );
    }

    @Override
    public int tradeCapacity()
    {
        return tradeCapacity.value().get().orElse( -1 );
    }

    @Override
    public boolean experienceReward()
    {
        return experience.value().get().orElse( true );
    }

    @Override
    public boolean ignoreDiscounts()
    {
        return ignoreDiscounts.value().get().orElse( false );
    }

    @Override
    public MerchantRecipe recipe()
    {
        ItemStack result = result();
        if ( result == null ) return null;
        MerchantRecipe recipe = new MerchantRecipe( result, 0, tradeCapacity(), experienceReward() );
        ItemStack item1 = ingredient1();
        ItemStack item2 = ingredient2();
        if ( item1 == null && item2 == null ) return null;
        if ( item1 != null && !item1.isEmpty() ) recipe.addIngredient( item1 );
        if ( item2 != null && !item2.isEmpty() ) recipe.addIngredient( item2 );
        recipe.setIgnoreDiscounts( ignoreDiscounts() );
        recipe.setExperienceReward( experienceReward() );
        recipe.setMaxUses( tradeCapacity() );
        return recipe;
    }

    public void from( TradeOfferData tradeOfferData )
    {
        this.getExperience().value().set( tradeOfferData.experienceReward() );
        this.getIgnoreDiscounts().value().set( tradeOfferData.ignoreDiscounts() );
        this.getResult().value().set( ItemSerialize.serializeItem( tradeOfferData.result() ) );
        String first = ItemSerialize.serializeItem( tradeOfferData.ingredient1() );
        String second = ItemSerialize.serializeItem( tradeOfferData.ingredient2() );
        this.getIngredient1().value().set( first );
        this.getIngredient2().value().set( second );
        this.getTradeCapacity().value().set( tradeOfferData.tradeCapacity() );
        this.getOrder().value().set( tradeOfferData.order() );
    }

}