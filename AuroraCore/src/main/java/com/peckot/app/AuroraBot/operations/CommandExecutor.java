package com.peckot.app.AuroraBot.operations;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.Const;
import com.peckot.app.AuroraBot.exceptions.PluginAlreadyLoadedException;
import com.peckot.app.AuroraBot.exceptions.PluginFileNotFoundException;
import com.peckot.app.AuroraBot.exceptions.PluginLoadedNotFoundException;
import com.peckot.app.AuroraBot.plugins.Plugin;
import org.slf4j.Logger;

import java.util.Scanner;

/**
 * AuroraBot控制台的指令处理类
 * @author Pectics
 * */
public class CommandExecutor {

    private static final Logger log = Aurora.getLogger();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean flag = true;

    public static void stop() { flag = false; }

    public CommandExecutor() {
        boolean arrow = false;
        while (flag) {
            if (arrow) {
                System.out.print("> ");
                arrow = false;
            }
            if (scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
                arrow = true;
            }
        }
    }

    private void executeCommand(String command) {
        String[] args = command.split(" ");
        switch (args[0]) {
            case "help":
                log.info("{} 指令帮助：", Const.NAME);
                Const.MSG_HELP.forEach(log::info);
                break;
            case "info":
                log.info("\n{} 系统信息：{}", Const.NAME, Const.MSG_INFO);
                break;
            case "exit":
                System.exit(201);
                break;
            case "reload":
                Aurora.reload();
                break;
            case "plugin":
                if (args.length <= 1) {
                    log.info("Plugin 指令帮助：");
                    Const.MSG_PLUGIN.forEach(log::info);
                    break;
                } else if (
                        !args[1].trim().equalsIgnoreCase("help") &&
                                !args[1].trim().equalsIgnoreCase("list") &&
                                !args[1].trim().equalsIgnoreCase("info") &&
                                !args[1].trim().equalsIgnoreCase("load") &&
                                !args[1].trim().equalsIgnoreCase("unload") &&
                                !args[1].trim().equalsIgnoreCase("reload")
                ) {
                    log.info("无法识别的指令！使用\"plugin help\"来获取帮助！");
                } else {
                    switch (args[1]) {
                        case "help":
                            log.info("Plugin 指令帮助：");
                            Const.MSG_PLUGIN.forEach(log::info);
                            break;
                        case "list":
                            log.info("{} 已加载的插件：", Const.NAME);
                            Aurora.getPluginManager().getLoadedPluginList().forEach(plugin ->
                                    log.info("    > {} {}", plugin.getName(), plugin.getVersion()));
                            break;
                        case "info":
                            if (args.length <= 2) {
                                log.info("Info 指令帮助：");
                                log.info("使用方式：plugin info <插件名>");
                            }
                            else {
                                Plugin plugin = Aurora.getPluginManager().getPlugin(args[2]);
                                if (null == plugin) {
                                    log.warn("未找到已加载的插件：{}", args[2]);
                                } else log.info(
                                        "\n{} 插件信息：\n插件：{} v{}\n作者：{}\n说明：{}\n网站：{}",
                                        plugin.getName(), plugin.getName(), plugin.getVersion(),
                                        plugin.getAuthor(), plugin.getDescription(), plugin.getUrl()
                                );
                            }
                            break;
                        case "load":
                            if (args.length <= 2) {
                                log.info("Load 指令帮助：");
                                log.info("使用方式：plugin load <插件名>");
                            }
                            else try {
                                Aurora.getPluginManager().load(args[2]);
                                Plugin plugin = Aurora.getPluginManager().getPlugin(args[2]);
                                if (null != plugin) log.info("已成功加载插件：{}", plugin.getName());
                                else log.warn("插件加载失败！");
                            } catch (PluginAlreadyLoadedException e) {
                                log.warn("该插件已被加载！", e);
                            } catch (PluginFileNotFoundException e) {
                                log.warn("未找到文件列表中的插件：{}", args[2], e);
                            }
                            break;
                        case "unload":
                            if (args.length <= 2) {
                                log.info("Unload 指令帮助：");
                                log.info("使用方式：plugin unload <插件名>");
                            }
                            else try {
                                Plugin plugin = Aurora.getPluginManager().getPlugin(args[2]);
                                Aurora.getPluginManager().unload(args[2]);
                                if (null != plugin) log.info("已成功卸载插件：{}", plugin.getName());
                                else log.warn("插件卸载失败！");
                            } catch (PluginLoadedNotFoundException e) {
                                log.warn("未找到已加载的的插件：{}", args[2], e);
                            }
                            break;
                        case "reload":
                            if (args.length <= 2) {
                                log.info("Reload 指令帮助：");
                                log.info("使用方式：plugin reload <插件名>");
                            }
                            else try {
                                Plugin plugin = Aurora.getPluginManager().getPlugin(args[2]);
                                if (null == plugin) {
                                    log.warn("插件{}尚未加载！", args[2]);
                                    return;
                                } else {
                                    plugin.getService().onDisable();
                                    plugin.getService().onEnable();
                                    log.info("已成功重载插件：{}", plugin.getName());
                                }
                            } catch (PluginFileNotFoundException e) {
                                log.warn("未找到文件列表中的插件：{}", args[2], e);
                            }
                            break;
                        default:
                            log.info("无法识别的指令！使用\"plugin help\"来获取帮助！");
                            break;
                    }
                    break;
                }
                break;
            default:
                log.info("无法识别的指令！使用\"help\"来获取帮助！");
                break;
        }
    }

}
