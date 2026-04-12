package de.juyas.customtrader.api;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Presets {

    private static final Map<String, List<TradeOfferEntry>> PRESET_MAP = new HashMap<>();

    public static void load() {
        PRESET_MAP.clear();
        File file = new File(CustomTraderPlugin.getInstance().getDataFolder(), "presets.yml");
        if (!file.exists()) {
            CustomTraderPlugin.getInstance().saveResource("presets.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection targetSection = config.isConfigurationSection("presets") ? config.getConfigurationSection("presets") : config;

        for (String key : targetSection.getKeys(false)) {
            if (key.equalsIgnoreCase("presets")) continue;

            List<String> lines = targetSection.getStringList(key);
            if (lines == null || lines.isEmpty()) continue;

            List<TradeOfferEntry> offers = new ArrayList<>();
            for (String line : lines) {
                if (!line.contains(">")) continue;
                try {
                    String[] side = line.split(">");
                    ItemStack buy = parseItem(side[0]);
                    ItemStack sell = parseItem(side[1]);

                    MerchantRecipe recipe = new MerchantRecipe(sell, 999999);
                    recipe.addIngredient(buy);
                    recipe.setExperienceReward(false);
                    offers.add(new TradeOfferEntry(recipe));
                } catch (Exception e) {
                    Bukkit.getLogger().warning("[CustomTrader] Fehler in Zeile: " + line);
                }
            }
            if (!offers.isEmpty()) {
                PRESET_MAP.put(key.toLowerCase(), offers);
            }
        }
        Bukkit.getLogger().info("[CustomTrader] Erfolgreich geladene Presets: " + String.join(", ", PRESET_MAP.keySet()));
    }

    private static ItemStack parseItem(String input) {
        try {
            String[] parts = input.split(":");
            String matName = parts[0].replaceAll("[^a-zA-Z_]", "").toUpperCase();
            Material material = Material.matchMaterial(matName);

            if (material == null) return new ItemStack(Material.BARRIER, 1);

            int amount = 1;

            // ---- POTION LOGIK (Legacy Translation) ----
            if (matName.contains("POTION") && parts.length == 3) {
                String amountStr = parts[2].replaceAll("[^0-9]", "");
                if (!amountStr.isEmpty()) amount = Integer.parseInt(amountStr);

                ItemStack item = new ItemStack(material, amount);
                PotionMeta meta = (PotionMeta) item.getItemMeta();

                if (meta != null) {
                    String pType = parts[1].toLowerCase();
                    if (pType.contains("poison")) {
                        try {
                            // Alte API: PotionType.POISON, extended=false, upgraded(Stufe II)=true
                            meta.setBasePotionData(new PotionData(PotionType.POISON, false, true));
                            Bukkit.getLogger().info("[CustomTrader] Legacy PotionData angewendet für Gift II!");
                        } catch (Exception e) {
                            Bukkit.getLogger().warning("[CustomTrader] Konnte Legacy Potion nicht setzen.");
                        }
                    }
                    item.setItemMeta(meta);
                }
                return item;
            }

            // ---- STANDARD ITEM LOGIK ----
            if (parts.length >= 2) {
                String amountStr = parts[parts.length - 1].replaceAll("[^0-9]", "");
                if (!amountStr.isEmpty()) {
                    amount = Integer.parseInt(amountStr);
                }
            }

            return new ItemStack(material, amount);

        } catch (Exception e) {
            return new ItemStack(Material.BARRIER, 1);
        }
    }

    public static List<TradeOfferEntry> getPreset(String name) { return PRESET_MAP.get(name.toLowerCase()); }
    public static List<String> getPresetNames() { return new ArrayList<>(PRESET_MAP.keySet()); }
}