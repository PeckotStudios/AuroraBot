package com.peckot.app.AuroraBot.exceptions;

/**
 * 读取格式不合法插件时抛出.
 * @author Pectics
 * */
public class PluginPropertiesNotFoundException extends PluginException {

    public PluginPropertiesNotFoundException(String pluginName) {
        super(pluginName, "Plugin does not contains plugin.properties");
    }

}
