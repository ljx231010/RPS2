package com.lu.controller;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class T {

    public static void main(String[] args) {
        String result = get("https://biolab.gxu.edu.cn/AFP/config.jsp");
    }

    public static String get(String url) {
        String resultContent = null;
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(Timeout.ofSeconds(200000))
                .setConnectionRequestTimeout(Timeout.ofSeconds(200000))
                .setResponseTimeout(Timeout.ofSeconds(200000))
                .build();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                
                System.out.println(response.getVersion()); 
                System.out.println(response.getCode()); 
                System.out.println(response.getReasonPhrase()); 
                HttpEntity entity = response.getEntity();
                
                resultContent = EntityUtils.toString(entity);
                System.out.println(resultContent);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return resultContent;
    }

    public static String post(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            
            URLConnection conn = realUrl.openConnection();
            
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            out = new PrintWriter(conn.getOutputStream());
            
            out.print(param);
            
            out.flush();
            
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
