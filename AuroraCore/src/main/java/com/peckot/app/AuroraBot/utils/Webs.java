package com.peckot.app.AuroraBot.utils;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 提供了常用的网络请求方法.
 * @author Pectics
 * */
public final class Webs {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * 通过get请求获取网络信息.
     * @param url 获取信息的地址
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpGet(String url) {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的get请求获取网络信息.
     * @param url           获取信息的地址
     * @param headerKey     头部信息的键
     * @param headerValue   头部信息的值
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpGet(String url, String headerKey, String headerValue) {
        HttpGet request = new HttpGet(url);
        request.setHeader(headerKey, headerValue);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的get请求获取网络信息.
     * @param url       获取信息的地址
     * @param headers   头部信息的{@link Map}对象
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpGet(String url, Map<String, String> headers) {
        HttpGet request = new HttpGet(url);
        for (Map.Entry<String, String> entry : headers.entrySet()){
            request.setHeader(entry.getKey(), entry.getValue());
        }
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的get请求获取网络信息.
     * @param url       获取信息的地址
     * @param header    头部信息对象
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpGet(String url, Header header) {
        HttpGet request = new HttpGet(url);
        request.setHeader(header);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的get请求获取网络信息.
     * @param url       获取信息的地址
     * @param headers   多个头部信息对象
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpGet(String url, Header... headers) {
        HttpGet request = new HttpGet(url);
        for (Header header : headers) request.setHeader(header);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过post请求获取网络信息.
     * @param url 获取信息的地址
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpPost(String url) {
        HttpPost request = new HttpPost(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的post请求获取网络信息.
     * @param url           获取信息的地址
     * @param headerKey     头部信息的键
     * @param headerValue   头部信息的值
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpPost(String url, String headerKey, String headerValue) {
        HttpPost request = new HttpPost(url);
        request.setHeader(headerKey, headerValue);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的post请求获取网络信息.
     * @param url       获取信息的地址
     * @param headers   头部信息的{@link Map}对象
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpPost(String url, Map<String, String> headers) {
        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> entry : headers.entrySet()){
            request.setHeader(entry.getKey(), entry.getValue());
        }
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的post请求获取网络信息.
     * @param url       获取信息的地址
     * @param header    头部信息对象
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpPost(String url, Header header) {
        HttpPost request = new HttpPost(url);
        request.setHeader(header);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 通过带header的post请求获取网络信息.
     * @param url       获取信息的地址
     * @param headers   多个头部信息对象
     * @return {@link String} 请求到的内容,若出现错误则返回{@code "Error: 错误信息"}
     * @author Pectics
     * */
    public static String httpPost(String url, Header... headers) {
        HttpPost request = new HttpPost(url);
        for (Header header : headers) request.setHeader(header);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

}

