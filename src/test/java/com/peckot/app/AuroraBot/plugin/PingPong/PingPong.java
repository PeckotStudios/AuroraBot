package com.peckot.app.AuroraBot.plugin.PingPong;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.plugins.PluginService;
import org.slf4j.Logger;

public class PingPong extends PluginService {

    private static Logger log;

    @Override
    public void onEnable() {
        log = getLogger();
        saveDefaultConfig();
        Aurora.getPluginManager().setEventHandler(new PingPongEventHandler(this));
        log.info("PingPong加载成功啦！！");
    }

    @Override
    public void onDisable() {
        log.info("PingPong卸载成功啦！！");
    }

}
