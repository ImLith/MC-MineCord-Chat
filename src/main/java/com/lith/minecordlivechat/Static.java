package com.lith.minecordlivechat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.lith.lithcore.abstractClasses.AbstractConfigKey;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Static {
    public static TextChannel textChannel = null;

    final public static class ConfigKeys {
        public static final String CHANNEL_ID = "channel_id";

        public static final class Discord_Messages extends AbstractConfigKey {
            public static final String JOIN = setKey("join");
            public static final String LEAVE = setKey("leave");
            public static final String ACHIEVEMENT = setKey("achievement");
            public static final String DEATH = setKey("death");
            public static final String SERVER_ON = setKey("server_on");
            public static final String SERVER_OFF = setKey("server_off");
            public static final String FORMAT = setKey("format");
        }

        public static final class Minecraft_Messages extends AbstractConfigKey {
            public static final String CLICKABLE = setKey("clickable");
            public static final String PREFIX = setKey("prefix");
            public static final String HOVER = setKey("hover");
            public static final String FORMAT = setKey("format");
            public static final String ADD_EMOJIES = setKey("add_emojies");
            public static final String AFTER_EMOJIE = setKey("after_emojie");
            public static final String BEFORE_EMOJIE = setKey("before_emojie");

            public static final class Reply extends AbstractConfigKey {
                public static final String ICON = setKey("icon");
                public static final String HOVER_USER = setKey("hover_user");
                public static final String HOVER_BOT = setKey("hover_bot");
            }
        }

        public static final class Slash_Commands extends AbstractConfigKey {
            public static final String ENABLE_COMMANDS = setKey("enable_commands");

            public static final class Online extends AbstractConfigKey {
                public static final String ENABLED = setKey("enabled");
                public static final String NAME = setKey("name");
                public static final String DESCRIPTION = setKey("description");
                public static final String IS_EPHEMERAL = setKey("is_ephemeral");
                public static final String FORMAT = setKey("format");
            }
        }
    }

    public static final Collection<GatewayIntent> gatewayIntents = Collections
            .unmodifiableCollection(Arrays.asList(GatewayIntent.MESSAGE_CONTENT));

    final public static class MessageKey {
        public static final String PLAYER_NAME = "%player_name%";
        public static final String USER_NAME = "%user_name%";
        public static final String CONTENT = "%content%";
        public static final String ACHIEVEMENT_NAME = "%achievement_name%";
        public static final String ACHIEVEMENT_DESCRIPTION = "%achievement_description%";
        public static final String CURRENT = "%current%";
        public static final String MAX = "%max%";
    }

    final public static class Command {
        final public static class Names {
            public static final String RELOAD = "lcReload";
        }

        final public static class PermissionKeys {
            public static final String PREFIX = "livechat";
            public static final String RELOAD = PermissionKeys.PREFIX + "reload";
        }
    }
}
