package de.juyas.customtrader.command;

import de.juyas.customtrader.api.*;
import de.juyas.utils.api.command.Arguments;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.bukkit.Material.*;

/**
 * @author Juyas
 * @version 19.02.2024
 * @since 19.02.2024
 */
public class DefaultTrades extends AbstractSelectionCommand
{

    private HashMap<Villager.Profession, List<TradeOfferData>> defaultTrades;

    private record InfoData(boolean experience, boolean discount, int capacity) {}

    private final HashMap<UUID, InfoData> additionalInfo;

    public DefaultTrades()
    {
        super( "default", "def" );
        setDescription( "Setzt die Trades entsprechend seiner Profession" );
        setSignature( "capacity", "experience?", "discount?" );
        setMinArgs( 1 );
        genDefaults();
        additionalInfo = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Integer capacity = Arguments.validateRange( player, args[0], Integer::parseInt, 1, 100000 );
        boolean experience = Arguments.optBool( player, args, 1 ).orElse( true );
        boolean discount = Arguments.optBool( player, args, 2 ).orElse( true );
        if ( capacity == null || capacity <= 0 ) return;
        putInQueue( player );
        additionalInfo.put( player.getUniqueId(), new InfoData( experience, discount, capacity ) );
        Chat.send( player, "§aKlicke nun den Händler an, der die default trades bekommen soll." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        Villager.Profession profession = info.trader().getAttribute( TraderAttribute.VILLAGER_PROFESSION );
        if ( profession == Villager.Profession.NONE )
        {
            Chat.send( player, "§cDer Händler hat noch keine Profession." );
            return true;
        }
        pullFromQueue( player );
        InfoData infoData = additionalInfo.remove( player.getUniqueId() );
        info.trader().removeAllOffers();
        defaultTrades.getOrDefault( profession, new ArrayList<>() ).forEach( def -> info.trader().addOffer( def.modified( infoData.experience(), !infoData.discount(), infoData.capacity() ) ) );
        info.resetOffers();
        Chat.send( player, "§aDer Händler hat nun " + defaultTrades.size() + " Angebote entsprechend seiner Profession." );
        return true;
    }

    private TradeOfferData trade( Material in, int inAmount, Material out, int outAmount )
    {
        return new TradeOfferData( new ItemStack( in, inAmount ), ItemStack.empty(), new ItemStack( out, outAmount ), true, false, 0, 24 );
    }

    private TradeOfferData trade( Material in, int inAmount, Material in2, int inAmount2, Material out, int outAmount )
    {
        return new TradeOfferData( new ItemStack( in, inAmount ), new ItemStack( in2, inAmount2 ), new ItemStack( out, outAmount ), true, false, 0, 24 );
    }

    private void genDefaults()
    {
        defaultTrades = new HashMap<>();
        List<String> colorPrefixes = List.of(
                "WHITE", "LIGHT_GRAY", "GRAY", "BLACK",
                "BROWN", "RED", "ORANGE", "YELLOW",
                "LIME", "GREEN", "CYAN", "LIGHT_BLUE",
                "BLUE", "PURPLE", "MAGENTA", "PINK" );
        List<TradeOfferData> armorerTrades = List.of(
                trade( COAL, 15, EMERALD, 1 ),
                trade( IRON_INGOT, 4, EMERALD, 1 ),
                trade( LAVA_BUCKET, 1, EMERALD, 1 ),
                trade( DIAMOND, 1, EMERALD, 1 ),
                trade( EMERALD, 5, IRON_HELMET, 1 ),
                trade( EMERALD, 9, IRON_CHESTPLATE, 1 ),
                trade( EMERALD, 7, IRON_LEGGINGS, 1 ),
                trade( EMERALD, 4, IRON_BOOTS, 1 ),
                trade( EMERALD, 36, BELL, 1 ),
                trade( EMERALD, 1, CHAINMAIL_HELMET, 1 ),
                trade( EMERALD, 4, CHAINMAIL_CHESTPLATE, 1 ),
                trade( EMERALD, 3, CHAINMAIL_LEGGINGS, 1 ),
                trade( EMERALD, 1, CHAINMAIL_BOOTS, 1 ),
                trade( EMERALD, 5, SHIELD, 1 ) );
        List<TradeOfferData> butcherTrades = List.of(
                trade( CHICKEN, 14, EMERALD, 1 ),
                trade( BEEF, 10, EMERALD, 1 ),
                trade( PORKCHOP, 7, EMERALD, 1 ),
                trade( MUTTON, 7, EMERALD, 1 ),
                trade( RABBIT, 6, EMERALD, 1 ),
                trade( COAL, 16, EMERALD, 1 ),
                trade( DRIED_KELP_BLOCK, 10, EMERALD, 1 ),
                trade( SWEET_BERRIES, 10, EMERALD, 1 ),
                trade( EMERALD, 1, RABBIT_STEW, 1 ),
                trade( EMERALD, 1, COOKED_PORKCHOP, 5 ),
                trade( EMERALD, 1, COOKED_CHICKEN, 8 ) );
        List<TradeOfferData> cartographerTrades = new ArrayList<>( List.of(
                trade( PAPER, 24, EMERALD, 1 ),
                trade( GLASS_PANE, 11, EMERALD, 1 ),
                trade( COMPASS, 1, EMERALD, 1 ),
                trade( EMERALD, 7, MAP, 1 ),
                trade( EMERALD, 7, ITEM_FRAME, 1 ),
                trade( EMERALD, 8, GLOBE_BANNER_PATTERN, 1 ) ) );
        cartographerTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_BANNER" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 3, mat, 1 ) ).toList() );
        List<TradeOfferData> clericTrades = List.of(
                trade( ROTTEN_FLESH, 32, EMERALD, 1 ),
                trade( GOLD_INGOT, 3, EMERALD, 1 ),
                trade( RABBIT_FOOT, 2, EMERALD, 1 ),
                trade( TURTLE_SCUTE, 4, EMERALD, 1 ),
                trade( GLASS_BOTTLE, 9, EMERALD, 1 ),
                trade( NETHER_WART, 22, EMERALD, 1 ),
                trade( EMERALD, 1, REDSTONE, 2 ),
                trade( EMERALD, 1, LAPIS_LAZULI, 1 ),
                trade( EMERALD, 4, GLOWSTONE, 1 ),
                trade( EMERALD, 5, ENDER_PEARL, 1 ),
                trade( EMERALD, 3, EXPERIENCE_BOTTLE, 1 ) );
        List<TradeOfferData> farmerTrades = List.of(
                trade( WHEAT, 20, EMERALD, 1 ),
                trade( POTATO, 26, EMERALD, 1 ),
                trade( CARROT, 22, EMERALD, 1 ),
                trade( BEETROOT, 15, EMERALD, 1 ),
                trade( PUMPKIN, 6, EMERALD, 1 ),
                trade( MELON, 4, EMERALD, 1 ),
                trade( EMERALD, 1, BREAD, 6 ),
                trade( EMERALD, 1, PUMPKIN_PIE, 4 ),
                trade( EMERALD, 1, APPLE, 4 ),
                trade( EMERALD, 3, COOKIE, 18 ),
                trade( EMERALD, 1, CAKE, 1 ),
                trade( EMERALD, 3, GOLDEN_CARROT, 3 ),
                trade( EMERALD, 4, GLISTERING_MELON_SLICE, 3 ) );
        List<TradeOfferData> fishermanTrades = List.of(
                trade( STRING, 20, EMERALD, 1 ),
                trade( COAL, 10, EMERALD, 1 ),
                trade( COD, 15, EMERALD, 1 ),
                trade( SALMON, 13, EMERALD, 1 ),
                trade( TROPICAL_FISH, 6, EMERALD, 1 ),
                trade( PUFFERFISH, 4, EMERALD, 1 ),
                trade( BIRCH_BOAT, 1, EMERALD, 1 ),
                trade( ACACIA_BOAT, 1, EMERALD, 1 ),
                trade( DARK_OAK_BOAT, 1, EMERALD, 1 ),
                trade( JUNGLE_BOAT, 1, EMERALD, 1 ),
                trade( OAK_BOAT, 1, EMERALD, 1 ),
                trade( EMERALD, 2, CAMPFIRE, 1 ),
                trade( EMERALD, 3, COD_BUCKET, 1 ),
                trade( EMERALD, 1, COD, 6, COOKED_COD, 6 ),
                trade( EMERALD, 1, SALMON, 6, COOKED_SALMON, 6 ) );
        List<TradeOfferData> fletcherTrades = List.of(
                trade( STICK, 32, EMERALD, 1 ),
                trade( FLINT, 26, EMERALD, 1 ),
                trade( STRING, 14, EMERALD, 1 ),
                trade( FEATHER, 24, EMERALD, 1 ),
                trade( TRIPWIRE_HOOK, 8, EMERALD, 1 ),
                trade( EMERALD, 1, ARROW, 16 ),
                trade( EMERALD, 2, BOW, 1 ),
                trade( EMERALD, 3, CROSSBOW, 1 ),
                trade( EMERALD, 1, GRAVEL, 10, FLINT, 10 ) );
        List<TradeOfferData> leatherTrades = List.of(
                trade( LEATHER, 6, EMERALD, 1 ),
                trade( FLINT, 26, EMERALD, 1 ),
                trade( RABBIT_HIDE, 9, EMERALD, 1 ),
                trade( TURTLE_SCUTE, 4, EMERALD, 1 ),
                trade( EMERALD, 5, LEATHER_HELMET, 1 ),
                trade( EMERALD, 7, LEATHER_CHESTPLATE, 1 ),
                trade( EMERALD, 3, LEATHER_LEGGINGS, 1 ),
                trade( EMERALD, 4, LEATHER_BOOTS, 1 ),
                trade( EMERALD, 6, SADDLE, 1 ) );
        List<TradeOfferData> librarianTrades = List.of(
                trade( PAPER, 24, EMERALD, 1 ),
                trade( INK_SAC, 5, EMERALD, 1 ),
                trade( BOOK, 4, EMERALD, 1 ),
                trade( WRITABLE_BOOK, 2, EMERALD, 1 ),
                trade( EMERALD, 9, BOOKSHELF, 1 ),
                trade( EMERALD, 1, LANTERN, 1 ),
                trade( EMERALD, 1, GLASS, 4 ),
                trade( EMERALD, 5, CLOCK, 1 ),
                trade( EMERALD, 4, COMPASS, 1 ),
                trade( EMERALD, 20, NAME_TAG, 1 ) );
        List<TradeOfferData> masonTrades = new ArrayList<>( List.of(
                trade( CLAY_BALL, 10, EMERALD, 1 ),
                trade( STONE, 20, EMERALD, 1 ),
                trade( GRANITE, 16, EMERALD, 1 ),
                trade( ANDESITE, 16, EMERALD, 1 ),
                trade( DIORITE, 16, EMERALD, 1 ),
                trade( QUARTZ, 12, EMERALD, 1 ),
                trade( EMERALD, 1, BRICK, 10 ),
                trade( EMERALD, 1, CHISELED_STONE_BRICKS, 4 ),
                trade( EMERALD, 1, POLISHED_ANDESITE, 4 ),
                trade( EMERALD, 1, POLISHED_GRANITE, 4 ),
                trade( EMERALD, 1, POLISHED_DIORITE, 4 ),
                trade( EMERALD, 1, DRIPSTONE_BLOCK, 4 ),
                trade( EMERALD, 1, QUARTZ_PILLAR, 1 ),
                trade( EMERALD, 1, QUARTZ_BLOCK, 1 ) ) );
        masonTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_TERRACOTTA" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 1, mat, 1 ) ).toList() );
        masonTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_GLAZED_TERRACOTTA" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 1, mat, 1 ) ).toList() );
        List<TradeOfferData> shepherdTrades = new ArrayList<>( List.of(
                trade( WHITE_WOOL, 18, EMERALD, 1 ),
                trade( BROWN_WOOL, 18, EMERALD, 1 ),
                trade( BLACK_WOOL, 18, EMERALD, 1 ),
                trade( GRAY_WOOL, 18, EMERALD, 1 ),
                trade( EMERALD, 2, SHEARS, 1 ),
                trade( EMERALD, 2, PAINTING, 3 ) ) );
        shepherdTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_DYE" )
                .map( Material::getMaterial ).map( mat -> trade( mat, 12, EMERALD, 1 ) ).toList() );
        shepherdTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_BANNER" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 3, mat, 1 ) ).toList() );
        shepherdTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_BED" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 3, mat, 1 ) ).toList() );
        shepherdTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_WOOL" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 1, mat, 1 ) ).toList() );
        shepherdTrades.addAll( colorPrefixes.stream().map( elem -> elem + "_CARPET" )
                .map( Material::getMaterial ).map( mat -> trade( EMERALD, 1, mat, 1 ) ).toList() );
        List<TradeOfferData> toolsSmithTrades = List.of(
                trade( COAL, 15, EMERALD, 1 ),
                trade( IRON_INGOT, 4, EMERALD, 1 ),
                trade( FLINT, 30, EMERALD, 1 ),
                trade( DIAMOND, 1, EMERALD, 1 ),
                trade( EMERALD, 1, STONE_AXE, 1 ),
                trade( EMERALD, 1, STONE_SHOVEL, 1 ),
                trade( EMERALD, 1, STONE_PICKAXE, 1 ),
                trade( EMERALD, 1, STONE_HOE, 1 ),
                trade( EMERALD, 36, BELL, 1 ) );
        List<TradeOfferData> weaponSmithTrades = List.of(
                trade( COAL, 15, EMERALD, 1 ),
                trade( IRON_INGOT, 4, EMERALD, 1 ),
                trade( FLINT, 24, EMERALD, 1 ),
                trade( DIAMOND, 1, EMERALD, 1 ),
                trade( EMERALD, 3, IRON_AXE, 1 ),
                trade( EMERALD, 7, IRON_SWORD, 1 ),
                trade( EMERALD, 36, BELL, 1 ) );

        defaultTrades.put( Villager.Profession.ARMORER, ordered( armorerTrades ) );
        defaultTrades.put( Villager.Profession.BUTCHER, ordered( butcherTrades ) );
        defaultTrades.put( Villager.Profession.CARTOGRAPHER, ordered( cartographerTrades ) );
        defaultTrades.put( Villager.Profession.CLERIC, ordered( clericTrades ) );
        defaultTrades.put( Villager.Profession.FARMER, ordered( farmerTrades ) );
        defaultTrades.put( Villager.Profession.FISHERMAN, ordered( fishermanTrades ) );
        defaultTrades.put( Villager.Profession.FLETCHER, ordered( fletcherTrades ) );
        defaultTrades.put( Villager.Profession.LEATHERWORKER, ordered( leatherTrades ) );
        defaultTrades.put( Villager.Profession.LIBRARIAN, ordered( librarianTrades ) );
        defaultTrades.put( Villager.Profession.MASON, ordered( masonTrades ) );
        defaultTrades.put( Villager.Profession.SHEPHERD, ordered( shepherdTrades ) );
        defaultTrades.put( Villager.Profession.TOOLSMITH, ordered( toolsSmithTrades ) );
        defaultTrades.put( Villager.Profession.WEAPONSMITH, ordered( weaponSmithTrades ) );
    }

    private List<TradeOfferData> ordered( List<TradeOfferData> offers )
    {
        List<TradeOfferData> data = new ArrayList<>( offers.size() );
        for ( int i = 0; i < offers.size(); i++ )
        {
            TradeOfferData offer = offers.get( i );
            data.add( offer.reordered( i + 1 ) );
        }
        return data;
    }

}