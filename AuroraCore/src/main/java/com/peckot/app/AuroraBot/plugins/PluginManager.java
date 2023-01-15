package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.exceptions.PluginAlreadyLoadedException;
import com.peckot.app.AuroraBot.exceptions.PluginFileNotFoundException;
import com.peckot.app.AuroraBot.exceptions.PluginLoadedNotFoundException;
import me.zhenxin.qqbot.core.BotCore;
import me.zhenxin.qqbot.websocket.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * AuroraBot的插件管理器.
 * @author Pectics
 * */
public final class PluginManager {

    private static final Logger log = Aurora.getLogger();
    private static final List<Plugin> pluginList = new ArrayList<>();
    private static final List<Plugin> loadedPluginList = new ArrayList<>();

    private static BotCore botInstance;

    /**
     * 初始化插件管理器,默认管理器在软件实例启动时初始化.
     * @param bot zhenxin提供的{@link BotCore}机器人核心实例
     * @author Pectics
     * */
    public PluginManager(BotCore bot) {
        botInstance = bot;
        log.info("正在读取插件...");
        List<Plugin> list = PluginLoader.loadAll();
        pluginList.addAll(list);
        log.debug("读取到以下插件：");
        list.forEach(plugin -> log.debug(plugin.toString()));
        log.info("插件读取完毕！");
    }

    /**
     * 启动所有已成功读取的插件.
     * @author Pectics
     * */
    public void loadAll() {
        log.debug("启动插件中...");
        pluginList.forEach(plugin -> {
            if (!loadedPluginList.contains(plugin)) {
                log.debug("正在加载：{}", plugin.getName());
                plugin.getService().onEnable();
                loadedPluginList.add(plugin);
                log.debug("插件{}加载成功！", plugin.getName());
            }
        });
    }

    /**
     * 停止所有已加载的插件.
     * @author Pectics
     * */
    public void unloadAll() {
        log.debug("停止插件中...");
        loadedPluginList.forEach(plugin -> {
            log.debug("正在卸载：{}", plugin.getName());
            plugin.getService().onDisable();
            log.debug("插件{}卸载成功！", plugin.getName());
        });
        loadedPluginList.clear();
        pluginList.clear();
    }

    /**
     * 加载单个插件实例,用于插件间控制.
     * @param plugin 将加载的插件实例
     * @throws PluginAlreadyLoadedException 插件已经启动
     * @author Pectics
     * */
    public void load(@NotNull Plugin plugin) throws PluginAlreadyLoadedException {
        log.debug("接收到请求单个插件加载事件：{}", plugin.getName());
        if (loadedPluginList.contains(plugin)) {
            log.debug("插件{}加载失败：插件已经启动！", plugin.getName());
            throw new PluginAlreadyLoadedException(plugin);
        } else {
            log.debug("正在加载：{}", plugin.getName());
            plugin.getService().onEnable();
            loadedPluginList.add(plugin);
            log.debug("插件{}加载成功！", plugin.getName());
        }
    }

    /**
     * 加载单个插件实例,用于插件间控制.
     * @param pluginName 将加载的插件名称
     * @throws PluginAlreadyLoadedException 插件已经启动
     * @throws PluginFileNotFoundException  未找到插件文件
     * @author Pectics
     * */
    public void load(@NotNull String pluginName) throws PluginAlreadyLoadedException, PluginFileNotFoundException {
        log.debug("接收到请求单个插件加载事件：{}", pluginName);
        Plugin plugin = getPlugin(pluginName);
        if (loadedPluginList.contains(plugin)) {
            log.debug("插件{}加载失败：插件已经启动！", plugin.getName());
            throw new PluginAlreadyLoadedException(plugin);
        } else {
            try {
                plugin = PluginLoader.load(pluginName);
                if (null == plugin || null == plugin.getService()) {
                    log.debug("插件{}加载失败：插件无法读取！", pluginName);
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

    /**
     * 卸载单个插件实例,用于插件间控制.
     * @param plugin 将卸载的插件实例
     * @throws PluginLoadedNotFoundException 未找到已加载的插件
     * @author Pectics
     * */
    public void unload(@NotNull Plugin plugin) throws PluginLoadedNotFoundException {
        log.debug("接收到单个插件卸载事件：{}", plugin.getName());
        if (loadedPluginList.contains(plugin)) {
            log.debug("正在卸载：{}", plugin.getName());
            plugin.getService().onDisable();
            log.debug("插件{}卸载成功！", plugin.getName());
            loadedPluginList.remove(plugin);
        } else {
            log.debug("插件{}卸载失败：插件尚未启动！", plugin.getName());
            throw new PluginLoadedNotFoundException(plugin.getName());
        }
    }

    /**
     * 卸载单个插件实例,用于插件间控制.
     * @param pluginName 将卸载的插件名称
     * @throws PluginLoadedNotFoundException 未找到已加载的插件
     * @author Pectics
     * */
    public void unload(@NotNull String pluginName) throws PluginLoadedNotFoundException {
        log.debug("接收到单个插件卸载事件：{}", pluginName);
        Plugin plugin = getPlugin(pluginName);
        if (loadedPluginList.contains(plugin)) {
            log.debug("正在卸载：{}", plugin.getName());
            plugin.getService().onDisable();
            log.debug("插件{}卸载成功！", plugin.getName());
            loadedPluginList.remove(plugin);
        } else {
            log.debug("插件{}卸载失败：插件尚未启动！", pluginName);
            throw new PluginLoadedNotFoundException(pluginName);
        }
    }

    /**
     * 设置插件的事件处理器.
     * @param eventHandler 事件处理器实例
     * @author Pectics
     * */
    public void setEventHandler(EventHandler eventHandler) {
        botInstance.setEventHandler(eventHandler);
    }

    /**
     * 获取已加载的插件列表.
     * @return {@link List} 已加载的插件列表对象
     * @author Pectics
     * */
    public List<Plugin> getLoadedPluginList() {
        return loadedPluginList;
    }

    /**
     * 通过插件名称获取已加载的插件.
     * @param pluginName 获取的插件名称
     * @return {@link Plugin} 获取到的插件对象
     * */
    public Plugin getPlugin(@NotNull String pluginName) {
        return loadedPluginList.stream().filter(plugin -> plugin.getName().equalsIgnoreCase(pluginName))
                .findFirst().orElse(pluginList.stream().filter(plugin ->
                        plugin.getName().equals(pluginName)).findFirst().orElse(null));
    }

    /**
     * 通过插件服务实例获取已加载的插件.
     * @param pluginService 获取的插件服务实例
     * @return {@link Plugin} 获取到的插件对象
     * */
    public Plugin getPlugin(@NotNull PluginService pluginService) {
        return loadedPluginList.stream().filter(plugin -> plugin.getService() == pluginService)
                .findFirst().orElse(pluginList.stream().filter(plugin ->
                        plugin.getService() == pluginService).findFirst().orElse(null));
    }

}
