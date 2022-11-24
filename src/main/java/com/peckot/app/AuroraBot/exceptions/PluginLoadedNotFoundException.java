package com.peckot.app.AuroraBot.exceptions;

public class PluginLoadedNotFoundException extends PluginException {

    public PluginLoadedNotFoundException(String pluginName) {
        super(pluginName, "Plugin not found");
    }

}
