package com.joker.just4fun.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.joker.just4fun.config.HttpRespHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * Http请求处理工具类
 *
 * @author Joker.Y
 * @since 2024/4/8
 * @version 1.0
 */
@Slf4j
public class HttpUtil {

    private static final ConnectionConfig connConf = ConnectionConfig.custom()
            .setConnectTimeout(Timeout.ofMilliseconds(5000L)) //连接请求超时时间
            .setSocketTimeout(5000, TimeUnit.MILLISECONDS) //数据传输超时时间
            .build();

    private static final SocketConfig sktConf = SocketConfig.custom()
            .setTcpNoDelay(true) //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
            .setSoReuseAddress(true) //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
            .setSoTimeout(Timeout.ofMilliseconds(500L)) //接收数据的等待超时时间
            .setSoLinger(60, TimeUnit.SECONDS) //关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
            .setSoKeepAlive(true) //开启监视TCP连接是否有效
            .build();

    private static final RequestConfig reqConf = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(5000L)) //从连接池获取连接的超时时间
            .setResponseTimeout(5000, TimeUnit.MILLISECONDS) //请求响应超时时间
            .setCookieSpec("rfc6265") //cookie设置严格模式
            .build();

    private static CloseableHttpClient client = null;

    static {
        PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(200) //最大连接数
                .setMaxConnPerRoute(100) //每个路由的最大连接数
//                .setDefaultConnectionConfig(ConnectionConfig.DEFAULT)
                .setDefaultConnectionConfig(connConf)
//                .setDefaultSocketConfig(SocketConfig.DEFAULT)
                .setDefaultSocketConfig(sktConf)
                .build();

        final CookieStore cookieStore = new BasicCookieStore();

        try {
            client = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultCookieStore(cookieStore)
                    .setDefaultRequestConfig(reqConf)
//                    .setProxy(new HttpHost(""))
                    .build();
        } catch (Exception e) {
            log.info("===============>HttpClient初始化失败：{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (null != client){
                try {
                    client.close();
                } catch (IOException e) {
                    log.info("===============>关闭Httpclient失败：{}", e.getMessage());
                }
            }
        }
    }

    private static final HttpRespHandler respHandler = new HttpRespHandler();

    /**
     * get请求
     * @param url
     * @return
     */
    public static String get(String url){
        return getWithHeadersAndParams(url, null, null);
    }

    /**
     * 带头带参的get请求
     * @param url
     * @param headers
     * @param reqParams
     * @return
     */
    public static String getWithHeadersAndParams(String url, Map<String, Object> headers, Map<String, Object> reqParams){
        if (StringUtils.isBlank(url)){
            log.info("=============== get请求url为空!! ===============");
            return "request url cannot be empty!!";
        }
        String resp = "";
        ClassicHttpRequest get = new HttpGet(url);
        try {
            if (CollectionUtil.isNotEmpty(headers)){
                for(String key : headers.keySet()){
                    get.addHeader(key, headers.get(key));
                }
            }
            if (CollectionUtil.isNotEmpty(reqParams)){
                List<NameValuePair> nameValuePairs = new LinkedList<>();
                for(String key : reqParams.keySet()){
                    nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(reqParams.get(key))));
                }
                URI uri = new URIBuilder(new URI(url))
                        .addParameters(nameValuePairs)
                        .build();
                get.setUri(uri);
            }
            resp = client.execute(get, respHandler);
        } catch (IOException | URISyntaxException e) {
            String msg = StringUtils.isBlank(resp) ? e.getMessage() : resp;
            log.error("================>请求异常!!\tmethod: GET,\turl: " + url + "\tmsg: {}", msg);
            throw new RuntimeException(e);
        }
        return resp;
    }

    /**
     * post请求
     * @param url
     * @return
     */
    public static String post(String url){
        return postFormData(url, null, null);
    }

    /**
     * 表单参数形式的post请求
     * @param url
     * @param headers
     * @param formData
     * @return
     */
    public static String postFormData(String url, Map<String, Object> headers, Map<String, Object> formData){
        if (StringUtils.isBlank(url)){
            log.info("=============== post请求url为空!! ===============");
            return "request url cannot be empty!!";
        }
        String resp = "";
        ClassicHttpRequest post = new HttpPost(url);
        try {
            if (CollectionUtil.isNotEmpty(headers)){
                for(String key : headers.keySet()){
                    post.addHeader(key, headers.get(key));
                }
            }
            if (CollectionUtil.isNotEmpty(formData)){
                List<NameValuePair> nameValuePairs = new LinkedList<>();
                for(String key : formData.keySet()){
                    nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(formData.get(key))));
                }
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            resp = client.execute(post, respHandler);
        } catch (IOException e) {
            String msg = StringUtils.isBlank(resp) ? e.getMessage() : resp;
            log.error("================>请求异常!!\tmethod: POST,\turl: " + url + ",\tmsg: {}", msg);
            throw new RuntimeException(e);
        }
        return resp;
    }

    /**
     * JSON体形式的post请求
     * @param url
     * @param headers
     * @param reqData
     * @return
     */
    public static String postJson(String url, Map<String, Object> headers, JSONObject reqData){
        if (StringUtils.isBlank(url)){
            log.info("=============== post请求url为空!! ===============");
            return "request url cannot be empty!!";
        }
        String resp = "";
        ClassicHttpRequest post = new HttpPost(url);
        try {
            if (CollectionUtil.isNotEmpty(headers)){
                for(String key : headers.keySet()){
                    post.addHeader(key, headers.get(key));
                }
            }
            if (!JSONUtil.isNull(reqData)){
                post.setEntity(new StringEntity(reqData.toString(), ContentType.APPLICATION_JSON));
            }
            resp = client.execute(post, respHandler);
        } catch (IOException e) {
            String msg = StringUtils.isBlank(resp) ? e.getMessage() : resp;
            log.error("================>请求异常!!\tmethod: GET,\turl: " + url + ",\tmsg: {}", msg);
            throw new RuntimeException(e);
        }
        return resp;
    }
}
