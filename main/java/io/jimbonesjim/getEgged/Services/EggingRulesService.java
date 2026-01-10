package io.jimbonesjim.getEgged.Services;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Rules.EntityCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.*;

public class EggingRulesService {

    private final ConfigManager configManager;
    private final EntityCategoryResolver entityCategoryResolver;
    private final WorldguardService worldguardService;
    private final GriefPreventionService griefPreventionService;

    public EggingRulesService(ConfigManager configManager, EntityCategoryResolver entityCategoryResolver, WorldguardService worldguardService, GriefPreventionService griefPreventionService) {
        this.configManager = configManager;
        this.entityCategoryResolver = entityCategoryResolver;
        this.worldguardService = worldguardService;
        this.griefPreventionService = griefPreventionService;
    }

    public RuleResult canEgg(Player player, Entity entity) {

        if (entity instanceof Tameable te){
            boolean allowOthers = configManager.getEggTamed();

            if (te.isTamed() && te.getOwnerUniqueId() != null &&
                    !player.getUniqueId().equals(te.getOwnerUniqueId()) &&
                    !allowOthers && !player.hasPermission("getegged.tamed")){
                Component msg = MiniMessage.miniMessage().deserialize("<red>You cannot egg someone else's <dark_red><entity>",
                        Placeholder.component("entity", Component.translatable(entity.getType().translationKey())));
                return RuleResult.fail(msg);
            }
        }

        EntityCategory category = entityCategoryResolver.resolve(entity);

        if (!hasPermission(player, category.permission(), entity.getType())) return deny(entity);

        if (!worldguardService.canEggMob(player, BukkitAdapter.adapt(entity.getLocation()))) return RuleResult.fail(null);

        if (!griefPreventionService.canBreak(player, entity.getLocation())) return RuleResult.fail(null);

        return RuleResult.ok();
    }

    private boolean hasPermission(Player p, String base, EntityType etype){
        return p.hasPermission(base + etype.name()) || p.hasPermission(base + "*");
    }

    private RuleResult deny(Entity entity){
        Component msg = MiniMessage.miniMessage().deserialize("<red>You do not have permission to egg a <dark_red><entity>",
                Placeholder.component("entity", Component.translatable(entity.getType().translationKey())));
        return RuleResult.fail(msg);
    }
}