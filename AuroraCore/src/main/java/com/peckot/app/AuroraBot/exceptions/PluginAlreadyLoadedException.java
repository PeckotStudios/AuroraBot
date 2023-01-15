package com.peckot.app.AuroraBot.exceptions;

import com.peckot.app.AuroraBot.plugins.Plugin;
import com.peckot.app.AuroraBot.plugins.PluginManager;

/**
 * 使用{@link PluginManager}加载已加载的插件时抛出.
 * @author Pectics
 * */
public class PluginAlreadyLoadedException extends PluginException {

    private final Plugin plugin;

    public PluginAlreadyLoadedException(Plugin plugin) {
        super(plugin.getName(), "Plugin already loaded");
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
