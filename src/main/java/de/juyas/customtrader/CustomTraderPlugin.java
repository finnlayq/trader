package de.juyas.customtrader;

import de.juyas.customtrader.api.Presets;
import de.juyas.customtrader.command.MainCommand;
import de.juyas.customtrader.listener.DeletionListener;
import de.juyas.customtrader.listener.TraderDamageListener;
import de.juyas.customtrader.model.TradeOfferEntry;
import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomTraderPlugin extends JavaPlugin {

    private static CustomTraderPlugin instance;
    private TraderManager manager;

    public static CustomTraderPlugin getInstance() { return instance; }
    public TraderManager getManager() { return manager; }

    @Override
    public void onEnable() {
        // 1. Klassen für die Config registrieren
        ConfigurationSerialization.registerClass(TraderEntry.class);
        ConfigurationSerialization.registerClass(TradeOfferEntry.class);

        instance = this;

        // 2. Manager initialisieren
        manager = new TraderManager();

        // 3. Dynamische Presets laden
        Presets.load();

        // 4. Zentralen Command registrieren
        registerCommands();

        // 5. Listener registrieren
        Bukkit.getPluginManager().registerEvents(new DeletionListener(), this);
        Bukkit.getPluginManager().registerEvents(new TraderDamageListener(), this);

        // 6. Alle aktiven Trader in die Welt setzen
        manager.spawn();
    }

    private void registerCommands() {
        // Wir nutzen nur noch den MainCommand als zentralen Verteiler
        MainCommand mainCmd = new MainCommand();
        var cmd = getCommand("trader");

        if (cmd != null) {
            cmd.setExecutor(mainCmd);
            cmd.setTabCompleter(mainCmd);
        }
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.save();
        }
    }
}