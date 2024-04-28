package com.lith.minecordlivechat.classes;

import java.util.ArrayList;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.dv8tion.jda.api.entities.User;
import com.lith.emojies.util.EmojiesUtil;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecord.utils.DiscordMessageUtil;
import com.lith.minecord.utils.DiscordUtil;
import com.lith.minecordlivechat.LiveChatPlugin;
import com.lith.minecordlivechat.Static;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;

public class McMessageBuilder {
    private final LiveChatPlugin plugin;
    @Getter
    private boolean isValid = false;
    private Message message = null;
    private Message repliedMessage = null;

    public McMessageBuilder(LiveChatPlugin plugin, MessageReceivedEvent event) {
        this.plugin = plugin;

        if (DiscordUtil.validateMessageOrigin(event, plugin.configs.mcTextValidations)) {
            this.message = event.getMessage();
            this.isValid = true;
        }
    };

    public McMessageBuilder setMessage(Message message) {
        if (DiscordMessageUtil.isTextMessage(message))
            this.message = message;

        return this;
    }

    public McMessageBuilder setRepliedMessage(Message message) {
        if (DiscordMessageUtil.isReplyMessage(message))
            this.repliedMessage = message;

        return this;
    }

    public McMessageBuilder addReplier() {
        if (DiscordMessageUtil.isReplyMessage(this.message))
            this.repliedMessage = this.message.getReferencedMessage();

        return this;
    }

    public Component build() {
        if (message == null)
            return null;

        ArrayList<TextComponent> targetResponse = new ArrayList<>();

        if (!plugin.configs.mcMsg.prefix.isEmpty())
            targetResponse.add(buildMessagePrefix());

        if (repliedMessage != null && !plugin.configs.mcMsg.reply.icon.isEmpty())
            targetResponse.add(buildReplySection());

        targetResponse.add(buildMessageSection());
        Component responseComponent = join(noSeparators(), targetResponse);

        if (plugin.configs.mcMsg.isClickable && MineCordPlugin.getPlugin() != null) {
            String inviteLink = MineCordPlugin.getPlugin().configs.getInviteLink();

            if (inviteLink != null && !inviteLink.isEmpty())
                responseComponent = responseComponent.clickEvent(openUrl(inviteLink));
        }

        return responseComponent;
    }

    private TextComponent buildReplySection() {
        return addHoverText(text(plugin.configs.mcMsg.reply.icon), repliedMessage.getAuthor());
    }

    private TextComponent buildMessageSection() {
        String contentDisplay = message.getContentDisplay();

        if (plugin.getEmojiesPlugin() != null)
            contentDisplay = EmojiesUtil.addEmojies(contentDisplay, plugin.configs.mcMsg.afterEmojie,
                    plugin.configs.mcMsg.beforeEmojie);

        TextComponent textComponent = text(plugin.configs.mcMsg.format
                .replace(Static.MessageKey.USER_NAME, message.getAuthor().getEffectiveName())
                .replace(Static.MessageKey.CONTENT, contentDisplay));

        if (repliedMessage != null)
            textComponent = addHoverText(textComponent, repliedMessage.getAuthor());

        return textComponent;
    }

    private TextComponent buildMessagePrefix() {
        TextComponent text = text(plugin.configs.mcMsg.prefix
                .replace(Static.MessageKey.USER_NAME, message.getAuthor().getEffectiveName()));

        if (!plugin.configs.mcMsg.hover.isEmpty())
            text = text.hoverEvent(showText(text(plugin.configs.mcMsg.hover)));

        return text;
    }

    private TextComponent addHoverText(TextComponent text, User user) {
        String hoverText = null;

        if (user.isBot()) {
            if (!plugin.configs.mcMsg.reply.hoverBot.isEmpty())
                hoverText = plugin.configs.mcMsg.reply.hoverBot;
        } else {
            if (!plugin.configs.mcMsg.reply.hoverUser.isEmpty())
                hoverText = plugin.configs.mcMsg.reply.hoverUser
                        .replace(Static.MessageKey.USER_NAME, user.getEffectiveName());
        }

        if (hoverText != null) {
            String contentDisplay = repliedMessage.getContentStripped();

            if (plugin.getEmojiesPlugin() != null)
                contentDisplay = EmojiesUtil.addEmojies(contentDisplay, plugin.configs.mcMsg.afterEmojie,
                        plugin.configs.mcMsg.beforeEmojie);

            text = text.hoverEvent(showText(text(hoverText.replace(Static.MessageKey.CONTENT, contentDisplay))));
        }

        return text;
    }
}
