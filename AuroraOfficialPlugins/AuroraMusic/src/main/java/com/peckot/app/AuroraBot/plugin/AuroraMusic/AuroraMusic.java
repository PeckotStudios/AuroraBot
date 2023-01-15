package com.peckot.app.AuroraBot.plugin.AuroraMusic;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.plugins.PluginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuroraMusic extends PluginService {

    @Override
    public void onEnable() {
        log.info("AuroraMusic正在启动……");
        Aurora.getPluginManager().setEventHandler(new CommandExecutor(this));
    }

    @Override
    public void onDisable() {
        log.info("AuroraMusic正在关闭……");
    }

}
