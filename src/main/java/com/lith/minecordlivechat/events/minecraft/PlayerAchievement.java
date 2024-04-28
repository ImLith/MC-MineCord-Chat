package com.lith.minecordlivechat.events.minecraft;

import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.Static;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import io.papermc.paper.advancement.AdvancementDisplay;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerAchievement implements Listener {
    private final LiveChatPlugin plugin;

    @EventHandler
    public void onPlayerAchievement(PlayerAdvancementDoneEvent event) {
        if (MineCordPlugin.getDiscordManager() == null || !MineCordPlugin.getDiscordManager().isOnline())
            return;

        Advancement advancement = event.getAdvancement();
        AdvancementDisplay display = advancement.getDisplay();
        if (display == null)
            return;

        Player player = event.getPlayer();
        if (!player.getAdvancementProgress(advancement).isDone())
            return;

        Component titleComponent = display.title();
        if (titleComponent == null)
            return;

        String advancementTitle = PlainTextComponentSerializer.plainText().serialize(titleComponent);
        if (advancementTitle == null)
            return;

        Component descriptionComponent = display.description();
        String advancementDescription = descriptionComponent != null
                ? PlainTextComponentSerializer.plainText().serialize(descriptionComponent)
                : "";

        MineCordPlugin.getDiscordManager().sendMessage(
                Static.textChannel,
                plugin.configs.dcMsg.achievement
                        .replace(Static.MessageKey.PLAYER_NAME, player.getName())
                        .replace(Static.MessageKey.ACHIEVEMENT_NAME, advancementTitle)
                        .replace(Static.MessageKey.ACHIEVEMENT_DESCRIPTION, advancementDescription));
    }
}
