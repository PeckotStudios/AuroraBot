package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;

public class Plugin {

    private final String name;
    private final String version;
    private final String author;
    private final String mainClass;
    private final String description;
    private final String url;
    private final String path;

    private PluginService service;

    public Plugin(String name, String version, String author, String mainClass, String description, String url, String path) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.mainClass = mainClass;
        this.description = description;
        this.url = url;
        this.path = path;
    }

    public String getName() { return name; }

    public String getVersion() { return version; }

    public String getAuthor() { return author; }

    public String getMainClass() { return mainClass; }

    public String getDescription() { return description; }

    public String getUrl() { return url; }

    public String getPath() { return path; }

    public String getFolder() { return path.substring(0, path.lastIndexOf(".jar")); }

    public PluginService getService() { return service; }

    public void setService(PluginService service) { this.service = service; }

    public boolean isLoaded() { return null != service && Aurora.getPluginManager().getPlugin(service) != null; }

    @Override
    public String toString() { return "Plugin{name='" + name + "', version='" + version + "', author='" + author + "', mainClass='" + mainClass + "', description='" + description + "', url='" + url + "'}"; }

}
