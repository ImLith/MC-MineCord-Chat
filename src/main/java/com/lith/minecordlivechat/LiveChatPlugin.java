package com.lith.minecordlivechat;

import org.bukkit.Bukkit;
import com.lith.lithcore.abstractClasses.AbstractPlugin;
import com.lith.lithcore.helpers.ReloadConfigCmd;
import com.lith.minecord.MineCordPlugin;
import com.lith.minecordlivechat.config.ConfigManager;
import com.lith.minecordlivechat.events.discord.BotEvent;
import com.lith.minecordlivechat.events.discord.SendDiscordMessage;
import com.lith.minecordlivechat.events.discord.SlashCommandEvent;
import com.lith.minecordlivechat.events.minecraft.PlayerAchievement;
import com.lith.minecordlivechat.events.minecraft.PlayerChat;
import com.lith.minecordlivechat.events.minecraft.PlayerDeath;
import com.lith.minecordlivechat.events.minecraft.PlayerJoin;
import com.lith.minecordlivechat.events.minecraft.PlayerLeave;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

public class LiveChatPlugin extends AbstractPlugin<LiveChatPlugin, ConfigManager> {
  @Getter
  private Plugin emojiesPlugin = null;

  @Override
  public void onEnable() {
    configs = new ConfigManager(this);
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
    if (!configs.dcMsg.serverOff.isEmpty())
      if (MineCordPlugin.getDiscordManager() != null)
        MineCordPlugin.getDiscordManager().sendMessage(configs.channelId, configs.dcMsg.serverOff);

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
    unregisterAllEvents();
  }

  @Override
  protected void registerCommands() {
    new ReloadConfigCmd<LiveChatPlugin>(this, Static.Command.PermissionKeys.RELOAD, Static.Command.Names.RELOAD);
  }

  @Override
  protected void registerEvents() {
    if (!configs.dcMsg.format.isEmpty())
      registerEvent(new PlayerChat(this));

    if (!configs.dcMsg.join.isEmpty())
      registerEvent(new PlayerJoin(this));

    if (!configs.dcMsg.leave.isEmpty())
      registerEvent(new PlayerLeave(this));

    if (!configs.dcMsg.achievement.isEmpty())
      registerEvent(new PlayerAchievement(this));

    if (configs.dcMsg.onDeath)
      registerEvent(new PlayerDeath(this));

    MineCordPlugin.getDiscordManager().addEvent(
        new BotEvent(this),
        new SendDiscordMessage(this),
        new SlashCommandEvent(this));
  }
}
