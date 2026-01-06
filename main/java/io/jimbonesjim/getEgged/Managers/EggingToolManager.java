package io.jimbonesjim.getEgged.Managers;

import io.jimbonesjim.getEgged.utils.InventoryUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static io.jimbonesjim.getEgged.Keys.GetEggedKeys.*;

public class EggingToolManager {

    private final ConfigManager configManager;

    public EggingToolManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public ItemStack createTool(){
        ItemStack egging_tool = new ItemStack(configManager.getToolMaterial());

        ItemMeta meta = egging_tool.getItemMeta();
        PersistentDataContainer PDC = meta.getPersistentDataContainer();

        PDC.set(EGGING_TOOL, PersistentDataType.BOOLEAN, true);
        if (configManager.getUsageMode() == ConfigManager.UsageMode.DURABILITY) {
            PDC.set(TOOL_DURABILITY, PersistentDataType.INTEGER, configManager.getMaxUses());
        }

        meta.setEnchantmentGlintOverride(configManager.getToolGlow());

        meta.customName(Component.text(configManager.getToolName()).color(NamedTextColor.YELLOW));

        meta.lore(updateLore(configManager.getMaxUses()));
        egging_tool.setItemMeta(meta);
        return egging_tool;
    }

    public boolean isTool(ItemMeta meta){
        return meta != null &&
                meta.getPersistentDataContainer().has(EGGING_TOOL, PersistentDataType.BOOLEAN);
    }

    public int getUses(ItemMeta meta) {
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        if (!PDC.has(TOOL_DURABILITY)) return 0;
        return PDC.get(TOOL_DURABILITY, PersistentDataType.INTEGER);
    }

    public void applyUsage(ItemStack tool, Player player) {
        if (configManager.getUsageMode() == ConfigManager.UsageMode.NONE) return;
        if (player.hasPermission("getegged.usagebypass")) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (configManager.getUsageMode() == ConfigManager.UsageMode.DURABILITY) {
            handleDurability(tool, player);
            return;
        }

        // CONSUME mode fallback
        InventoryUtil.removeOneFromMainHand(player, tool);
    }

    private void handleDurability(ItemStack tool, Player player) {
        ItemMeta meta = tool.getItemMeta();
        int uses = getUses(meta);

        if (uses > 1) {
            tool.setItemMeta(useTool(meta));
        } else {
            InventoryUtil.removeOneFromMainHand(player, tool);
        }
    }

    public ItemMeta useTool(ItemMeta meta) {
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        int toolUses = getUses(meta) - 1;
        PDC.set(TOOL_DURABILITY, PersistentDataType.INTEGER, toolUses);
        meta.lore(updateLore(toolUses));
        return meta;
    }

    private List<Component> updateLore(int toolUses) {
        List<Component> lore = new ArrayList<>();
        if (configManager.getUsageMode() == ConfigManager.UsageMode.DURABILITY) {
            lore.add(Component.text("Durability: " + toolUses + "/" + configManager.getMaxUses()));
        }
        lore.add(Component.text(configManager.getToolLore()).color(NamedTextColor.YELLOW));
        return lore;
    }
}