package com.lith.minecordlivechat.events.discord;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.classes.McMessageBuilder;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;

@RequiredArgsConstructor
public class SendDiscordMessage extends ListenerAdapter {
    @NotNull
    private final LiveChatPlugin plugin;

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (plugin.configs.mcMsg.format.isEmpty())
            return;

        McMessageBuilder msgBuilder = new McMessageBuilder(plugin, event);
        if (!msgBuilder.isValid())
            return;

        Component msg = msgBuilder.addReplier().build();
        if (msg == null)
            return;

        plugin.getServer().broadcast(msg);
    }
}
