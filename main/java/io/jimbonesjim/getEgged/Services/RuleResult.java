package io.jimbonesjim.getEgged.Services;

import net.kyori.adventure.text.Component;

public class RuleResult {

    private final boolean allowed;
    private final Component message;

    private RuleResult(boolean allowed, Component message) {
        this.allowed = allowed;
        this.message = message;
    }

    public static RuleResult ok() {
        return new RuleResult(true, null);
    }

    public static RuleResult fail(Component message) {
        return new RuleResult(false, message);
    }

    public boolean allowed() {
        return allowed;
    }

    public Component message() {
        return message;
    }
}