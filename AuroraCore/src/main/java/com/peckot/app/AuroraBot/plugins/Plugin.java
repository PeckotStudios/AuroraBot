package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;

/**
 * AuroraBot的插件实例.
 * @author Pectics
 * */
public class Plugin {

    private final String name;
    private final String version;
    private final String author;
    private final String mainClass;
    private final String description;
    private final String url;
    private final String path;

    private PluginService service;

    /**
     * 初始化插件.
     * @author Pectics
     * */
    public Plugin(String name, String version, String author, String mainClass, String description, String url, String path) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.mainClass = mainClass;
        this.description = description;
        this.url = url;
        this.path = path;
    }

    /**
     * 获取插件名称.
     * @return {@link String} 插件名称
     * */
    public String getName() { return name; }

    /**
     * 获取插件版本.
     * @return {@link String} 插件版本
     * */
    public String getVersion() { return version; }

    /**
     * 获取插件作者.
     * @return {@link String} 插件作者
     * */
    public String getAuthor() { return author; }

    /**
     * 获取插件主类.
     * @return {@link String} 插件主类
     * */
    public String getMainClass() { return mainClass; }

    /**
     * 获取插件简介.
     * @return {@link String} 插件简介
     * */
    public String getDescription() { return description; }

    /**
     * 获取插件网页地址.
     * @return {@link String} 插件网页地址
     * */
    public String getUrl() { return url; }

    /**
     * 获取插件文件路径.
     * @return {@link String} 插件文件路径
     * */
    public String getPath() { return path; }

    /**
     * 获取插件文件夹路径.
     * @return {@link String} 插件文件夹路径
     * */
    public String getFolder() { return path.substring(0, path.lastIndexOf(".jar")); }

    /**
     * 获取插件服务实例.
     * @return {@link PluginService} 插件服务实例
     * */
    public PluginService getService() { return service; }

    /**
     * 设置插件服务实例.
     * @param service 设置的{@link PluginService}插件服务实例
     * */
    public void setService(PluginService service) { this.service = service; }

    /**
     * 返回插件是否被加载.
     * @return {@link Boolean} 插件是否被加载
     * */
    public boolean isLoaded() { return null != service && Aurora.getPluginManager().getPlugin(service) != null; }

    @Override
    public String toString() { return "Plugin{name='" + name + "', version='" + version + "', author='" + author + "', mainClass='" + mainClass + "', description='" + description + "', url='" + url + "'}"; }

}
