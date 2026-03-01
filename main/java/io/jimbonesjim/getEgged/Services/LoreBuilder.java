package io.jimbonesjim.getEgged.Services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ColorableArmorMeta;

import java.util.ArrayList;
import java.util.List;

import static io.jimbonesjim.getEgged.utils.TextColorUtil.stringToTextColor;

public class LoreBuilder {

    public List<Component> build(Entity e) {
        List<Component> lore = new ArrayList<>();

        if (e.customName() != null) lore.add(createLabel("Name: ", e.getName()));

        if (e instanceof Ageable ae && !ae.isAdult()) lore.add(Component.text("Baby", NamedTextColor.GOLD));

        if (e instanceof Tameable te && te.getOwner() != null) {
            lore.add(Component.text("Tamed", NamedTextColor.GOLD));
            lore.add(createLabel("Owner: ", te.getOwner().getName()));
        }

        if (e instanceof Pig pig) {
            if (pig.hasSaddle()) lore.add(addSaddle());
            lore.add(createLabel("Variant: ", pig.getVariant().getKey().getKey()));
        }

        if (e instanceof Chicken chicken) lore.add(createLabel("Variant: ", chicken.getVariant().getKey().getKey()));

        if (e instanceof Sheep sheep){
            lore.add(Component.text("Color: ", NamedTextColor.WHITE)
                    .append(Component.text(String.valueOf(sheep.getColor()), stringToTextColor(sheep.getColor().name()))));
            if (sheep.isSheared()) lore.add(Component.text("Sheared", NamedTextColor.GOLD));
        }

        if (e instanceof Creeper creeper && creeper.isPowered()) lore.add(Component.text("Powered", NamedTextColor.GOLD));

        if (e instanceof Strider strider && strider.hasSaddle()) lore.add(addSaddle());

        if (e instanceof HappyGhast happyGhast && happyGhast.getEquipment().getItem(EquipmentSlot.BODY).getType().name().contains("_HARNESS")) {
            String harnessColor = happyGhast.getEquipment().getItem(EquipmentSlot.BODY).getType().name().replace("_HARNESS", "");
            lore.add(Component.text("Harnessed", stringToTextColor(harnessColor)));
        }

        if (e instanceof AbstractNautilus nautilus) {
            if (nautilus.getEquipment().getItem(EquipmentSlot.SADDLE).getType() ==  Material.SADDLE) {
                lore.add(addSaddle());
            }
            if (nautilus.getEquipment().getItem(EquipmentSlot.BODY).getType().name().contains("_NAUTILUS_ARMOR")) {
                String ArmorColor = nautilus.getEquipment().getItem(EquipmentSlot.BODY).getType().name().replace("_NAUTILUS_ARMOR", "");
                lore.add(Component.text("Armored", stringToTextColor(ArmorColor)));
            }
        }

        if (e instanceof Slime slime) lore.add(createLabel("Size: ", String.valueOf(slime.getSize())));

        if (e instanceof Fox fox) lore.add(createLabel("Type: ", fox.getFoxType().name()));

        if (e instanceof Rabbit rabbit) lore.add(createLabel("Variant: ", rabbit.getRabbitType().name()));

        if (e instanceof Axolotl axolotl) lore.add(createLabel("Variant: ", axolotl.getVariant().name()));

        if (e instanceof Frog frog) lore.add(createLabel("Variant: ", frog.getVariant().getKey().getKey()));

        if (e instanceof Wolf wolf) {
            if (wolf.getEquipment().getItem(EquipmentSlot.BODY).getType() == Material.WOLF_ARMOR) {
                if (wolf.getEquipment().getItem(EquipmentSlot.BODY).getItemMeta() instanceof ColorableArmorMeta armorMeta) {
                    lore.add(Component.text("Armored", TextColor.color(armorMeta.getColor().asRGB())));
                }
            }
            lore.add(createLabel("Variant: ", wolf.getVariant().getKey().getKey()));
            lore.add(createLabel("Sound Variant: ",  wolf.getSoundVariant().getKey().getKey()));
        }

        if (e instanceof Cat cat) lore.add(createLabel("Type: ", cat.getCatType().getKey().getKey()));

        if (e instanceof Parrot parrot) lore.add(createLabel("Variant: ", parrot.getVariant().name()));

        if (e instanceof Panda panda){
            lore.add(createLabel("Main Gene: ", panda.getMainGene().name()));
            lore.add(createLabel("Hidden Gene: ", panda.getHiddenGene().name()));
        }

        if (e instanceof TropicalFish tf){
            lore.add(createLabel("Pattern: ", tf.getPattern().name()));
            lore.add(Component.text("Color: ", NamedTextColor.WHITE)
                    .append(Component.text(tf.getBodyColor().toString(), stringToTextColor(tf.getBodyColor().name()))));
        }

        if (e instanceof Villager v){
            lore.add(createLabel("Type: ", v.getVillagerType().getKey().getKey()));
            if (v.getVillagerLevel() >= 2){
                lore.add(createLabel("Profession: ", v.getProfession().getKey().getKey()));
                lore.add(createLabel("Level: ", String.valueOf(v.getVillagerLevel())));
            }
        }

        if (e instanceof AbstractHorse ah && !(ah instanceof Llama)){
            double rawSpeed = ah.getAttribute(Attribute.MOVEMENT_SPEED).getValue();
            double rawJump = ah.getJumpStrength();
            double speed = rawSpeed * 43.2;
            double jump = -0.1817584952 * Math.pow(rawJump, 3)
                    + 3.689713992 * Math.pow(rawJump, 2)
                    + 2.128599134 * rawJump
                    - 0.343930367;

            lore.add(createLabel("Jump: ", String.format("%.2f", jump) + " blocks"));
            lore.add(createLabel("Speed: ", String.format("%.2f", speed) + " blocks/second"));
            if (ah instanceof ChestedHorse chestedHorse && chestedHorse.isCarryingChest()) lore.add(Component.text("Chested", NamedTextColor.GOLD));
            if (ah.getInventory().getSaddle() != null) lore.add(addSaddle());
        }

        if (e instanceof Horse horse) {
            if (horse.getInventory().getArmor() != null) {
                String armorType = horse.getInventory().getArmor().getType().name().replace("_HORSE_ARMOR", "");
                lore.add(Component.text("Armor: ", NamedTextColor.WHITE)
                        .append(Component.text(armorType, stringToTextColor(armorType))));
            }
            lore.add(createLabel("Color: ", horse.getColor().name()));
            lore.add(createLabel("Style: ", horse.getStyle().name()));
        }

        if (e instanceof Llama llama){
            lore.add(createLabel("Color: ", llama.getColor().name()));
            lore.add(createLabel("Strength: ", String.valueOf(llama.getStrength())));
        }

        if (!(lore.isEmpty())){
            lore.addFirst(Component.text("─────────────", NamedTextColor.GRAY));
            lore.addFirst(Component.text("─ GetEgged Data ─", NamedTextColor.GRAY).decorate(TextDecoration.BOLD));
        }

        return lore;
    }

    private Component addSaddle() {
        return Component.text("Saddled", NamedTextColor.GOLD);
    }

    private Component createLabel(String key, String value){
        return Component.text(key, NamedTextColor.WHITE)
                .append(Component.text(value, NamedTextColor.YELLOW));
    }
}