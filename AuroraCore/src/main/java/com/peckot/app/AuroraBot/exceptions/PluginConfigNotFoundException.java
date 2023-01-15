package com.peckot.app.AuroraBot.exceptions;

import com.peckot.app.AuroraBot.plugins.Plugin;
import com.peckot.app.AuroraBot.plugins.PluginService;

/**
 * 使用{@link PluginService#saveDefaultConfig()},无法读取插件jar文件内默认配置文件时抛出
 * @author Pectics
 * */
public class PluginConfigNotFoundException extends PluginException {

    private final Plugin plugin;

    public PluginConfigNotFoundException(Plugin plugin) {
        super(plugin.getName(), "Plugin does not contains config.properties");
        this.plugin = plugin;
    }

    /**
     * 获取出现问题的插件实例.
     * @return {@link Plugin} 插件实例
     * */
    public Plugin getPlugin() {
        return plugin;
    }

}
