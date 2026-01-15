package io.jimbonesjim.getEgged.Commands;

import io.jimbonesjim.getEgged.GetEgged;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Managers.EggingToolManager;
import io.jimbonesjim.getEgged.Services.RuleResult;
import io.jimbonesjim.getEgged.Services.VaultEconomyService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getPlayer;

public class GeteggedCommands implements TabExecutor {

    private final GetEgged getEgged;
    private final EggingToolManager eggingToolManager;
    private final ConfigManager configManager;
    private final VaultEconomyService vaultEconomyService;
    private static final String PERM_GET = "getegged.get";
    private static final String PERM_GIVE = "getegged.give";
    private static final String PERM_RELOAD = "getegged.reload";
    private final Component usageMessage = Component.text("Usage: /getegged <get | reload | give>", NamedTextColor.RED);
    private final Component permissionMessage = Component.text("You do not have permission to use this command", NamedTextColor.RED);
    private final Component prefix =  Component.text("[GetEgged] ", NamedTextColor.GOLD);

    public GeteggedCommands(GetEgged getegged, EggingToolManager eggingToolManager, ConfigManager configManager, VaultEconomyService vaultEconomyService) {
        this.getEgged = getegged;
        this.eggingToolManager = eggingToolManager;
        this.configManager = configManager;
        this.vaultEconomyService = vaultEconomyService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 0){
            sender.sendMessage(usageMessage);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                //checks if sender has permission for reload command
                if (!sender.hasPermission(PERM_RELOAD)){
                    sender.sendMessage(permissionMessage);
                    return true;
                }
                configManager.reloadConfig();
                getEgged.reloadMessages();
                sender.sendMessage(prefix.append(Component.text("Config reloaded!", NamedTextColor.GREEN)));
                break;
            case "get":
                if (!(sender instanceof Player p)){
                    sender.sendMessage(Component.text("This command can only be run by a player.", NamedTextColor.RED));
                    return true;
                }
                //Checks if player has permission for get command
                if (!p.hasPermission(PERM_GET)){
                    p.sendMessage(permissionMessage);
                    return true;
                }
                RuleResult result = vaultEconomyService.withdrawForTool(p);
                if (!result.allowed()){
                    p.sendMessage(result.message());
                    return true;
                }
                p.give(eggingToolManager.createTool());
                break;
            case "give":
                if (!sender.hasPermission(PERM_GIVE)){
                    sender.sendMessage(permissionMessage);
                    return true;
                }
                if (args.length != 2){
                    sender.sendMessage(Component.text("Usage: /getegged give <player>", NamedTextColor.RED));
                    return true;
                }
                Player target = getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(Component.text("Player " + args[1] + " not found!", NamedTextColor.RED));
                    return true;
                }
                target.give(eggingToolManager.createTool());
                sender.sendMessage(Component.text("Gave an Egging Tool to ", NamedTextColor.YELLOW)
                        .append(Component.text(target.getName(), NamedTextColor.DARK_PURPLE)));
                break;
            default:
                sender.sendMessage(usageMessage);
                break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String @NotNull [] args) {

        if (args.length == 1){
            List<String> subcommands = new ArrayList<>();
            if (sender.hasPermission(PERM_GET)) subcommands.add("get");
            if (sender.hasPermission(PERM_GIVE)) subcommands.add("give");
            if (sender.hasPermission(PERM_RELOAD)) subcommands.add("reload");

            String current = args[0].toLowerCase();
            subcommands.removeIf(sub -> !sub.startsWith(current));

            return subcommands;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give") && sender.hasPermission(PERM_GIVE)){
            List<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                playerNames.add(player.getName());
            }

            String current = args[1].toLowerCase();
            playerNames.removeIf(name -> !name.toLowerCase().startsWith(current));


            return playerNames;
        }
        return List.of();
    }
}