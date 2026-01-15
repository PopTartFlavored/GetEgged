package io.jimbonesjim.getEgged.Managers;

import org.bukkit.configuration.file.FileConfiguration;

public class MessageManager {

    private FileConfiguration messagesConfig;
    private String noPermissionMessage;
    private String tamedMessage;
    private String blacklistedMessage;
    private String unsafeSpawnMessage;
    private String toolPaidMessage;
    private String eggPaidMessage;
    private String failedPayToolMessage;
    private String failedPayEggMessage;
    private String denyWorldGuardEggingMessage;
    private String denyWorldGuardSpawnMessage;
    private String denyItemsMessage;

    public MessageManager(FileConfiguration messagesConfig) {
        this.messagesConfig = messagesConfig;
        loadMessages();
    }

    public void reload(FileConfiguration messagesConfig) {
        this.messagesConfig = messagesConfig;
        loadMessages();
    }

    public void loadMessages() {
        noPermissionMessage = messagesConfig.getString("no-permission",
                "<red>You do not have permission to egg a <dark_red><entity>." // Default message
        );

        tamedMessage = messagesConfig.getString("tamed",
                "<red>You cannot egg someone else's <dark_red><entity>." // Default message
        );

        blacklistedMessage = messagesConfig.getString("blacklisted",
                "<red>Egging of <dark_red><entity><red> has been disabled on this server." // Default message
        );

        unsafeSpawnMessage = messagesConfig.getString("unsafe-spawn",
                "<red>This is not a safe spawn location for your <dark_red><entity>." // Default message
        );

        toolPaidMessage = messagesConfig.getString("tool-paid",
                "<yellow>You have paid <green><cost><yellow> to get an <dark_purple>egging tool." // Default message
        );

        eggPaidMessage = messagesConfig.getString("egg-paid",
                "<yellow>You have paid <green><cost><yellow> to egg that <dark_purple><entity>." // Default message
        );

        failedPayToolMessage = messagesConfig.getString("failed-pay-tool",
                "<red>You need <green><cost><red> for an egging tool." // Default message
        );

        failedPayEggMessage = messagesConfig.getString("failed-pay-egg",
                "<red>You need <green><cost><red> to egg that <dark_red><entity>." // Default message
        );

        denyWorldGuardEggingMessage = messagesConfig.getString("worldguard-deny-egging",
                "<red>You can't egg mobs here." // Default message
        );

        denyWorldGuardSpawnMessage = messagesConfig.getString("worldguard-deny-spawn",
                "<red>You can't spawn an egg here." // Default message
        );

        denyItemsMessage = messagesConfig.getString("deny-items",
                "<red>That <dark_red><entity><red> has items in inventory and cannot be egged." // Default message
        );
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getTamedMessage() {
        return tamedMessage;
    }

    public String getBlacklistedMessage() {
        return blacklistedMessage;
    }

    public String getUnsafeSpawnMessage() {
        return unsafeSpawnMessage;
    }

    public String getToolPaidMessage() {
        return toolPaidMessage;
    }

    public String getEggPaidMessage() {
        return  eggPaidMessage;
    }

    public String getFailedPayToolMessage() {
        return failedPayToolMessage;
    }

    public String getFailedPayEggMessage() {
        return failedPayEggMessage;
    }

    public String getDenyWorldGuardEggingMessage() {
        return denyWorldGuardEggingMessage;
    }

    public String getDenyWorldGuardSpawnMessage() {
        return denyWorldGuardSpawnMessage;
    }

    public String getDenyItemsMessage() {
        return denyItemsMessage;
    }
}
