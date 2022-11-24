package com.peckot.app.AuroraBot.exceptions;

public class PluginPropertiesNotFoundException extends PluginException {

    public PluginPropertiesNotFoundException(String pluginName) {
        super(pluginName, "Plugin does not contains plugin.properties");
    }

}
