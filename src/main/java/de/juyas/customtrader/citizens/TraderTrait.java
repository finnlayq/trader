package de.juyas.customtrader.citizens;

import de.juyas.customtrader.api.TradeOffer;
import de.juyas.customtrader.api.Trader;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Merchant;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public class TraderTrait extends Trait
{

    private final Trader traderData;

    private final Merchant merchant;

    public TraderTrait( Trader traderData )
    {
        super( "active_trader" );
        this.traderData = traderData;
        this.merchant = Bukkit.createMerchant( Component.empty() );
        merchant.setRecipes( traderData.offers().stream().map( TradeOffer::recipe ).toList() );
    }

    public void reset()
    {
        merchant.setRecipes( traderData.offers().stream().map( TradeOffer::recipe ).toList() );
    }

    @EventHandler
    public void click( NPCRightClickEvent event )
    {
        if ( this.npc != event.getNPC() ) return;
        event.getClicker().openMerchant( this.merchant, false );
    }

}