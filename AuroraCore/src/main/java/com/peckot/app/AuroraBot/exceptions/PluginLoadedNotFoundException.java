package com.peckot.app.AuroraBot.exceptions;

import com.peckot.app.AuroraBot.plugins.PluginManager;

/**
 * 使用{@link PluginManager}卸载未加载的插件时抛出.
 * @author Pectics
 * */
public class PluginLoadedNotFoundException extends PluginException {

    public PluginLoadedNotFoundException(String pluginName) {
        super(pluginName, "Plugin not found");
    }

}
