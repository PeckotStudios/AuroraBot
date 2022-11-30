package com.peckot.app.AuroraBot.exceptions;

public abstract class PluginException extends RuntimeException {

    private final String pluginName;

    public PluginException(String pluginName, String message) {
        super(message);
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }

}
