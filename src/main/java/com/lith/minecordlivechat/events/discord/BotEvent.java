package com.lith.minecordlivechat.events.discord;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecordlivechat.LiveChatPlugin;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;

@RequiredArgsConstructor
public class BotEvent extends ListenerAdapter {
    @NotNull
    private final LiveChatPlugin plugin;

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        JDA client = event.getJDA();

        if (!plugin.configs.dcMsg.serverOn.isEmpty())
            if (MineCordPlugin.getDiscordManager() != null)
                MineCordPlugin.getDiscordManager().sendMessage(plugin.configs.channelId, plugin.configs.dcMsg.serverOn);

        if (plugin.configs.slashCommands.commandsEnabled)
            registerGuildCommands(client);
    }

    private void registerGuildCommands(JDA client) {
        if (MineCordPlugin.getPlugin() == null)
            return;

        Long serverId = MineCordPlugin.getPlugin().configs.getServerId();
        if (serverId == null)
            return;

        Guild guild = client.getGuildById(serverId);
        if (guild == null)
            return;

        if (plugin.configs.slashCommands.online.enabled
                && !plugin.configs.slashCommands.online.name.isEmpty()) {

            guild.updateCommands().addCommands(
                    Commands.slash(
                            plugin.configs.slashCommands.online.name.toLowerCase(),
                            plugin.configs.slashCommands.online.description))
                    .queue();
        }
    }
}
