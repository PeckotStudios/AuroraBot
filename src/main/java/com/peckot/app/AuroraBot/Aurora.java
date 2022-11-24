package com.peckot.app.AuroraBot;

import com.peckot.app.AuroraBot.operations.CommandExecutor;
import com.peckot.app.AuroraBot.plugins.PluginManager;
import me.zhenxin.qqbot.api.ApiManager;
import me.zhenxin.qqbot.core.BotCore;
import me.zhenxin.qqbot.entity.AccessInfo;
import me.zhenxin.qqbot.enums.Intent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class Aurora {

    private static final Logger log = LoggerFactory.getLogger(Aurora.class);
    private static ApiManager api;
    private static PluginManager pluginManager;

    private static Thread thread;
    private static Thread executor;
    private static boolean exitFlag = false;

    public static void startBot() {
        log.debug("初始化常量内容...");
        log.debug("常量池实例对象：{}", new Const());
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setBotAppId(Const.APP_ID);
        accessInfo.setBotToken(Const.APP_TOKEN);
        log.debug("沙盒模式状态：{}", Const.SANDBOX);
        if (Const.SANDBOX) accessInfo.useSandBoxMode();
        BotCore bot;
        log.debug("机器人实例对象：{}", bot = new BotCore(accessInfo));
        api = bot.getApiManager();
        Intent[] intents = new Intent[] {
                Intent.AT_MESSAGES,
                Intent.USER_MESSAGES,
                Intent.GUILDS,
                Intent.GUILD_MEMBERS,
                Intent.GUILD_MESSAGE_REACTIONS,
                Intent.DIRECT_MESSAGE
        };
        log.debug("进行机器人权限申请，权限列表：{}",
                Arrays.stream(intents).map(Intent::toString).collect(Collectors.joining(", ")));
        bot.registerIntents(intents);
        log.debug("插件管理器实例化对象：{}", pluginManager = new PluginManager(bot));
        log.info("开始运行插件...");
        pluginManager.loadAll();
        log.info("插件加载完毕！");
        bot.start();
        try {
            Thread.sleep(1145);
            log.info("AuroraBot启动完毕！使用\"help\"来获取帮助！");
            if (!executor.isAlive()) executor.start();
        } catch (InterruptedException e) {
            log.error("严重错误！程序退出...", e);
            System.exit(500);
        }
    }

    public static void reload() {
        log.debug("重新加载程序...");
        try {
            Thread.sleep(1145);
        } catch (InterruptedException e) {
            log.error("严重错误！程序退出...", e);
            System.exit(500);
        }
        log.debug("开始关闭程序...");
        (thread = new Thread(() -> {
            pluginManager.unloadAll();
            ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
            Thread[] threads = new Thread[currentGroup.activeCount()];
            currentGroup.enumerate(threads);
            Arrays.stream(threads).filter(t -> t.getName().equals("WebSocket")).forEach(Thread::interrupt);
            log.debug("正在重新启动程序...");
            startBot();
        })).setName("AuroraBot");
        thread.start();
    }

    /**
     * 主方法
     */
    public static void main(String[] args) {
        log.debug("初始化主线程...");
        (thread = Thread.currentThread()).setName("AuroraBot");
        log.info("欢迎使用AuroraBot！");
        (executor = new Thread(CommandExecutor::new)).setName("Executor");
        log.debug("主线程开始运行...");
        startBot();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Thread.currentThread().setName("ExitThread");
            log.info("AuroraBot正在停止...");
            CommandExecutor.stop();
            pluginManager.unloadAll();
            log.info("停止主线程...");
            if (!thread.isInterrupted()) thread.interrupt();
            log.info("AuroraBot已停止");
            exitFlag = true;
        }));
        while (!exitFlag) {
            if (!thread.isAlive()) {
                break;
            }
        }
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static Logger getLogger() {
        return log;
    }

    public static ApiManager getApi() {
        return api;
    }

}
