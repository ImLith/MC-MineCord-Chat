package com.lith.minecordlivechat.events.discord;

import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import java.util.List;
import com.lith.lithcore.utils.PlayerUtil;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.Static;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.awt.Color;

@RequiredArgsConstructor
public class SlashCommandEvent extends ListenerAdapter {
    private final LiveChatPlugin plugin;

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals(plugin.configs.slashCommands.online.name.toLowerCase())) {
            event.deferReply(plugin.configs.slashCommands.online.isEphemeral).queue();

            List<String> playerNames = PlayerUtil.getOnlinePlayerNames();
            int onlinePlayers = playerNames.size();
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setColor(Color.RED)
                    .setAuthor(plugin.configs.slashCommands.online.format
                            .replace(Static.MessageKey.CURRENT, String.valueOf(onlinePlayers))
                            .replace(Static.MessageKey.MAX, String.valueOf(Bukkit.getMaxPlayers())));

            if (onlinePlayers > 0)
                embedBuilder.setDescription("**" + String.join("** | **", playerNames) + "**");

            event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
