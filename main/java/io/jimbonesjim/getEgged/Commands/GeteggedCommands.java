package io.jimbonesjim.getEgged.Commands;

import io.jimbonesjim.getEgged.Managers.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getPlayer;

public class GeteggedCommands implements CommandExecutor {

    private final DataManager DATAMANAGER;
    private final JavaPlugin PLUGIN;

    public GeteggedCommands(JavaPlugin plugin, DataManager dataManager) {
        PLUGIN = plugin;
        DATAMANAGER = dataManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0){
            sender.sendMessage(Component.text("usage: /getegged <get/reload/give>"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                //checks if sender has permission for reload command
                if (!sender.hasPermission("getegged.reload")){
                    sender.sendMessage(Component.text("You do not have permission to use this command").color(NamedTextColor.RED));
                    return true;
                }
                PLUGIN.reloadConfig();
                sender.sendMessage(Component.text("[GetEgged] Config reloaded!").color(NamedTextColor.GREEN));
                break;
            case "get":
                if (!(sender instanceof Player p)){
                    sender.sendMessage(Component.text("Only players can execute this command!"));
                    return true;
                }
                //Checks if player has permission for get command
                if (!p.hasPermission("getegged.get")){
                    p.sendMessage(Component.text("You are not allowed to use this command!").color(NamedTextColor.RED));
                    return true;
                }
                p.give(DATAMANAGER.createCaptureItem());
                break;
            case "give":
                if (!sender.hasPermission("getegged.give")){
                    sender.sendMessage(Component.text("You do not have permission to use this command").color(NamedTextColor.RED));
                    return true;
                }
                if (args.length != 2){
                    sender.sendMessage(Component.text("Usage: /getegged give <player>"));
                    return true;
                }
                Player target = getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(Component.text("Player " + args[1] + " not found!").color(NamedTextColor.RED));
                    return true;
                }
                target.give(DATAMANAGER.createCaptureItem());
                sender.sendMessage(Component.text("Gave a Capture Tool to " + target.getName()).color(NamedTextColor.GREEN));
                break;
            default:
                sender.sendMessage(Component.text("usage: /getegged <get/reload/give>"));
                break;
        }
        return true;
    }
}