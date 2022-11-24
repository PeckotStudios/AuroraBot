package com.peckot.app.AuroraBot;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;

public class Const {

    private static final Logger log = Aurora.getLogger();
    private static final Properties
            resConfigProperties = new Properties(),
            desConfigProperties = new Properties();

    @Value("${project.version}")
    public static final String VERSION = "null";
    @Value("${project.name}")
    public static final String NAME = "AuroraBot";
    @Value("${project.url}")
    public static final String GITHUB = "https://github.com/Pectics/AuroraBot";
    public static final String
            AUTHOR = "Pectics",
            DESCRIPTION = "Peckot Studios QQChannelBot.",
            WEBSITE = "https://peckot.com";

    public static final String MSG_INFO = "\n" +
            "当前运行版本：" + VERSION + "\n" +
            "软件唯一作者：" + AUTHOR + "\n" +
            "本软件使用以GPL-3.0协议开源的QQBotSdk框架：\n" +
            "    https://github.com/xiaoye-bot/qqbot-sdk\n" +
            "本软件使用MIT协议开源：\n" +
            "    " + GITHUB + "\n" +
            "\033[0;31m注意：本软件仅供开发者学习交流, 严禁商用！\033[m";
    public static List<String> MSG_HELP = new LinkedList<>();
    public static List<String> MSG_PLUGIN = new LinkedList<>();

    static {
        MSG_HELP.add("> help\t - 显示当前帮助页面");
        MSG_HELP.add("> info\t - 显示软件相关信息");
        MSG_HELP.add("> exit\t - 安全停止并退出");
        MSG_HELP.add("> reload\t - 重新启动并登录");
        MSG_HELP.add("> plugin\t - 查看插件管理帮助");
        MSG_PLUGIN.add("> plugin [help]\t - 显示当前帮助页面");
        MSG_PLUGIN.add("> plugin list\t - 查看当前已加载的插件");
        MSG_PLUGIN.add("> plugin info <插件名>\t - 查看插件详细信息");
        MSG_PLUGIN.add("> plugin load <插件名>\t - 加载指定插件");
        MSG_PLUGIN.add("> plugin unload <插件名>\t - 卸载指定插件");
        MSG_PLUGIN.add("> plugin reload <插件名>\t - 重新加载指定插件");
    }

    public static String GUILD_ID;
    public static int APP_ID;
    public static String APP_TOKEN;
    public static boolean SANDBOX;

    private void loadConst() {
        log.debug("获取配置文件内容...");
        log.debug("Guild id：{}", GUILD_ID = getValueOrDefault("guild-id", "Your-Guild-Id-Here"));
        log.debug("App id：{}", Utils.encryptAfterThree(String.valueOf(
                APP_ID = getValueOrDefault("app-id", 0)
        ), 3));
        log.debug("App token：{}", Utils.encryptAfterThree(String.valueOf(
                APP_TOKEN = getValueOrDefault("app-token", "Your-App-Token-Here")
        ), 5));
        log.debug("Sandbox：{}", SANDBOX = getValueOrDefault("sandbox-mode.enabled", true));
    }

    public Const() {
        try {
            String selfPath = Const.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            JarFile jar;
            InputStream resConfigIS;
            if (selfPath.endsWith(".jar")) {
                jar = new JarFile(selfPath);
                resConfigIS = jar.getInputStream(jar.getEntry("config.properties"));
            } else {
                resConfigIS = new FileInputStream(new File(selfPath, "config.properties"));
            }
            File desConfigFile = new File("config.properties");
            if (!desConfigFile.exists()) {
                log.debug("未找到配置文件，正在创建默认配置文件...");
                if (!desConfigFile.createNewFile()) {
                    log.error("无法创建配置文件，请检查运行目录权限!");
                    log.error("程序即将退出...");
                    System.exit(1);
                }
                FileOutputStream configFileOS = new FileOutputStream(desConfigFile);
                configFileOS.write(resConfigIS.readAllBytes());
                configFileOS.close();
                resConfigIS.close();
                log.info("默认配置文件已创建，请修改有关内容后重新启动程序!");
                log.error("程序将退出...");
                System.exit(0);
            } else if (DigestUtils.md5Hex(resConfigIS).equals(DigestUtils.md5Hex(new FileInputStream(desConfigFile)))) {
                resConfigIS.close();
                log.info("请修改配置文件(config.properties)有关内容后重新启动程序!");
                log.error("程序将退出...");
                System.exit(0);
            }
            FileInputStream temp = new FileInputStream(desConfigFile);
            resConfigProperties.load(resConfigIS);
            desConfigProperties.load(temp);
            temp.close();
            loadConst();
        } catch (IOException e) {
            log.error("严重错误！程序退出...", e);
            System.exit(500);
        }
        File pluginsDir = new File("plugins");
        if (!pluginsDir.exists()) if (!pluginsDir.mkdir()) {
            log.error("无法创建plugins文件夹，请检查权限!");
            log.error("程序将退出...");
            System.exit(501);
        }
    }

    public static String getValueOrDefault(String key, String defaultValue) {
        return String.valueOf(desConfigProperties.getOrDefault(key, resConfigProperties.getOrDefault(key, defaultValue)));
    }

    public static Integer getValueOrDefault(String key, Integer defaultValue) {
        return Integer.valueOf((String)desConfigProperties.getOrDefault(key, resConfigProperties.getOrDefault(key, defaultValue)));
    }

    public static Boolean getValueOrDefault(String key, Boolean defaultValue) {
        return Boolean.valueOf((String)desConfigProperties.getOrDefault(key, resConfigProperties.getOrDefault(key, defaultValue)));
    }

    private static <T> T getValue(String key, Class<T> type) {
        return type.cast(desConfigProperties.getOrDefault(key, resConfigProperties.get(key)));
    }

    private static <T> T getValueOrDefault(String key, Class<T> type, Object defaultValue) {
        return type.cast(desConfigProperties.getOrDefault(key, resConfigProperties.getOrDefault(key, defaultValue)));
    }

}
