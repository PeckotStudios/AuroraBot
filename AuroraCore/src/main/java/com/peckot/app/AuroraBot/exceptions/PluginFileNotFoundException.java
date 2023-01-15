package com.peckot.app.AuroraBot.exceptions;

import com.peckot.app.AuroraBot.plugins.PluginManager;

/**
 * 使用{@link PluginManager}加载插件,无法读取插件文件时抛出.
 * @author Pectics
 * */
public class PluginFileNotFoundException extends PluginException {

    public PluginFileNotFoundException(String pluginName) {
        super(pluginName, "Plugin file is not found");
    }

}
