package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TradeOffer;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.Try;
import de.juyas.utils.api.command.TabCompletion;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class DeleteTrade extends AbstractSelectionCommand
{

    private record Deletion(Integer index, String item) {}

    private HashMap<UUID, Deletion> deletions;

    public DeleteTrade()
    {
        super( "deleteTrade", "delTrade" );
        setDescription( "Löscht ein Tauschangebot eines Händlers.",
                "Gewählt wird mit dem Index oder dem ersten Item des Handels.",
                "Sollte es mehrere Angebote mit demselben ersten Item geben, werden alle gelöscht." );
        setSignature( "index/item" );
        setMinArgs( 1 );
        deletions = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Integer index;
        String item = null;

        index = Try.silent( () -> Integer.parseInt( args[0] ), null );

        if ( index == null )
        {
            item = Try.silent( () ->
            {
                Material material = Material.matchMaterial( args[0] );
                return material != null ? material.name() : null;
            }, null );
        }

        if ( index == null && item == null )
        {
            Chat.send( player, "§cAngegebener Wert \"" + args[0] + "\" ist weder eine Zahl noch ein gültiges Item" );
            return;
        }

        deletions.put( player.getUniqueId(), new Deletion( index, item ) );
        putInQueue( player );

        Chat.send( player, "§aKlicke nun den Händler an, der das Angebot entfernen soll." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        pullFromQueue( player );
        Deletion deletion = deletions.get( player.getUniqueId() );
        if ( deletion.index() != null )
        {
            int index = deletion.index();
            TradeOffer offer = info.trader().offers().get( index );
            info.trader().removeOffer( offer.id() );
            Chat.send( player, "§aTauschangebot §9" + deletion.index() + " §aentfernt." );
        }
        else if ( deletion.item() != null )
        {
            Material matched = Material.matchMaterial( deletion.item() );
            List<TradeOffer> forRemoval = info.trader().offers().stream().filter( trade -> trade.ingredient1().getType().equals( matched )
                    || trade.ingredient2().getType().equals( matched ) ).toList();
            if ( !forRemoval.isEmpty() )
            {
                forRemoval.forEach( offer -> info.trader().removeOffer( offer.id() ) );
                Chat.send( player, "§aTauschangebot(e) mit §9" + deletion.item() + " §aentfernt." );
            }
            else Chat.send( player, "§cEs gab kein Tauschangebot mit §9" + deletion.item() );
        }
        info.resetOffers();
        return true;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? TabCompletion.MATERIAL.concat( TabCompletion.MATERIAL_KEYS ) : TabCompletion.NONE;
    }

}