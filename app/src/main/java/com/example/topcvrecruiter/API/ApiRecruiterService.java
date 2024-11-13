package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.model.Recruiter;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiRecruiterService {

    // Tạo interceptor cho logging
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Tạo OkHttpClient với interceptor và bỏ qua SSL
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .sslSocketFactory(getUnsafeSslSocketFactory(), new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0]; // Trả về mảng trống thay vì null
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certificates, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certificates, String authType) {
                }
            })
            .hostnameVerifier((hostname, session) -> true)  // Chấp nhận tất cả hostname
            .build();

    // Tạo Retrofit instance cho ApiRecruiterService
    Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)  // Sử dụng OkHttpClient đã bỏ qua SSL
            .baseUrl("https://10.0.2.2:7200/")  // Địa chỉ máy chủ của bạn
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

    // Khởi tạo API service từ Retrofit
    ApiRecruiterService apiRecruiterService = retrofit.create(ApiRecruiterService.class);

    // API endpoint lấy tất cả nhà tuyển dụng
    @GET("api/Recruiter")
    Observable<List<Recruiter>> getAllRecruiter();

    // API endpoint lấy nhà tuyển dụng theo ID
    @GET("api/Recruiter/{id}")
    Observable<Recruiter> getRecruiterById(@Path("id") int id);

    // Hàm để tạo SSL socket factory cho việc bỏ qua SSL
    static javax.net.ssl.SSLSocketFactory getUnsafeSslSocketFactory() {
        try {
            // Tạo TrustManager cho phép tất cả chứng chỉ
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0]; // Trả về mảng trống
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certificates, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certificates, String authType) {
                        }
                    }
            };

            // Tạo SSLContext để sử dụng TrustManager đã tạo
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Trả về SSLSocketFactory để sử dụng
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a custom SSL socket factory", e);
        }
    }
}
