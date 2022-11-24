package com.peckot.app.AuroraBot.exceptions;

import com.peckot.app.AuroraBot.plugins.Plugin;

public class PluginAlreadyLoadedException extends PluginException {

    private final Plugin plugin;

    public PluginAlreadyLoadedException(Plugin plugin) {
        super(plugin.getName(), "Plugin already loaded");
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}
