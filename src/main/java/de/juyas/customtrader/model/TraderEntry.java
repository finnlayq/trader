package de.juyas.customtrader.model;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.*;
import de.juyas.customtrader.villager.VillagerEnumMapper;
import de.juyas.utils.api.data.base.*;
import de.juyas.utils.api.data.model.*;
import de.juyas.utils.api.data.query.factory.SQLCondition;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 22.11.2024
 * @since 22.11.2024
 */
@Getter
@Model(schema = CustomTraderPlugin.DATABASE_SCHEMA, table = "Traders")
public final class TraderEntry extends ModelBase implements Trader
{

    @DatabaseColumn(name = "trader_id", sqlType = "bigint")
    private PrimaryKey primaryKey;

    @DatabaseColumn(name = "world_uuid", sqlType = "char(36)")
    private Column<String> world;

    @DatabaseColumn(name = "pos_x", sqlType = "double")
    private Column<Double> positionX;

    @DatabaseColumn(name = "pos_y", sqlType = "double")
    private Column<Double> positionY;

    @DatabaseColumn(name = "pos_z", sqlType = "double")
    private Column<Double> positionZ;

    @DatabaseColumn(name = "yaw", sqlType = "double")
    private Column<Float> yaw;

    @DatabaseColumn(name = "pitch", sqlType = "double")
    private Column<Float> pitch;

    @DatabaseColumn(name = "animation", sqlType = "boolean")
    private Column<Boolean> animation;

    @DatabaseColumn(name = "refresh_seconds", sqlType = "int")
    private Column<Integer> refreshSeconds;

    @DatabaseColumn(name = "villager_profession", sqlType = "varchar(16)")
    private Column<String> profession;

    @DatabaseColumn(name = "villager_type", sqlType = "varchar(16)")
    private Column<String> villagerType;

    @DatabaseColumn(name = "npc_preference", sqlType = "boolean")
    private Column<Boolean> preferNpc;

    @DatabaseColumn(name = "npc_has_skin", sqlType = "boolean")
    private Column<Boolean> hasNpcSkin;

    @DatabaseColumn(name = "npc_skin_data", sqlType = "text")
    private Column<String> skinData;

    @DatabaseColumn(name = "npc_skin_signature", sqlType = "text")
    private Column<String> skinSignature;

    @DatabaseColumn(name = "npc_name", sqlType = "varchar(255)")
    private Column<String> npcName;

    private final List<TradeOfferEntry> tradeOffers = new ArrayList<>();

    private void loadFrom( ResultSet resultSet ) throws SQLException
    {
        this.primaryKey.value().set( resultSet.getLong( this.primaryKey.settings().columnName() ) );
        this.world.value().set( resultSet.getString( this.world.settings().columnName() ) );
        this.positionX.value().set( resultSet.getDouble( this.positionX.settings().columnName() ) );
        this.positionY.value().set( resultSet.getDouble( this.positionY.settings().columnName() ) );
        this.positionZ.value().set( resultSet.getDouble( this.positionZ.settings().columnName() ) );
        this.yaw.value().set( resultSet.getFloat( this.yaw.settings().columnName() ) );
        this.pitch.value().set( resultSet.getFloat( this.pitch.settings().columnName() ) );
        this.animation.value().set( resultSet.getBoolean( this.animation.settings().columnName() ) );
        this.refreshSeconds.value().set( resultSet.getInt( this.refreshSeconds.settings().columnName() ) );
        this.profession.value().set( resultSet.getString( this.profession.settings().columnName() ) );
        this.villagerType.value().set( resultSet.getString( this.villagerType.settings().columnName() ) );
        this.preferNpc.value().set( resultSet.getBoolean( this.preferNpc.settings().columnName() ) );
        this.hasNpcSkin.value().set( resultSet.getBoolean( this.hasNpcSkin.settings().columnName() ) );
        this.skinData.value().set( resultSet.getString( this.skinData.settings().columnName() ) );
        this.skinSignature.value().set( resultSet.getString( this.skinSignature.settings().columnName() ) );
        this.npcName.value().set( resultSet.getString( this.npcName.settings().columnName() ) );
    }

    public List<TradeOfferEntry> getTradeOffers()
    {
        return Collections.unmodifiableList( tradeOffers );
    }

    public void applyDefaults()
    {
        villagerType.value().init( VillagerEnumMapper.map( Villager.Type.PLAINS ) );
        profession.value().init( VillagerEnumMapper.map( Villager.Profession.NONE ) );
        preferNpc.value().init( false );
        hasNpcSkin.value().init( false );
        skinData.value().init( "" );
        skinSignature.value().init( "" );
        npcName.value().init( "Händler" );
        refreshSeconds.value().init( 60 );
        animation.value().init( true );
    }

    public void from( Villager villager )
    {
        applyDefaults();
        villagerType.value().init( VillagerEnumMapper.map( villager.getVillagerType() ) );
        profession.value().init( VillagerEnumMapper.map( villager.getProfession() ) );
        setLocation( villager.getLocation(), false );
    }

    public void setSkin( String signature, String data )
    {
        skinData.value().set( data );
        skinSignature.value().set( signature );
        hasNpcSkin.value().set( true );
    }

    public void setLocation( Location location )
    {
        setLocation( location, true );
    }

    public void setLocation( Location location, boolean update )
    {
        if ( update )
        {
            this.world.value().set( location.getWorld().getUID().toString() );
            this.positionX.value().set( location.getX() );
            this.positionY.value().set( location.getY() );
            this.positionZ.value().set( location.getZ() );
            this.yaw.value().set( location.getYaw() );
            this.pitch.value().set( location.getPitch() );
        }
        else
        {
            this.world.value().init( location.getWorld().getUID().toString() );
            this.positionX.value().init( location.getX() );
            this.positionY.value().init( location.getY() );
            this.positionZ.value().init( location.getZ() );
            this.yaw.value().init( location.getYaw() );
            this.pitch.value().init( location.getPitch() );
        }
    }

    private void addTradeOffer( TradeOfferEntry tradeOffer )
    {
        if ( primaryKey.value().get().isEmpty() ) return;
        tradeOffer.getTraderId().value().set( primaryKey.value().get().get() );
        if ( tradeOffer.insert() )
        {
            tradeOffers.add( tradeOffer );
            tradeOffers.sort( Comparator.comparing( TradeOffer::order ) );
        }
    }

    private boolean removeTradeOffer( long tradeOfferId )
    {
        TradeOfferEntry entry = tradeOffers.stream().filter( offer -> offer.getPrimaryKey().value().get().isPresent()
                        && offer.getPrimaryKey().value().get().get() == tradeOfferId )
                .findFirst().orElse( null );
        if ( entry == null ) return false;
        return entry.delete() && tradeOffers.remove( entry );
    }

    private void deleteEntry()
    {
        tradeOffers.forEach( TradeOfferEntry::delete );
        this.delete();
    }

    private void loadTrades( QueryTarget target )
    {
        TradeOfferEntry dummy = ModelBase.generate( TradeOfferEntry.class );
        SQLCondition condition = SQLCondition.equals( dummy.getTraderId().settings().columnName(), String.valueOf( this.primaryKey.value().get().orElseThrow() ) );
        ModelQueries.selectAllRawConditionedTargeted( TradeOfferEntry.class, condition, result ->
        {
            List<TradeOfferEntry> entries = new ArrayList<>();
            while ( result.next() )
            {
                TradeOfferEntry tradeOffer = ModelBase.generate( TradeOfferEntry.class );
                tradeOffer.load( result );
                entries.add( tradeOffer );
            }
            tradeOffers.addAll( entries );
            tradeOffers.sort( Comparator.comparing( TradeOffer::order ) );
        }, target );
    }

    public static void loadEntries( Consumer<List<TraderEntry>> resultConsumer )
    {
        ModelQueries.selectAllRaw( TraderEntry.class, result ->
        {
            QueryTarget target = new QueryTargetBase();
            List<TraderEntry> entries = new ArrayList<>();
            while ( result.next() )
            {
                TraderEntry entry = ModelBase.generate( TraderEntry.class );
                entry.loadFrom( result );
                entry.loadTrades( target );
                entries.add( entry );
            }
            target.triggerOnceOnRelease( () -> resultConsumer.accept( entries ) );
        } );
    }

    @Override
    public Location location()
    {
        //check their existence
        if ( world.value().get().isEmpty() ) return null;
        if ( positionX.value().get().isEmpty() || positionY.value().get().isEmpty()
                || positionZ.value().get().isEmpty() ) return null;
        if ( yaw.value().get().isEmpty() || pitch.value().get().isEmpty() ) return null;
        UUID uuid = UUID.fromString( world.value().get().get() );
        World w = Bukkit.getWorld( uuid );
        double x = positionX.value().get().get();
        double y = positionY.value().get().get();
        double z = positionZ.value().get().get();
        float yw = yaw.value().get().get();
        float pt = pitch.value().get().get();
        return new Location( w, x, y, z, yw, pt );
    }

    @Override
    public <T> @NotNull T getAttribute( TraderAttribute<T> attribute )
    {
        return attribute.getValue( this );
    }

    @Override
    public <T> void setAttribute( TraderAttribute<T> attribute, T value )
    {
        attribute.setValue( this, value );
    }

    public Villager.Profession profession()
    {
        if ( profession.value().get().isEmpty() ) return Villager.Profession.NONE;
        return VillagerEnumMapper.mapProfession( profession.value().get().get() );
    }

    public Villager.Type villagerType()
    {
        if ( villagerType.value().get().isEmpty() ) return Villager.Type.PLAINS;
        return VillagerEnumMapper.mapType( villagerType.value().get().get() );
    }

    public List<TradeOffer> offers()
    {
        return new ArrayList<>( getTradeOffers() );
    }

    @Override
    public void addOffer( TradeOfferData offer )
    {
        TradeOfferEntry offerEntry = ModelBase.generate( TradeOfferEntry.class );
        offerEntry.from( offer );
        addTradeOffer( offerEntry );
    }

    @Override
    public boolean removeOffer( long id )
    {
        return removeTradeOffer( id );
    }

    @Override
    public boolean hasNPCSkin()
    {
        return hasNpcSkin.value().get().orElse( false );
    }

    @Override
    public void removeAllOffers()
    {
        tradeOffers.forEach( TradeOfferEntry::delete );
        tradeOffers.clear();
    }

    @Override
    public void deleteData()
    {
        deleteEntry();
    }

    @Override
    public void updateData()
    {
        getModel().columns().forEach( column -> column.value().update() );
    }

}