package com.peckot.app.AuroraBot.plugin.CorpusScript.corpus;

import com.peckot.app.AuroraBot.plugin.CorpusScript.CorpusScript;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.zhenxin.qqbot.event.AtMessageEvent;
import me.zhenxin.qqbot.event.DirectMessageEvent;
import me.zhenxin.qqbot.event.UserMessageEvent;
import me.zhenxin.qqbot.websocket.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
@Slf4j
public class JSCorpusRunner extends EventHandler {

    private final CorpusScript plugin;

    private List<JSCorpus> corpusList = new LinkedList<>();

    public JSCorpusRunner(CorpusScript plugin) {
        this.plugin = plugin;
        System.setProperty("-Dpolyglot.log.file", new File(plugin.getPluginDir(), "logs.txt").getPath());
        System.setProperty("-Dpolyglot.engine.WarnInterpreterOnly", "false");
        loadCorpusList();
    }

    private void loadCorpusList() {
        plugin.saveResources("corpus/Example.js");
        File[] files = new File(plugin.getPluginDir(), "corpus").listFiles();
        if (null == files) return;
        for (File file : files) {
            if (file.isDirectory() || !file.getPath().endsWith(".js")) continue;
            try {
                int dot = file.getName().lastIndexOf(".");
                corpusList.add(new JSCorpus(dot > -1 ? file.getName().substring(0, dot) : file.getName(), file));
            } catch (IOException e) {
                log.error("无法访问语料库文件：{}", file.getPath(), e);
            }
        }
        corpusList.sort(Comparator.comparing(JSCorpus::getPriority));
        log.warn("语料库加载完毕：" + corpusList.get(0).getMessageType());
    }

    @Override
    protected void onUserMessage(UserMessageEvent event) {
        corpusList.forEach(c -> {
            if (c.getMessageType() == JSCorpus.MessageType.COMMON) c.runAction(event.getMessage());
        });
    }

    @Override
    protected void onDirectMessage(DirectMessageEvent event) {
        corpusList.forEach(c -> {
            if (c.getMessageType() == JSCorpus.MessageType.DIRECT) c.runAction(event.getMessage());
        });
    }

    @Override
    protected void onAtMessage(AtMessageEvent event) {
        corpusList.forEach(c -> {
            if (c.getMessageType() == JSCorpus.MessageType.AT) c.runAction(event.getMessage());
        });
    }

}
