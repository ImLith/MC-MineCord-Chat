package com.lith.minecordlivechat.events.discord;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.classes.McMessageBuilder;
import net.kyori.adventure.text.Component;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;

@RequiredArgsConstructor
public class BotEvent extends ListenerAdapter {
    @NotNull
    private final LiveChatPlugin plugin;

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        if (!plugin.configs.dcMsg.serverOn.isEmpty())
            if (MineCordPlugin.getDiscordManager() != null)
                MineCordPlugin.getDiscordManager().sendMessage(plugin.configs.channelId, plugin.configs.dcMsg.serverOn);
    }

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
