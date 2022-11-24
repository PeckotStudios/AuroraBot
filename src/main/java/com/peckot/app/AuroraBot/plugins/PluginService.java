package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.exceptions.PluginConfigNotFoundException;
import me.zhenxin.qqbot.api.ApiManager;
import org.slf4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.jar.JarFile;

public abstract class PluginService {

    private final Logger log = Aurora.getLogger();
    private Properties config = new Properties();

    abstract public void onEnable();

    abstract public void onDisable();

    public final Logger getLogger() {
        return log;
    }

    public final ApiManager getApi() {
        return Aurora.getApi();
    }

    public final Properties getConfig() {
        return config;
    }

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

    public final void saveConfig() {
        Plugin plugin = Aurora.getPluginManager().getPlugin(this);
        try {
            File pluginConfigFile = new File(plugin.getFolder());
            config.store(new FileOutputStream(pluginConfigFile), new Date().toString());
        } catch (IOException e) {
            log.error("保存配置文件失败，请检查文件权限！", e);
        }
    }

    protected final void setConfig(Properties config) {
        this.config = config;
    }

}
