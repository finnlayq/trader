package de.juyas.customtrader.villager;

import com.google.common.collect.Lists;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Juyas
 * @version 02.10.2024
 * @since 02.10.2024
 */
public final class VillagerEnumMapper
{

    public static Villager.Type mapType( String typeName )
    {
        try
        {
            return Registry.VILLAGER_TYPE.get( Objects.requireNonNull( NamespacedKey.fromString( typeName.toLowerCase( Locale.ROOT ) ) ) );
        }
        catch ( Exception e )
        {
            return Villager.Type.PLAINS;
        }
    }

    public static Villager.Profession mapProfession( String professionName )
    {
        try
        {
            return Registry.VILLAGER_PROFESSION.get( Objects.requireNonNull( NamespacedKey.fromString( professionName.toLowerCase( Locale.ROOT ) ) ) );
        }
        catch ( Exception e )
        {
            return Villager.Profession.NONE;
        }
    }

    public static String map( @Nullable Villager.Profession profession )
    {
        if ( profession == null ) return null;
        return profession.getKey().getKey();
    }

    public static String map( @Nullable Villager.Type type )
    {
        if ( type == null ) return null;
        return type.getKey().getKey();
    }

    public static Villager.Type[] typeValues()
    {
        return Lists.newArrayList( Registry.VILLAGER_TYPE ).toArray( new Villager.Type[0] );
    }

    public static Villager.Profession[] professionValues()
    {
        return Lists.newArrayList( Registry.VILLAGER_PROFESSION ).toArray( new Villager.Profession[0] );
    }

}