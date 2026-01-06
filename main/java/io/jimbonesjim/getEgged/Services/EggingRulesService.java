package io.jimbonesjim.getEgged.Services;

import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Rules.EntityCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.*;

public class EggingRulesService {

    private final ConfigManager configManager;
    private final EntityCategoryResolver entityCategoryResolver;

    public EggingRulesService(ConfigManager configManager, EntityCategoryResolver entityCategoryResolver) {
        this.configManager = configManager;
        this.entityCategoryResolver = entityCategoryResolver;
    }

    public RuleResult canEgg(Player player, Entity entity) {

        if (entity instanceof Tameable te){
            boolean allowOthers = configManager.getEggTamed();

            if (te.isTamed() && te.getOwnerUniqueId() != null &&
                    !player.getUniqueId().equals(te.getOwnerUniqueId()) &&
                    !allowOthers && !player.hasPermission("getegged.tamed")){
                return RuleResult.fail(Component.text("You cannot egg someone else's ")
                        .append(Component.text(entity.getType().name()))
                        .color(NamedTextColor.RED));
            }
        }

        EntityCategory category = entityCategoryResolver.resolve(entity);

        if (!hasPermission(player, category.permission(), entity.getType())) return deny(entity);

        return RuleResult.ok();
    }

    private boolean hasPermission(Player p, String base, EntityType etype){
        return p.hasPermission(base + etype.name()) || p.hasPermission(base + "*");
    }

    private RuleResult deny(Entity entity){
        return RuleResult.fail(Component.text("You do not have permission to egg a ")
                .append(Component.text(entity.getType().name()))
                .color(NamedTextColor.RED));
    }
}