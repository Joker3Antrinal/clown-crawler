package com.joker.just4fun.config;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

/**
 *
 * Http响应处理类
 *
 * @author Joker.Y
 * @since 2024/4/10
 * @version 1.0
 */
public class HttpRespHandler implements HttpClientResponseHandler<String> {
    @Override
    public String handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
        int status = response.getCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                return EntityUtils.toString(entity);
            }
        }
        throw new IOException("Unexpected response status: " + status);
    }
}
