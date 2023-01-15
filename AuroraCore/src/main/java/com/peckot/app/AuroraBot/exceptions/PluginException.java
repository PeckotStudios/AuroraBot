package com.peckot.app.AuroraBot.exceptions;

/**
 * 插件抛出错误的父抽象类.
 * @author Pectics
 * */
public abstract class PluginException extends RuntimeException {

    private final String pluginName;

    public PluginException(String pluginName, String message) {
        super(message);
        this.pluginName = pluginName;
    }

    /**
     * 获取出现错误的插件名称.
     * @return {@link String} 插件名称
     * */
    public String getPluginName() {
        return pluginName;
    }

}
