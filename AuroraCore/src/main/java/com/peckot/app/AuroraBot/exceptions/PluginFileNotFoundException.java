package com.peckot.app.AuroraBot.exceptions;

public class PluginFileNotFoundException extends PluginException {

    public PluginFileNotFoundException(String pluginName) {
        super(pluginName, "Plugin file is not found");
    }

}
