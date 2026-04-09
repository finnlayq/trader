package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TraderEntry;
import de.juyas.customtrader.villager.VillagerEnumMapper;
import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public interface TraderAttribute<T>
{

    TraderAttribute<Location> LOCATION = new TraderAttribute<>()
    {

        @Override
        public void setValue( TraderEntry entry, Location value )
        {
            entry.setLocation( value );
        }

        @Override
        public @NotNull Location getValue( TraderEntry entry )
        {
            return entry.location();
        }
    };
    TraderAttribute<Integer> REFRESH_SECONDS = new TraderAttribute<>()
    {
        @Override
        public void setValue( TraderEntry entry, Integer value )
        {
            entry.getRefreshSeconds().value().set( value );
        }

        @Override
        public @NotNull Integer getValue( TraderEntry entry )
        {
            return entry.getRefreshSeconds().value().get().orElse( 0 );
        }
    };
    TraderAttribute<Villager.Profession> VILLAGER_PROFESSION = new TraderAttribute<>()
    {
        @Override
        public void setValue( TraderEntry entry, Villager.Profession value )
        {
            entry.getProfession().value().set( VillagerEnumMapper.map( value ) );
        }

        @Override
        public Villager.@NotNull Profession getValue( TraderEntry entry )
        {
            return entry.profession();
        }
    };
    TraderAttribute<Villager.Type> VILLAGER_TYPE = new TraderAttribute<>()
    {
        @Override
        public void setValue( TraderEntry entry, Villager.Type value )
        {
            entry.getVillagerType().value().set( VillagerEnumMapper.map( value ) );
        }

        @Override
        public Villager.@NotNull Type getValue( TraderEntry entry )
        {
            return entry.villagerType();
        }
    };
    TraderAttribute<Boolean> PREFER_NPC = new TraderAttribute<>()
    {
        @Override
        public void setValue( TraderEntry entry, Boolean value )
        {
            entry.getPreferNpc().value().set( value );
        }

        @Override
        public @NotNull Boolean getValue( TraderEntry entry )
        {
            return entry.getPreferNpc().value().get().orElse( false );
        }
    };
    TraderAttribute<Boolean> ANIMATION = new TraderAttribute<>()
    {

        @Override
        public void setValue( TraderEntry entry, Boolean value )
        {
            entry.getAnimation().value().set( value );
        }

        @Override
        public @NotNull Boolean getValue( TraderEntry entry )
        {
            return entry.getAnimation().value().get().orElse( true );
        }
    };
    TraderAttribute<String[]> SKIN = new TraderAttribute<>()
    {
        @Override
        public void setValue( TraderEntry entry, String[] value )
        {
            entry.setSkin( value[0], value[1] );
        }

        @Override
        public String @NotNull [] getValue( TraderEntry entry )
        {
            String skinData = entry.getSkinData().value().get().orElse( "" );
            String skinSignature = entry.getSkinSignature().value().get().orElse( "" );
            return new String[] { skinSignature, skinData };
        }
    };
    TraderAttribute<String> NAME = new TraderAttribute<>()
    {
        @Override
        public void setValue( TraderEntry entry, String value )
        {
            entry.getNpcName().value().set( value );
        }

        @Override
        public @NotNull String getValue( TraderEntry entry )
        {
            return entry.getNpcName().value().get().orElse( "" );
        }
    };

    void setValue( TraderEntry entry, T value );

    @NotNull
    T getValue( TraderEntry entry );

}