package com.peckot.app.AuroraBot.plugins;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.exceptions.PluginFileNotFoundException;
import com.peckot.app.AuroraBot.exceptions.PluginPropertiesNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class PluginLoader {

    private static final Logger log = Aurora.getLogger();
    private static final File parentDir = new File("plugins");

    /**
     * 遍历插件文件夹并加载单个插件
     * @param pluginName 插件名称
     * @return 插件实例
     * */
    @Nullable
    protected static Plugin load(@NotNull String pluginName) throws PluginFileNotFoundException {
        File[] files = parentDir.listFiles();
        // 插件目录为空
        if (null == files || files.length == 0) throw new PluginFileNotFoundException(pluginName);
        // 遍历插件目录
        Plugin plugin = null;
        for (File dir : files) {
            // 仅选择第一个文件名匹配的.jar文件
            if (    !dir.isDirectory() &&
                    dir.getPath().endsWith(".jar") &&
                    dir.getName().toUpperCase(Locale.ROOT).contains(pluginName.toUpperCase(Locale.ROOT))
            ) { try {
                    plugin = readPlugin(dir.getPath());
                } catch (IOException e) {
                    log.error("无法加载插件：{}", dir.getPath(), e);
                    return null;
                } catch (PluginPropertiesNotFoundException e) {
                    log.error("无法读取插件plugin.properties：{}", e.getPluginName(), e);
                    return null;
                } catch (ReflectiveOperationException e) {
                    log.error("无法加载插件主类：{}", e.getClass().getName(), e);
                    return null;
                }
            } else {
                throw new PluginFileNotFoundException(pluginName);
            }
        }
        // 加载插件文件目录
        File configDir = new File(parentDir, plugin.getName());
        if (!configDir.exists()) if (!new File(parentDir, plugin.getName()).mkdir()) {
            log.error("无法创建插件目录：{}({})", plugin.getName(), plugin.getPath());
            return null;
        }
        return plugin;
    }

    /**
     * 遍历插件目录，读取插件并返回插件实例列表
     * @return 可用插件实例列表
     */
    @NotNull
    protected static List<Plugin> loadAll() {
        List<Plugin> plugins = new ArrayList<>();
        // 获取插件目录下所有文件
        File[] files = parentDir.listFiles();
        // 插件目录为空
        if (null == files) {
            return Collections.emptyList();
        }
        Plugin plugin;
        // 遍历插件目录
        for (File dir : files) {
            // 忽略文件夹和非.jar文件
            if (dir.isDirectory() || !dir.getPath().endsWith(".jar")) continue;
            try {
                plugin = readPlugin(dir.getPath());
            } catch (IOException e) {
                log.error("无法加载插件：{}", dir.getPath(), e);
                continue;
            } catch (PluginPropertiesNotFoundException e) {
                log.error("无法读取插件plugin.properties：{}", e.getPluginName(), e);
                continue;
            } catch (ReflectiveOperationException e) {
                log.error("无法加载插件主类：{}", e.getClass().getName(), e);
                continue;
            }
            // 加载插件文件目录
            File configDir = new File(parentDir, plugin.getName());
            if (!configDir.exists()) if (!new File(parentDir, plugin.getName()).mkdir()) {
                log.error("无法创建插件目录：{}({})", plugin.getName(), plugin.getPath());
                continue;
            }
            plugins.add(plugin);
        }
        return plugins;
    }

    /**
     * 从插件jar包中读取properties到Plugin类
     * @param path jar 相对路径
     */
    @NotNull
    private static Plugin readPlugin(String path) throws IOException, ReflectiveOperationException, PluginPropertiesNotFoundException {
        JarFile jarFile = new JarFile(path);
        ZipEntry zipEntry = jarFile.getEntry("plugin.properties");
        // 插件不包含plugin.properties文件
        if (null == zipEntry) throw new PluginPropertiesNotFoundException(jarFile.getName());
        // 读取插件properties
        InputStream inputStream = jarFile.getInputStream(zipEntry);
        Properties pluginProperties = new Properties();
        pluginProperties.load(inputStream);
        // 生成插件实例
        Plugin plugin = new Plugin(
                pluginProperties.getProperty("name"),
                pluginProperties.getProperty("version"),
                pluginProperties.getProperty("author"),
                pluginProperties.getProperty("mainClass"),
                pluginProperties.getProperty("description"),
                pluginProperties.getProperty("url"), path
        );
        log.info("读取插件: " + plugin.getName() + " " + plugin.getVersion());
        // 反射获取插件主类实例
        URL[] url = new URL[] { new URL("file:" + path) };
        PluginService pluginService = (PluginService) new URLClassLoader(url)
                .loadClass(plugin.getMainClass()).getDeclaredConstructor().newInstance();
        File configFile = new File(parentDir, plugin.getName() + "/config.properties");
        if (configFile.exists()) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(configFile));
            pluginService.setConfig(properties);
        }
        // 设置插件服务主类
        plugin.setService(pluginService);
        return plugin;
    }

}
