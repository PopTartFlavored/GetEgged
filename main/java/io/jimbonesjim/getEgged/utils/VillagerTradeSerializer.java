package io.jimbonesjim.getEgged.utils;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class VillagerTradeSerializer {

    private VillagerTradeSerializer() {}

    public static String serializeTrades(Villager villager){
        Base64.Encoder encoder = Base64.getEncoder();
        List<MerchantRecipe> recipes = villager.getRecipes();
        List<String> out = new ArrayList<>();

        for (MerchantRecipe recipe : recipes) {
            if (recipe.getIngredients().size() > 2) continue;
            // ingredient 1 (required)
            String ing1 = encoder.encodeToString(recipe.getIngredients().get(0).serializeAsBytes());
            // ingredient 2 (optional)
            String ing2 = recipe.getIngredients().size() > 1
                    ? encoder.encodeToString(recipe.getIngredients().get(1).serializeAsBytes())
                    : "";
            String result = encoder.encodeToString(recipe.getResult().serializeAsBytes());

            out.add(ing1 // trade's ingredient 1 item
                    + ":" + ing2 // trade's ingredient 2 item
                    + ":" + result // trade's result item
                    + ":" + recipe.getUses() // trade's uses
                    + ":" + recipe.getMaxUses() // trade's max uses
                    + ":" + recipe.getVillagerExperience() // experience villager gets from trade
                    + ":" + recipe.getPriceMultiplier() // trade's price multiplier
                    + ":" + recipe.getDemand() // trade's demand
                    + ":" + recipe.getSpecialPrice() // trade's special price
                    + ":" + recipe.hasExperienceReward()); // if trade has experience reward or not
        }
        return Base64.getEncoder().encodeToString(
                String.join(";", out).getBytes(StandardCharsets.UTF_8)
        );
    }

    public static List<MerchantRecipe> deserializeTrades(String data) {
        List<MerchantRecipe> list = new ArrayList<>();
        if (data == null || data.isEmpty()) return list;

        try {
            data = new String(
                    Base64.getDecoder().decode(data),
                    StandardCharsets.UTF_8
            );
        } catch (IllegalArgumentException e) {
            return list;
        }

        String[] recipes = data.split(";", -1);
        for (String rec : recipes) {
            String[] parts = rec.split(":", -1);
            if (parts.length < 10) continue;          // malformed

            ItemStack ing1;
            ItemStack ing2;
            ItemStack result;
            try {
                ing1 = ItemStack.deserializeBytes(Base64.getDecoder().decode(parts[0]));
                ing2 = parts[1].isEmpty() ? null : ItemStack.deserializeBytes(Base64.getDecoder().decode(parts[1]));
                result = ItemStack.deserializeBytes(Base64.getDecoder().decode(parts[2]));
            } catch (IllegalArgumentException e) {
                continue;
            }

            int uses, maxUses, xp, demand, special;
            float priceMul;
            boolean xpReward;
            try {
                uses      = Integer.parseInt(parts[3]);
                maxUses   = Integer.parseInt(parts[4]);
                xp        = Integer.parseInt(parts[5]);
                priceMul  = Float.parseFloat(parts[6]);
                demand    = Integer.parseInt(parts[7]);
                special   = Integer.parseInt(parts[8]);
                xpReward  = Boolean.parseBoolean(parts[9]);
            } catch (NumberFormatException e) {
                continue;
            }
            MerchantRecipe mr = new MerchantRecipe(result, uses, maxUses, xpReward, xp, priceMul, demand, special);
            mr.addIngredient(ing1);
            if (ing2 != null) mr.addIngredient(ing2);
            list.add(mr);
        }
        return list;
    }
}