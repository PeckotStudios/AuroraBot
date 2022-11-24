package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.exceptions.*;
import me.zhenxin.qqbot.core.BotCore;
import me.zhenxin.qqbot.websocket.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class PluginManager {

    private static final Logger log = Aurora.getLogger();
    private static final List<Plugin> pluginList = new ArrayList<>();
    private static final List<Plugin> loadedPluginList = new ArrayList<>();

    private static BotCore botInstance;

    public PluginManager(BotCore bot) {
        botInstance = bot;
        log.info("正在读取插件...");
        List<Plugin> list = PluginLoader.loadAll();
        pluginList.addAll(list);
        log.debug("读取到以下插件：");
        list.forEach(plugin -> log.debug(plugin.toString()));
        log.info("插件读取完毕！");
    }

    public void loadAll() {
        log.debug("运行插件中...");
        pluginList.forEach(plugin -> {
            if (!loadedPluginList.contains(plugin)) {
                log.debug("正在加载：{}", plugin.getName());
                plugin.getService().onEnable();
                loadedPluginList.add(plugin);
                log.debug("插件{}加载成功！", plugin.getName());
            }
        });
    }

    public void unloadAll() {
        log.debug("卸载插件中...");
        loadedPluginList.forEach(plugin -> {
            log.debug("正在卸载：{}", plugin.getName());
            plugin.getService().onDisable();
            log.debug("插件{}卸载成功！", plugin.getName());
        });
        loadedPluginList.clear();
        pluginList.clear();
    }

    public void load(@NotNull Plugin plugin) throws PluginAlreadyLoadedException, PluginLoadedNotFoundException {
        log.debug("接收到单个插件加载事件：{}", plugin.getName());
        if (loadedPluginList.contains(plugin)) {
            log.debug("插件{}加载失败：插件已加载完毕！", plugin.getName());
            throw new PluginAlreadyLoadedException(plugin);
        } else {
            log.debug("正在加载：{}", plugin.getName());
            plugin.getService().onEnable();
            loadedPluginList.add(plugin);
            log.debug("插件{}加载成功！", plugin.getName());
        }
    }

    public void load(@NotNull String pluginName) throws PluginAlreadyLoadedException, PluginFileNotFoundException {
        log.debug("接收到单个插件加载事件：{}", pluginName);
        Plugin plugin = getPlugin(pluginName);
        if (null != plugin) {
            log.debug("插件{}加载失败：插件已加载完毕！", plugin.getName());
            throw new PluginAlreadyLoadedException(plugin);
        } else {
            try {
                plugin = PluginLoader.load(pluginName);
                if (null == plugin || null == plugin.getService()) {
                    log.debug("插件{}加载失败：插件读取失败！", pluginName);
                    throw new PluginFileNotFoundException(pluginName);
                }
            } catch (PluginFileNotFoundException e) {
                log.error("无法读取插件plugin.properties：{}", e.getPluginName(), e);
                return;
            }
            log.debug("正在加载：{}", plugin.getName());
            plugin.getService().onEnable();
            loadedPluginList.add(plugin);
            log.debug("插件{}加载成功！", plugin.getName());
        }
    }

    public void unload(@NotNull Plugin plugin) throws PluginLoadedNotFoundException {
        log.debug("接收到单个插件卸载事件：{}", plugin.getName());
        if (loadedPluginList.contains(plugin)) {
            log.debug("正在卸载：{}", plugin.getName());
            plugin.getService().onDisable();
            log.debug("插件{}卸载成功！", plugin.getName());
            loadedPluginList.remove(plugin);
        } else {
            log.debug("插件{}卸载失败：该插件尚未加载！", plugin.getName());
            throw new PluginLoadedNotFoundException(plugin.getName());
        }
    }

    public void unload(@NotNull String pluginName) throws PluginLoadedNotFoundException {
        log.debug("接收到单个插件卸载事件：{}", pluginName);
        Plugin plugin = getPlugin(pluginName);
        if (null != plugin) {
            log.debug("正在卸载：{}", plugin.getName());
            plugin.getService().onDisable();
            log.debug("插件{}卸载成功！", plugin.getName());
            loadedPluginList.remove(plugin);
        } else {
            log.debug("插件{}卸载失败：该插件尚未加载！", pluginName);
            throw new PluginLoadedNotFoundException(pluginName);
        }
    }

    public void setEventHandler(EventHandler eventHandler) {
        botInstance.setEventHandler(eventHandler);
    }

    public List<Plugin> getLoadedPluginList() {
        return loadedPluginList;
    }

    public Plugin getPlugin(@NotNull String pluginName) {
        return loadedPluginList.stream().filter(plugin -> plugin.getName().equalsIgnoreCase(pluginName))
                .findFirst().orElse(pluginList.stream().filter(plugin ->
                        plugin.getName().equals(pluginName)).findFirst().orElse(null));
    }

    public Plugin getPlugin(@NotNull PluginService pluginService) {
        return loadedPluginList.stream().filter(plugin -> plugin.getService() == pluginService)
                .findFirst().orElse(pluginList.stream().filter(plugin ->
                        plugin.getService() == pluginService).findFirst().orElse(null));
    }

}
