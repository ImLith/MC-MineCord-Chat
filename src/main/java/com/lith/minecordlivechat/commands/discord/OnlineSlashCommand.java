package com.lith.minecordlivechat.commands.discord;

import java.util.List;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import com.lith.lithcore.utils.PlayerUtil;
import com.lith.minecord.interfaces.ISlashCommand;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.Static;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import java.awt.Color;

@RequiredArgsConstructor
public class OnlineSlashCommand implements ISlashCommand {
    private final LiveChatPlugin plugin;

    @Override
    public @NotNull String getCommandName() {
        return plugin.configs.slashCommands.online.name;
    }

    @Override
    public @NotNull String getCommandDescription() {
        return plugin.configs.slashCommands.online.description;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        if (!plugin.configs.slashCommands.online.enabled)
            return;

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
