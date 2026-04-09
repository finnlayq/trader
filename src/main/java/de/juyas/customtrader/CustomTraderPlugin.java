package de.juyas.customtrader;

import de.juyas.customtrader.citizens.CitizensHandler;
import de.juyas.customtrader.command.*;
import de.juyas.customtrader.command.item.*;
import de.juyas.customtrader.listener.DeletionListener;
import de.juyas.utils.PaperUtilLog;
import de.juyas.utils.api.Runner;
import de.juyas.utils.api.command.CommandGroup;
import de.juyas.utils.api.number.BukkitTimeUnit;
import de.juyas.utils.api.updater.UpdatablePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CustomTraderPlugin extends JavaPlugin implements UpdatablePlugin
{

    public static final String DATABASE_SCHEMA = "Traders";

    @Getter
    private static CustomTraderPlugin instance;

    @Getter
    private TraderManager manager;

    @Override
    public void onEnable()
    {
        instance = this;
        manager = new TraderManager();
        //load and spawn them all on success - after 10 seconds to ensure that the server is fully started
        start( 10 );
        PaperUtilLog.pluginAuthorMessage( this, "Juyas" );
    }

    @Override
    public void onDisable()
    {
        stop();
    }

    public void reload()
    {
        info( "reloading Traders" );
        stop();
        start( 3 );
    }

    private void start( int delaySeconds )
    {
        boolean citizens = Bukkit.getPluginManager().isPluginEnabled( "Citizens" );
        if ( citizens )
            CitizensHandler.init();
        //load and spawn them all on success
        if ( manager.load( citizens ) )
        {
            postEnableDelay( manager::spawn, delaySeconds );
            info( "Traders loaded. Attempt to spawn them..." );
        }
        else warn( "Could not load traders!" );
        loadCommands();
        Bukkit.getPluginManager().registerEvents( new DeletionListener(), this );
    }

    private void loadCommands()
    {
        CommandGroup group = new CommandGroup( "trader" );
        group.addSubCommand( new CreateTrader() )
                .addSubCommand( new SpawnAll() )
                .addSubCommand( new WipeAll() )
                .addSubCommand( new ChangeProfession() )
                .addSubCommand( new ChangeType() )
                .addSubCommand( new ChangeName() )
                .addSubCommand( new ChangeCooldown() )
                .addSubCommand( new ChangeSkin() )
                .addSubCommand( new AdjustTrader() )
                .addSubCommand( new ShowInfo() )
                .addSubCommand( new Reload() )
                .addSubCommand( new ToggleAnimation() )
                .addSubCommand( new MoveTrader() )
                .addSubCommand( new RemoveTrader() )
                .addSubCommand( new AddRawTrade() )
                .addSubCommand( new AddTrade() )
                .addSubCommand( new SaveAll() )
                .addSubCommand( new DeleteTrade() )
                .addSubCommand( new DefaultTrades() );
        CommandGroup admin = new CommandGroup( "admin" );
        CommandGroup item = new CommandGroup( "item" );
        group.addSubCommand( admin );
        admin.addSubCommand( item );
        item.addSubCommand( new ChangeItemName() )
                .addSubCommand( new AddLoreLine() )
                .addSubCommand( new EmptyLine() )
                .addSubCommand( new RemoveLoreLine() );
        if ( group.register() )
            info( "Commands registered" );
        else warn( "Commands could not be registered correctly" );

    }

    private void stop()
    {
        if ( Bukkit.getPluginManager().isPluginEnabled( "Citizens" ) )
            CitizensHandler.deregister();
        manager.save();
        manager.wipe();
        HandlerList.unregisterAll( this );
        Bukkit.getScheduler().cancelTasks( this );
    }

    public void warn( String text )
    {
        getLogger().log( Level.WARNING, text );
    }

    public void info( String text )
    {
        getLogger().log( Level.INFO, text );
    }

    private void postEnableDelay( Runnable runnable, int delaySeconds )
    {
        Runner.task( runnable ).delayed( BukkitTimeUnit.SECONDS.toTicks( delaySeconds ) ).run( this );
    }

}
