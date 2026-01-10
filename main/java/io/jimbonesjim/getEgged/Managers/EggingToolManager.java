package io.jimbonesjim.getEgged.Managers;

import io.jimbonesjim.getEgged.utils.InventoryUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        PDC.set(TOOL_MODE, PersistentDataType.STRING, configManager.getUsageMode().name());
        if (configManager.getUsageMode() == ConfigManager.UsageMode.DURABILITY) {
            PDC.set(TOOL_DURABILITY, PersistentDataType.INTEGER, configManager.getMaxUses());
            meta.setMaxStackSize(1);
        }

        meta.setEnchantmentGlintOverride(configManager.getToolGlow());

        meta.customName(Component.text(configManager.getToolName()).color(NamedTextColor.YELLOW));

        meta.lore(updateLore(configManager.getMaxUses(), configManager.getUsageMode()));
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
        if (player.hasPermission("getegged.usagebypass") || player.getGameMode() == GameMode.CREATIVE) return;
        if (!tool.getItemMeta().getPersistentDataContainer().has(TOOL_MODE, PersistentDataType.STRING)) {
            InventoryUtil.removeOneFromMainHand(player, tool);
            return;
        }
        ConfigManager.UsageMode toolMode = ConfigManager.UsageMode.valueOf(tool.getPersistentDataContainer().get(TOOL_MODE, PersistentDataType.STRING));
        switch (toolMode) {
            case NONE:
                break;
            case CONSUME:
                InventoryUtil.removeOneFromMainHand(player, tool);
                break;
            case DURABILITY:
                handleDurability(tool, player);
                break;
        }
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
        meta.lore(updateLore(toolUses, ConfigManager.UsageMode.DURABILITY));
        return meta;
    }

    private List<Component> updateLore(int toolUses, ConfigManager.UsageMode usageMode) {
        List<Component> lore = new ArrayList<>();
        switch (usageMode) {
            case NONE ->  lore.add(Component.text("Unlimited Uses",  NamedTextColor.GOLD));
            case CONSUME -> lore.add(Component.text("Consumed when used", NamedTextColor.GOLD));
            case DURABILITY -> {
                Component msg = MiniMessage.miniMessage().deserialize("<gold>Durability: <uses>/<maxuses>",
                        Placeholder.parsed("uses", String.valueOf(toolUses)),
                        Placeholder.parsed("maxuses", String.valueOf(configManager.getMaxUses())));
                lore.add(msg);
            }
        }
        lore.add(Component.text(configManager.getToolLore(), NamedTextColor.YELLOW));
        return lore;
    }
}