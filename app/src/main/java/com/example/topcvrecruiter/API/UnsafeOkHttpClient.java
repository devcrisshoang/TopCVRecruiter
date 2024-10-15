package com.example.topcvrecruiter.API;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Tạo một TrustManager để bỏ qua các chứng chỉ không tin cậy
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Tạo SSL context với TrustManager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Tạo SSL socket factory với TrustManager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Xây dựng OkHttpClient để bỏ qua SSL
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true); // Bỏ qua xác thực hostname

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
