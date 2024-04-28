package com.lith.minecordlivechat.config;

import com.lith.lithcore.abstractClasses.AbstractConfigManager;
import com.lith.minecord.classes.DiscordValidationBuilder;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.Static.ConfigKeys;

public class ConfigManager extends AbstractConfigManager<LiveChatPlugin, ConfigManager> {
    public String channelId = null;
    public DiscordMessage dcMsg;
    public MinecraftMessage mcMsg;
    public DiscordValidationBuilder mcTextValidations;
    public SlashCommands slashCommands;

    public ConfigManager(final LiveChatPlugin plugin) {
        super(plugin);

        mcTextValidations = new DiscordValidationBuilder();
        mcTextValidations.isOnlyFromGuild = true;
    }

    @Override
    public void load() {
        super.load();
        dcMsg = new DiscordMessage();
        mcMsg = new MinecraftMessage();
        slashCommands = new SlashCommands();
        channelId = config.getString(ConfigKeys.CHANNEL_ID);
        mcTextValidations.requiredChannelId = channelId;
    }

    public final class DiscordMessage {
        public final String join = config.getString(ConfigKeys.Discord_Messages.JOIN);
        public final String leave = config.getString(ConfigKeys.Discord_Messages.LEAVE);
        public final String achievement = config.getString(ConfigKeys.Discord_Messages.ACHIEVEMENT);
        public final String serverOn = config.getString(ConfigKeys.Discord_Messages.SERVER_ON);
        public final String serverOff = config.getString(ConfigKeys.Discord_Messages.SERVER_OFF);
        public final String format = config.getString(ConfigKeys.Discord_Messages.FORMAT);
        public final Boolean onDeath = config.getBoolean(ConfigKeys.Discord_Messages.DEATH);
    }

    public final class MinecraftMessage {
        public final Reply reply = new Reply();

        public final String prefix = getMessage(ConfigKeys.Minecraft_Messages.PREFIX);
        public final String hover = getMessage(ConfigKeys.Minecraft_Messages.HOVER);
        public final String format = getMessage(ConfigKeys.Minecraft_Messages.FORMAT);
        public final String afterEmojie = getMessage(ConfigKeys.Minecraft_Messages.AFTER_EMOJIE);
        public final String beforeEmojie = getMessage(ConfigKeys.Minecraft_Messages.BEFORE_EMOJIE);
        public final Boolean addEmojies = config.getBoolean(ConfigKeys.Minecraft_Messages.ADD_EMOJIES);
        public final Boolean isClickable = config.getBoolean(ConfigKeys.Minecraft_Messages.CLICKABLE);

        public final class Reply {
            public final String icon = getMessage(ConfigKeys.Minecraft_Messages.Reply.ICON);
            public final String hoverUser = getMessage(ConfigKeys.Minecraft_Messages.Reply.HOVER_USER);
            public final String hoverBot = getMessage(ConfigKeys.Minecraft_Messages.Reply.HOVER_BOT);
        }
    }

    public final class SlashCommands {
        public final Online online = new Online();

        public final class Online {
            public final Boolean enabled = config.getBoolean(ConfigKeys.Slash_Commands.Online.ENABLED);
            public final String name = config.getString(ConfigKeys.Slash_Commands.Online.NAME);
            public final String description = config.getString(ConfigKeys.Slash_Commands.Online.DESCRIPTION);
            public final Boolean isEphemeral = config.getBoolean(ConfigKeys.Slash_Commands.Online.IS_EPHEMERAL);
            public final Boolean showUserList = config.getBoolean(ConfigKeys.Slash_Commands.Online.SHOW_USER_LIST);
            public final String format = config.getString(ConfigKeys.Slash_Commands.Online.FORMAT);
        }
    }
}
