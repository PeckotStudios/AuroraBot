package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.exceptions.PluginConfigNotFoundException;
import me.zhenxin.qqbot.api.ApiManager;
import org.slf4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * AuroraBot的插件服务实例.
 * @author Pectics
 * */
public abstract class PluginService {

    private final Logger log = Aurora.getLogger();
    private Properties config = new Properties();

    /**
     * 插件启动方法,加载时将调用.
     * @author Pectics
     * */
    abstract public void onEnable();

    /**
     * 插件停止方法,卸载时将调用.
     * @author Pectics
     * */
    abstract public void onDisable();

    /**
     * 获取插件的日志工具对象.
     * @return {@link Logger} 日志工具对象
     * @author Pectics
     * */
    public final Logger getLogger() {
        return log;
    }

    /**
     * 获取机器人的操作接口对象.
     * @return {@link ApiManager} 机器人操作接口对象
     * @author Pectics
     * */
    public final ApiManager getApi() {
        return Aurora.getApi();
    }

    /**
     * 获取插件的配置文件.
     * @return {@link Properties} 插件的配置文件
     * @author Pectics
     * */
    public final Properties getConfig() {
        return config;
    }

    /**
     * 获取插件文件夹.
     * @return {@link File} 插件文件夹
     * @author Pectics
     * */
    public final File getPluginDir() {
        return new File(Aurora.getPluginManager().getPlugin(this).getFolder());
    }

    /**
     * 导出并保存默认配置文件,若配置文件已存在则忽略.
     * @author Pectics
     * */
    public final void saveDefaultConfig() {
        Plugin plugin = Aurora.getPluginManager().getPlugin(this);
        try {
            JarFile jar = new JarFile(plugin.getPath());
            InputStream resPluginConfigIS = jar.getInputStream(jar.getEntry("config.properties"));
            File desPluginConfigFile = new File(plugin.getFolder() + "/config.properties");
            if (!desPluginConfigFile.exists()) {
                if (!desPluginConfigFile.createNewFile()) {
                    log.error("无法创建配置文件，请检查权限!");
                    log.error("插件将取消加载...");
                    Aurora.getPluginManager().unload(plugin);
                }
                FileOutputStream configFileOS = new FileOutputStream(desPluginConfigFile);
                configFileOS.write(resPluginConfigIS.readAllBytes());
                configFileOS.close();
                config.load(resPluginConfigIS);
                resPluginConfigIS.close();
            }
        } catch (IOException j) {
            log.error("未找到插件" + plugin.getName() + "的配置文件：config.properties！",
                    new PluginConfigNotFoundException(plugin));
        }
    }

    /**
     * 保存插件配置文件,将插件配置写入到文件.
     * @author Pectics
     * */
    public final void saveConfig() {
        Plugin plugin = Aurora.getPluginManager().getPlugin(this);
        try {
            File pluginConfigFile = new File(plugin.getFolder());
            config.store(new FileOutputStream(pluginConfigFile), new Date().toString());
        } catch (IOException e) {
            log.error("保存配置文件失败，请检查文件权限！", e);
        }
    }

    /**
     * 导出并保存插件资源文件.
     * @param file 资源文件在插件jar包中的相对目录
     * @author Pectics
     * */
    public final void saveResources(String file) {
        Plugin plugin = Aurora.getPluginManager().getPlugin(this);
        try {
            InputStream resResourceIS = this.getClass().getResourceAsStream("/" + file);
            File desResourceFile = new File(plugin.getFolder() + "/" + file);
            if (null == resResourceIS) {
                log.error("无法保存插件" + plugin.getName() + "的资源文件：" + file + "！");
                return;
            }
            if (!desResourceFile.exists()) {
                File desResourceDir = new File(plugin.getFolder() + "/" + file.substring(0, file.lastIndexOf("/")));
                if (!desResourceDir.exists()) if (!desResourceDir.mkdirs()) {
                    log.error("无法保存插件" + plugin.getName() + "的资源文件：" + file + "！");
                    return;
                }
                if (!desResourceFile.createNewFile()) {
                    log.error("无法保存插件" + plugin.getName() + "的资源文件：" + file + "！");
                    return;
                }
                FileOutputStream configFileOS = new FileOutputStream(desResourceFile);
                configFileOS.write(resResourceIS.readAllBytes());
                configFileOS.close();
                config.load(resResourceIS);
                resResourceIS.close();
            }
        } catch (IOException e) {
            log.error("无法保存插件" + plugin.getName() + "的资源文件：" + file + "！", e);
        }
    }

    protected final void setConfig(Properties config) {
        this.config = config;
    }

}
