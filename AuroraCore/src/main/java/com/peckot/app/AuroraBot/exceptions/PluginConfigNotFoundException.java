package com.peckot.app.AuroraBot.exceptions;

import com.peckot.app.AuroraBot.plugins.Plugin;

public class PluginConfigNotFoundException extends PluginException {

    private final Plugin plugin;

    public PluginConfigNotFoundException(Plugin plugin) {
        super(plugin.getName(), "Plugin does not contains config.properties");
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
