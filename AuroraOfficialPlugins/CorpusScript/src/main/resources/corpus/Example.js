// MessageType: COMMON, Priority: NORMAL
function Example() {
    let String = Java.type('java.lang.String');
    var content = Message.getContent();
    var channel = Message.getChannelId();
    Logger.info('脚本Example加载成功！');
    if (String.valueOf(content) == 'test') {
        Logger.info('开始发送消息！');
        API.getMessageApi().sendMessage(channel, 'test success!', Message.getId());
    }
}