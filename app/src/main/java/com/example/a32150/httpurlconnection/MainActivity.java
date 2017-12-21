package com.example.a32150.httpurlconnection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL url = null;
        try {
            url = new URL("http://blog.csdn.net/yanzhenjie1003");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        // 设置SSLSocketFoactory，这里有两种：1.需要安全证书 2.不需要安全证书；看官且往下看
        if (urlConnection instanceof HttpsURLConnection) { // 是Https请求
            SSLContext sslContext = SSLContextUtil.getSSLContext();
            if (sslContext != null) {
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
            }
        }

// 设置属性
        urlConnection.setConnectTimeout(8 * 1000);
        urlConnection.setReadTimeout(8 * 1000);

        int responseCode = 0;
        try {
            responseCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseCode == 200) { // 请求成功
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 读取结果，发送到主线程

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        urlConnection.disconnect();


    }
}
