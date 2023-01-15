package com.peckot.app.AuroraBot.plugin.AuroraMusic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.peckot.app.AuroraBot.utils.Webs;
import lombok.Getter;
import lombok.Setter;
import me.zhenxin.qqbot.api.ApiManager;
import me.zhenxin.qqbot.entity.AudioControl;
import me.zhenxin.qqbot.entity.Message;
import me.zhenxin.qqbot.event.UserMessageEvent;
import me.zhenxin.qqbot.websocket.EventHandler;

import java.util.*;

public class CommandExecutor extends EventHandler {

    private static final String searchUrl = "http://music.163.com/api/search/pc?s={key}&limit=9&type=1";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.76";
    private static final String cookie = "_iuqxldmzr_=32; _ntes_nnid=c88f7ce7d14cdea965b30661c3943469,1669290936482; _ntes_nuid=c88f7ce7d14cdea965b30661c3943469; NMTID=00OU1z4MgjNiT7p1kNBs6PC0C1qhnwAAAGEqX4m-g; WEVNSM=1.0.0; WNMCID=dgqnfh.1669290939671.01.0; WM_TID=djhOLmm+KYNFEBVAAFbEJIiDLrlazLWE; __bid_n=184c1f0b973e02a1614207; P_INFO=\"peckotstudios@163.com|1670420866|0|unireg|00&99|null&null&null#chq&null#10#0#0|&0||peckotstudios@163.com\"; JSESSIONID-WYYY=Hf2vPnv/lF1g3kezZTJ7K9cyAt5bt5P/0Qgzdz2VXzhVuEB5JIjD4ekv2quPh3JOjFovltz\\l7VP/YnZRrOK+9b2+T5hte/TkgTxr4132+BwC+X4hJOPAFiRDoGfsWM3KMHn04ZP9lbbe1OqWxmdQYJPqsvRTfTFb2\\4HUHFCg8uHqW9:1673277818850; WM_NI=1Q7zaKTHbMLeIxWs8WYsPfYqnXMor/tQNJmpWjqTWdSz9UzhvAo9Fw6XEnQxFYsEz8j5jUpra+dKS6tiAocXxoaJD0I5gx/t6oDngk7jd9dllyNKD2hM2TyL0BLwfjtGQlM=; WM_NIKE=9ca17ae2e6ffcda170e2e6eea3fb3bb5aa98a6e54993a88fb2d84e938f9b82c553f787e1b7db45b1f587aacc2af0fea7c3b92af38ff99be4419898aeaecf6f8ba89994bb5d9bada087dc7bae888390f125b88f82abf07c95b6bfb5d67091a6f7b6f964b69fa1dac57df6888b8fd4748687bda6f934aaeba6b6f37aa3f5acacd23eb8b78f90e254bcb1afabc73c8beba999ae80f6b18c8fe661f5b6acdae768b0a782acc73aac93feb5fc48f49e8498e562b8999f8dd037e2a3";

    private final AuroraMusic plugin;
    private final ApiManager api;

    private final Map<String, List<Music>> musicInfo = new HashMap<>();

    @Getter
    @Setter
    static class Music {
        int id;
        String name;
        String artists;
        String picUrl;
        String mp3Url;
    }

    public CommandExecutor(AuroraMusic plugin) {
        this.plugin = plugin;
        this.api = plugin.getApi();
    }

    private List<Music> searchMusic(String guildId, String key) {
        String data = Webs.getInstance().httpGet(
                searchUrl.replaceFirst("\\{key}", key),
                Map.of(
                        "User-Agent", userAgent,
                        "Content-Type", "application/json;charset=UTF-8",
                        "Cookie", cookie
                )
        );
        JSONArray songs = JSON.parseObject(data).getJSONObject("result").getJSONArray("songs");
        List<Music> result = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            Music music = new Music();
            JSONObject song = songs.getJSONObject(i);
            JSONArray songArtists = song.getJSONArray("artists");
            music.setId(song.getInteger("id"));
            music.setName(song.getString("name"));
            music.setMp3Url(song.getString("mp3Url"));
            music.setPicUrl(song.getJSONObject("album").getString("picUrl"));
            StringBuilder artists = new StringBuilder();
            for (int j = 0; j < songArtists.size(); j++) {
                artists.append(" & ").append(songArtists.getJSONObject(j).getString("name"));
            }
            music.setArtists(artists.substring(3));
            result.add(music);
        }
        //musicInfo.put(guildId, result);
        return result;
    }

    @Override
    protected void onUserMessage(UserMessageEvent event) {
        Message message = event.getMessage();
        String guildId = message.getGuildId();
        String channelId = message.getChannelId();
        String content = message.getContent();
        if (content.startsWith("/music")) {
            String[] commands = content.substring(6).trim().split(" ");
            AudioControl control = new AudioControl();
            switch (commands[0]) {
                case "on":
                    api.getAudioApi().openMic(channelId);
                    break;
                case "off":
                    api.getAudioApi().closeMic(channelId);
                    break;
                case "start":
                    control.setStatus(2);
                    api.getAudioApi().audioControl(channelId, control);
                    break;
                case "pause":
                    control.setStatus(1);
                    api.getAudioApi().audioControl(channelId, control);
                    break;
                case "stop":
                    control.setStatus(3);
                    api.getAudioApi().audioControl(channelId, control);
                    break;
                case "search":
                    if (commands.length == 1) {
                        api.getMessageApi().sendMessage(channelId, "使用/music search [关键词]来搜索音乐", message.getId());
                    }
                    List<Music> musics = searchMusic(guildId, commands[1]);
                    StringBuilder searchFeedback = new StringBuilder();
                    for (int i = 0; i < musics.size(); i++) {
                        Music music = musics.get(i);
                        searchFeedback.append("\n").append(i + 1).append(". ").append(music.getName())
                                .append(" - ").append(music.getArtists());
                    }
                    api.getMessageApi().sendMessage(channelId, searchFeedback.substring(1), message.getId());
                    api.getMessageApi().sendMessage(channelId, "使用/music [序号]进行播放", message.getId());
                    break;
                default:

                    break;
            }
        }
    }

//    public static void main(String[] args) {
//        List<Music> musics = searchMusic("", "Tempestissimo");
//        StringBuilder searchFeedback = new StringBuilder();
//        for (int i = 0; i < musics.size(); i++) {
//            Music music = musics.get(i);
//            searchFeedback.append("\n").append(i + 1).append(". ").append(music.getName())
//                    .append(" - ").append(music.getArtists());
//        }
//        System.out.println(searchFeedback.substring(1));
//    }
}
