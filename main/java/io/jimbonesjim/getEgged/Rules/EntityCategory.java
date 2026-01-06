package io.jimbonesjim.getEgged.Rules;

public enum EntityCategory {

    ANIMAL("getegged.animal."),
    MONSTER("getegged.monster."),
    GOLEM("getegged.golem."),
    VILLAGER("getegged.villager."),
    DEFAULT("getegged.");

    private final String permissionBase;

    EntityCategory(String permissionBase) {
        this.permissionBase = permissionBase;
    }

    public String permission() {
        return permissionBase;
    }
}
