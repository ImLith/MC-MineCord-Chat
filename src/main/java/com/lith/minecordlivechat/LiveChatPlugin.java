package com.lith.minecordlivechat;

import org.bukkit.Bukkit;
import com.lith.lithcore.abstractClasses.AbstractPlugin;
import com.lith.lithcore.helpers.ReloadConfigCmd;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecord.utils.DiscordChannelUtil;
import com.lith.minecordlivechat.config.ConfigManager;
import com.lith.minecordlivechat.events.minecraft.PlayerAchievement;
import com.lith.minecordlivechat.events.minecraft.PlayerChat;
import com.lith.minecordlivechat.events.minecraft.PlayerDeath;
import com.lith.minecordlivechat.events.minecraft.PlayerJoin;
import com.lith.minecordlivechat.events.minecraft.PlayerLeave;
import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.Plugin;

public class LiveChatPlugin extends AbstractPlugin<LiveChatPlugin, ConfigManager> {
  @Getter
  private Plugin emojiesPlugin = null;
  @Getter
  private MineCordPlugin minecord = null;

  @Override
  public void onEnable() {
    configs = new ConfigManager(this);
    minecord = MineCordPlugin.getPlugin();
    MineCordPlugin.getDiscordManager().addGatewayIntent(Static.gatewayIntents);

    super.onEnable();

    if (configs.mcMsg.addEmojies) {
      Plugin softDependPlugin = Bukkit.getPluginManager().getPlugin("Emojies");

      if (softDependPlugin != null && softDependPlugin.isEnabled()) {
        emojiesPlugin = softDependPlugin;
        log.info("Emojies plugin loaded.");
      } else
        log.warning("Emojies plugin not found!");
    }
  }

  @Override
  public void onDisable() {
    if (isValid() && !configs.dcMsg.serverOff.isEmpty())
      if (MineCordPlugin.getDiscordManager() != null && MineCordPlugin.getDiscordManager().isOnline())
        MineCordPlugin.getDiscordManager().sendMessage(Static.textChannel, configs.dcMsg.serverOff);

    super.onDisable();
  }

  @Override
  public void reloadConfigs() {
    super.reloadConfigs();
    registerEvents();
  }

  @Override
  protected void registerConfigs() {
    super.registerConfigs();
    Static.textChannel = null;

    unregisterAllEvents();
    validateChannel();
  }

  @Override
  protected void registerCommands() {
    new ReloadConfigCmd<LiveChatPlugin>(this, Static.Command.PermissionKeys.RELOAD, Static.Command.Names.RELOAD);
  }

  @Override
  protected void registerEvents() {
    if (!isValid()) {
      log.warning("Text channel not found! Check your configs");
      return;
    }

    if (!configs.dcMsg.format.isEmpty())
      registerEvent(new PlayerChat(this));

    if (!configs.dcMsg.join.isEmpty())
      registerEvent(new PlayerJoin(this));

    if (!configs.dcMsg.leave.isEmpty())
      registerEvent(new PlayerLeave(this));

    if (!configs.dcMsg.achievement.isEmpty())
      registerEvent(new PlayerAchievement(this));

    if (configs.dcMsg.onDeath)
      registerEvent(new PlayerDeath());
  }

  @Override
  protected void preRegisterRunnables() {
    if (isValid() && !configs.dcMsg.serverOn.isEmpty() && MineCordPlugin.getDiscordManager() != null)
      MineCordPlugin.getDiscordManager().sendMessage(
          Static.textChannel,
          configs.dcMsg.serverOn);
  }

  private void validateChannel() {
    if (MineCordPlugin.getDiscordManager() != null) {
      TextChannel channel = MineCordPlugin.getDiscordManager().getClient().getTextChannelById(configs.channelId);
      Static.textChannel = DiscordChannelUtil.isTextChannel(channel) ? channel : null;
    }
  }

  private Boolean isValid() {
    return Static.textChannel != null;
  }
}
