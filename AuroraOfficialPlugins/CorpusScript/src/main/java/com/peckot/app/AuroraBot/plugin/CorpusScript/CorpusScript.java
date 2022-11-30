package com.peckot.app.AuroraBot.plugin.CorpusScript;

import com.peckot.app.AuroraBot.Aurora;
import com.peckot.app.AuroraBot.plugin.CorpusScript.corpus.JSCorpusRunner;
import com.peckot.app.AuroraBot.plugins.PluginService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class CorpusScript extends PluginService {

    @Override
    public void onEnable() {
        log.info("CorpusScript启动啦！");
        Aurora.getPluginManager().setEventHandler(new JSCorpusRunner(this));
    }

    @Override
    public void onDisable() {
        log.info("CorpusScript卸载啦！");
    }

}
