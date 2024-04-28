package com.lith.minecordlivechat.events.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecordlivechat.Static;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class PlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (MineCordPlugin.getDiscordManager() == null || !MineCordPlugin.getDiscordManager().isOnline())
            return;

        Component deathMessageComponent = event.deathMessage();
        if (deathMessageComponent == null)
            return;

        String deathMessage = PlainTextComponentSerializer.plainText().serialize(deathMessageComponent);
        String playerName = event.getEntity().getName();

        MineCordPlugin.getDiscordManager().sendMessage(
                Static.textChannel,
                deathMessage.replace(playerName, "**" + playerName + "**"));
    }
}
