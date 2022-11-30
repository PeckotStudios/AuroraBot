package com.peckot.app.AuroraBot.plugin.CorpusScript.corpus;

import com.peckot.app.AuroraBot.Aurora;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.zhenxin.qqbot.entity.Message;
import org.apache.commons.io.FileUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

@Getter
@Slf4j
public class JSCorpus {

    private final String name;
    private final String jsCode;

    public enum Priority { HIGHEST, HIGH, NORMAL, LOW, LOWEST, MONITOR }

    private Priority priority;

    public enum MessageType { COMMON, DIRECT, AT }

    private MessageType messageType;

    public JSCorpus(String name, String jsCode) {
        this.name = name;
        this.jsCode = "(" + jsCode + ")";
        int enter = jsCode.indexOf("\n");
        String jsHeader = enter > -1 ? jsCode.substring(0, enter) : jsCode;
        jsHeader = jsHeader.startsWith("//") ? jsHeader.substring(2).trim() : null;
        if (null == jsHeader) {
            priority = Priority.NORMAL;
            messageType = MessageType.COMMON;
        } else {
            for (String header : jsHeader.split(",")) {
                String[] entry = header.split(":");
                switch (entry[0].trim().toUpperCase(Locale.ROOT)) {
                    case "PRIORITY":
                        priority = Priority.valueOf(entry[1].trim());
                        break;
                    case "MESSAGETYPE":
                        messageType = MessageType.valueOf(entry[1].trim());
                        break;
                }
                if (null == priority) priority = Priority.NORMAL;
                if (null == messageType) messageType = MessageType.COMMON;
            }
        }
    }

    public JSCorpus(String name, File jsFile) throws IOException {
        this(name, FileUtils.readFileToString(jsFile, "utf-8"));
    }

    private void bindObjects(Value binder, Message message) {
        binder.putMember("Logger", log);
        binder.putMember("API", Aurora.getApi());
        binder.putMember("Message", message);
    }

    public void runAction(Message message)  {
        log.warn("尝试执行：" + name);
        try (Context context = Context.newBuilder("js").allowAllAccess(true).build()) {
            bindObjects(context.getBindings("js"), message);
            context.eval("js", jsCode).execute();
        }
    }

}
