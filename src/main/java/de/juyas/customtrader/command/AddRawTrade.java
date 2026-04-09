package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TradeOfferData;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.TabCompletion;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

import static de.juyas.utils.api.command.TabCompletion.*;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class AddRawTrade extends AbstractSelectionCommand
{

    private final HashMap<UUID, TradeOfferData> newTrades;

    public AddRawTrade()
    {
        super( "addRawTrade" );
        setDescription( "Fügt einem Händler einen neuen Tauschhandel hinzu" );
        setSignature( "amount1", "ingredient1", "resultAmount", "resultItem", "amount2", "ingredient2" );
        setMinArgs( 4 );
        newTrades = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        int amount1 = tryParse( player, args[0] );
        int resultAmount = tryParse( player, args[2] );
        int amount2 = args.length == 6 ? tryParse( player, args[4] ) : 0;
        Material ingr1 = tryParseMat( player, args[1] );
        Material result = tryParseMat( player, args[3] );
        Material ingr2 = args.length == 6 ? tryParseMat( player, args[5] ) : null;

        if ( ingr1 == null || result == null || amount1 <= 0 || resultAmount <= 0
                || amount1 > ingr1.getMaxStackSize() || resultAmount > result.getMaxStackSize()
                || ( ingr2 != null && ( amount2 <= 0 || amount2 > ingr2.getMaxStackSize() ) ) )
        {
            Chat.send( player, "§cDas Tauschangebot kann so nicht funktionieren. Bitte überprüfe deine Angaben." );
            return;
        }

        TradeOfferData trade = new TradeOfferData( new ItemStack( ingr1, amount1 ), ingr2 != null ? new ItemStack( ingr2, amount2 ) : ItemStack.empty(), new ItemStack( result, resultAmount ),
                true, false, 0, 24 );
        newTrades.put( player.getUniqueId(), trade );
        putInQueue( player );
        Chat.send( player, "§aKlicke nun den Händler an, den du zwingen willst, diesen Tausch anzubieten." );
    }

    private int tryParse( Player player, String number )
    {
        try
        {
            int val = Integer.parseInt( number );
            if ( val <= 0 )
                Chat.send( player, "§cDie Zahl ist zu niedrig: §9" + number + " §c[min. 1]" );
            return val;
        }
        catch ( Exception e )
        {
            Chat.send( player, "§cDas ist keine gültige Zahl: §9" + number );
            return 0;
        }
    }

    private Material tryParseMat( Player player, String mat )
    {
        try
        {
            Material material = Material.matchMaterial( mat );
            if ( material == null || !material.isItem() )
            {
                Chat.send( player, "§cDas Item §9" + mat + " §cist nicht möglich für einen Tauschhandel" );
                return null;
            }
            return material;
        }
        catch ( Exception e )
        {
            Chat.send( player, "§cDas ist kein gültiges Item: §9" + mat );
            return null;
        }
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        TradeOfferData trade = newTrades.remove( player.getUniqueId() );
        info.trader().addOffer( trade );
        info.resetOffers();
        pullFromQueue( player );
        Chat.send( player, "§aDer Händler hat sich dazu entschieden neue Waren anzubieten." );
        return true;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        if ( args.length == 2 || args.length == 4 || args.length == 6 )
            return MATERIAL.concat( MATERIAL_KEYS );
        return NONE;
    }

}