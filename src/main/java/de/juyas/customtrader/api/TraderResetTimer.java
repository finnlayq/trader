package de.juyas.customtrader.api;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.utils.api.Runner;
import de.juyas.utils.api.number.BukkitTimeUnit;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public final class TraderResetTimer
{

    private final TraderNPCHandler npcHandler;

    private BukkitTask task;

    public TraderResetTimer( TraderNPCHandler npcHandler )
    {
        this.npcHandler = npcHandler;
    }

    public long refreshRate()
    {
        long seconds = npcHandler.trader().getAttribute( TraderAttribute.REFRESH_SECONDS );
        return BukkitTimeUnit.SECONDS.toTicks( seconds );
    }

    private void reset()
    {
        npcHandler.resetOffers();
        if ( npcHandler.trader().getAttribute( TraderAttribute.ANIMATION ) )
            npcHandler.playAnimation();
    }

    public void start()
    {
        if ( task != null )
            task.cancel();
        long ticks = refreshRate();
        task = Runner.task( this::reset )
                .delayed( ticks )
                .repeating( ticks )
                .run( CustomTraderPlugin.getInstance() );
    }

    public void stop()
    {
        if ( task != null )
            task.cancel();
        task = null;
    }

}