package com.jingdong;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;

public class SignHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        StringBuilder response = new StringBuilder();

        String postJson = null;
        try {
            postJson = IOUtils.toString(exchange.getRequestBody());
            JSONObject object = JSONObject.parseObject(postJson);
            String sign = JingDong.jingdong.callSign(
                    object.getString("functionId"),
                    object.getString("body"),
                    object.getString("uuid"),
                    object.getString("client"),
                    object.getString("clientVersion")
            );
            response.append(sign);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}