package com.lith.minecordlivechat.events.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.Static;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerJoin implements Listener {
    private final LiveChatPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (MineCordPlugin.getDiscordManager() == null || !MineCordPlugin.getDiscordManager().isOnline())
            return;

        MineCordPlugin.getDiscordManager().sendMessage(
                plugin.configs.channelId,
                plugin.configs.dcMsg.join
                        .replace(Static.MessageKey.PLAYER_NAME, event.getPlayer().getName()));
    }
}
