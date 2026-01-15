package io.jimbonesjim.getEgged.Services;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Managers.MessageManager;
import io.jimbonesjim.getEgged.Rules.EntityCategory;
import io.jimbonesjim.getEgged.utils.MiniMessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.*;

public class EggingRulesService {

    private final ConfigManager configManager;
    private final MessageManager messageManager;
    private final EntityCategoryResolver entityCategoryResolver;
    private final WorldguardService worldguardService;
    private final GriefPreventionService griefPreventionService;

    public EggingRulesService(ConfigManager configManager, MessageManager messageManager, EntityCategoryResolver entityCategoryResolver, WorldguardService worldguardService, GriefPreventionService griefPreventionService) {
        this.configManager = configManager;
        this.messageManager = messageManager;
        this.entityCategoryResolver = entityCategoryResolver;
        this.worldguardService = worldguardService;
        this.griefPreventionService = griefPreventionService;
    }

    public RuleResult canEgg(Player player, Entity entity) {

        if (!configManager.getBlacklist().isEmpty() && !player.hasPermission("getegged.blacklist.bypass") &&
                configManager.getBlacklist().stream().anyMatch(s -> s.equalsIgnoreCase(entity.getType().name()))) {
            return RuleResult.fail(MiniMessageUtil.createMessage(messageManager.getBlacklistedMessage(),
                    Placeholder.component("entity", Component.translatable(entity.getType().translationKey()))));
        }

        if (!player.isOp() && player.hasPermission("getegged.deny." + entity.getType().name())) {
            return RuleResult.fail(MiniMessageUtil.createMessage(messageManager.getNoPermissionMessage(),
                    Placeholder.component("entity", Component.translatable(entity.getType().translationKey()))));
        }

        if (entity instanceof Tameable te){
            boolean allowOthers = configManager.getEggTamed();

            if (te.isTamed() && te.getOwnerUniqueId() != null &&
                    !player.getUniqueId().equals(te.getOwnerUniqueId()) &&
                    !allowOthers && !player.hasPermission("getegged.tamed")){
                return RuleResult.fail(MiniMessageUtil.createMessage(messageManager.getTamedMessage(),
                        Placeholder.component("entity", Component.translatable(entity.getType().translationKey()))));
            }
        }

        EntityCategory category = entityCategoryResolver.resolve(entity);

        if (!hasPermission(player, category.permission(), entity.getType())) return RuleResult.fail(MiniMessageUtil.createMessage(messageManager.getNoPermissionMessage(),
                Placeholder.component("entity", Component.translatable(entity.getType().translationKey()))));

        if (!worldguardService.canEggMob(player, BukkitAdapter.adapt(entity.getLocation()))) return RuleResult.fail(null);

        if (!griefPreventionService.canBreak(player, entity.getLocation())) return RuleResult.fail(null);

        return RuleResult.ok();
    }

    private boolean hasPermission(Player p, String base, EntityType etype){
        return p.hasPermission(base + etype.name()) || p.hasPermission(base + "*");
    }
}