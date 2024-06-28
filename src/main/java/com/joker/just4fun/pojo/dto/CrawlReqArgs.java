package com.joker.just4fun.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * 抓取所需参数
 *
 * @author Joker.Y
 * @since 2024/4/7
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CrawlReqArgs implements Serializable {

    @Serial
    private static final long serialVersionUID = 1930406276578146159L;

    /**
     * 目标url
     */
    @NonNull
    private String targetUrl;

    /**
     * 请求方式
     */
    @NonNull
    private String method;

    /**
     * 用户代理头
     * eg: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3
     */
    private String userAgent = "Mozilla AppleWebKit Chrome Safari Edg";

    /**
     * 请求头信息
     */
    private Map<String, String> headers;

    /**
     * cookie
     */
    private Map<String, String> cookies;

    /**
     * 请求参数
     */
    private Map<String, String> reqData;

}
