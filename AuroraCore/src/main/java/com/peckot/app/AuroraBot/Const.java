package com.peckot.app.AuroraBot;

import com.peckot.app.AuroraBot.utils.Strs;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * AuroraBot常量池.
 * @author Pectics
 * */
public class Const {

    private static final Logger log = Aurora.getLogger();
    private static final Properties
            resConfigProperties = new Properties(),
            desConfigProperties = new Properties();

    /**
     * 软件版本.
     * */
    @Value("${project.version}")
    public static final String VERSION = "null";
    /**
     * 软件名称.
     * */
    @Value("${project.name}")
    public static final String NAME = "AuroraBot";
    /**
     * 软件GitHub开源地址.
     * */
    @Value("${project.url}")
    public static final String GITHUB = "https://github.com/Pectics/AuroraBot";
    /**
     * 软件作者.
     * */
    public static final String AUTHOR = "Pectics";
    /**
     * 软件简介.
     * */
    public static final String DESCRIPTION = "Peckot Studios QQChannelBot.";
    /**
     * 软件网站.
     * */
    public static final String WEBSITE = "https://peckot.com";

    /**
     * 控制台消息: 软件信息.
     * */
    public static final String MSG_INFO = "\n" +
            "当前运行版本：" + VERSION + "\n" +
            "软件唯一作者：" + AUTHOR + "\n" +
            "本软件使用以GPL-3.0协议开源的QQBotSdk框架：\n" +
            "    https://github.com/xiaoye-bot/qqbot-sdk\n" +
            "本软件使用MIT协议开源：\n" +
            "    " + GITHUB + "\n" +
            "\033[0;31m注意：本软件仅供开发者学习交流, 严禁商用！\033[m";
    /**
     * 控制台消息: 帮助信息.
     * */
    public static List<String> MSG_HELP = new LinkedList<>();
    /**
     * 控制台消息: 插件帮助信息.
     * */
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

    /**
     * 配置文件信息: 频道ID.
     * */
    public static String GUILD_ID;
    /**
     * 配置文件信息: 机器人应用程序ID.
     * */
    public static int APP_ID;
    /**
     * 配置文件信息: 机器人应用程序Token.
     * */
    public static String APP_TOKEN;
    /**
     * 配置文件信息: 是否使用沙盒模式启动.
     * */
    public static boolean SANDBOX;

    private void loadConst() {
        log.debug("获取配置文件内容...");
        log.debug("Guild id：{}",
                GUILD_ID = getValueOrDefault("guild-id", "Your-Guild-Id-Here"));
        log.debug("App id：{}", Strs.encryptAfterThree(String.valueOf(
                APP_ID = Integer.parseInt(getValueOrDefault("app-id", "0"))), 3, '*'));
        log.debug("App token：{}", Strs.encryptAfterThree(String.valueOf(
                APP_TOKEN = getValueOrDefault("app-token", "Your-App-Token-Here")), 5, '*'));
        log.debug("Sandbox：{}",
                SANDBOX = Boolean.parseBoolean(getValueOrDefault("sandbox-mode.enabled", "true")));
    }

    /**
     * 初始化配置文件和常量池.
     * @author Pectics
     * */
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

    private static String getValueOrDefault(String key, String defaultValue) {
        return String.valueOf(desConfigProperties.getOrDefault(key, resConfigProperties.getOrDefault(key, defaultValue)));
    }

}
