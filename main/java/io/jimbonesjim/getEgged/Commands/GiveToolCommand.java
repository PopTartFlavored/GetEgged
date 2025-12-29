package io.jimbonesjim.getEgged.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.jimbonesjim.getEgged.Managers.DataManager.CAPTURE_TOOL;

public class GiveToolCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public GiveToolCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        //Checks if sender is a player
        if (!(sender instanceof Player p)){
            sender.sendMessage(Component.text("Only players can execute this command!"));
            return true;
        }
        //Checks for correct command usage
        if (args.length != 1 || !args[0].equalsIgnoreCase("get")) {
            sender.sendMessage(Component.text("usage: /getegged get"));
            return true;
        }
        //Checks if player has permission
        if (!p.hasPermission("getegged.get")){
            p.sendMessage(Component.text("You are not allowed to use this command!").color(NamedTextColor.RED));
            return true;
        }
        ItemStack capture_tool;
        //Checks for material type in config and sets it to default if not set.
        Material mat = Material.getMaterial(plugin.getConfig().getString("capture-item.material"));
        if (!plugin.getConfig().contains("capture-item.material") || plugin.getConfig().get("capture-item.material") == null || mat == null) {
            capture_tool = new ItemStack(Material.STICK);
        } else {
            capture_tool = new ItemStack(mat);
        }
        ItemMeta meta = capture_tool.getItemMeta();
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        PDC.set(CAPTURE_TOOL, PersistentDataType.BOOLEAN, true);
        meta.setEnchantmentGlintOverride(true);
        //Checks for name in config and sets it to default if not set.
        if (!plugin.getConfig().contains("capture-item.name") || plugin.getConfig().get("capture-item.name") == null){
            meta.customName(Component.text("Mob Capture Tool"));
        } else {
            meta.customName(Component.text(plugin.getConfig().get("capture-item.name").toString()));
        }
        //Checks for lore in config and sets it to default if not set.
        List<Component> lore = new ArrayList<>();
        if (!plugin.getConfig().contains("capture-item.lore") || plugin.getConfig().get("capture-item.lore") == null) {
            lore.add(Component.text("Right click me on a mob to capture it!").color(NamedTextColor.RED));
        } else {
            lore.add(Component.text(plugin.getConfig().getString("capture-item.lore")).color(NamedTextColor.YELLOW));
        }
        //sets lore and item meta then gives item to player
        meta.lore(lore);
        capture_tool.setItemMeta(meta);
        p.give(capture_tool);
        return true;
    }
}
