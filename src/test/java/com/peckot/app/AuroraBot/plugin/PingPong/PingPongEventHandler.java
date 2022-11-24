package com.peckot.app.AuroraBot.plugin.PingPong;

import me.zhenxin.qqbot.api.ApiManager;
import me.zhenxin.qqbot.entity.Message;
import me.zhenxin.qqbot.event.UserMessageEvent;
import me.zhenxin.qqbot.exception.ApiException;
import me.zhenxin.qqbot.websocket.EventHandler;
import org.slf4j.Logger;

public class PingPongEventHandler extends EventHandler {

    private final ApiManager api;
    private final Logger log;

    public PingPongEventHandler(PingPong plugin) {
        this.api = plugin.getApi();
        this.log = plugin.getLogger();
    }

    @Override
    protected void onError(Exception e) {
        log.error("发生错误: {}", e.getMessage());
    }

    @Override
    protected void onUserMessage(UserMessageEvent event) {
        Message message = event.getMessage();
        String channelId = message.getChannelId();
        String content = message.getContent();
        String messageId = message.getId();
        try {
            String[] args = content.split(" ");
            String command = args[0];
            if ("ping".equals(command)) {
                api.getMessageApi().sendMessage(
                        channelId,
                        "pong",
                        messageId
                );
            }
        } catch (ApiException e) {
            log.error("消息处理发生异常: {} {}({})", e.getCode(), e.getMessage(), e.getError());
            api.getMessageApi().sendMessage(channelId, "消息处理失败: " + e.getMessage(), messageId);
        }
    }

}