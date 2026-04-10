package de.juyas.customtrader.api;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Presets {

    private static FileConfiguration config;

    public static void load() {
        File folder = CustomTraderPlugin.getInstance().getDataFolder();
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, "presets.yml");
        if (!file.exists()) {
            CustomTraderPlugin.getInstance().saveResource("presets.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static List<TradeOfferEntry> getPreset(String name) {
        List<TradeOfferEntry> offers = new ArrayList<>();
        if (config == null) return offers;

        // Versuche den Key direkt oder unter "presets." zu finden
        Object rawData = config.get(name);
        if (rawData == null) rawData = config.get("presets." + name);

        if (rawData instanceof List<?>) {
            List<?> list = (List<?>) rawData;
            for (Object obj : list) {
                String line = String.valueOf(obj);
                try {
                    // Erkennt sowohl "->" als auch ">"
                    String separator = line.contains("->") ? "->" : ">";
                    String[] parts = line.split(separator);

                    String[] buyPart = parts[0].trim().split(":");
                    String[] sellPart = parts[1].trim().split(":");

                    Material m1 = Material.valueOf(buyPart[0].toUpperCase());
                    int a1 = Integer.parseInt(buyPart[1]);
                    Material m2 = Material.valueOf(sellPart[0].toUpperCase());
                    int a2 = Integer.parseInt(sellPart[1]);

                    MerchantRecipe recipe = new MerchantRecipe(new ItemStack(m2, a2), 999999);
                    recipe.addIngredient(new ItemStack(m1, a1));
                    offers.add(new TradeOfferEntry(recipe));
                } catch (Exception e) {
                    CustomTraderPlugin.getInstance().getLogger().warning("Konnte Zeile im Preset nicht lesen: " + line);
                }
            }
        }
        return offers;
    }

    public static Set<String> getPresetNames() {
        if (config == null) return Set.of();

        // Falls die Datei eine "presets:" Sektion hat
        if (config.isConfigurationSection("presets")) {
            return config.getConfigurationSection("presets").getKeys(false);
        }

        // Ansonsten alle Keys auf oberster Ebene (außer dem Namen der Datei selbst)
        return config.getKeys(false).stream()
                .filter(k -> !k.equalsIgnoreCase("presets"))
                .collect(Collectors.toSet());
    }
}