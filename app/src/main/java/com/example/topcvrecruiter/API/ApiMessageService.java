package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.Model.Message;
import com.example.topcvrecruiter.Model.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiMessageService {

    // Logging interceptor để theo dõi request và response
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Sử dụng OkHttpClient an toàn bỏ qua SSL
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    // Sử dụng Retrofit để tạo API service
    ApiMessageService apiMessageService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Thay địa chỉ bằng IP của máy bạn hoặc server thật
            .client(okHttpClient)  // Áp dụng OkHttpClient bỏ qua SSL
            .addConverterFactory(GsonConverterFactory.create())  // Chuyển đổi JSON sang đối tượng Java
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())  // Sử dụng RxJava3
            .build()
            .create(ApiMessageService.class);

    // API lấy danh sách tin nhắn
    @GET("api/Message")
    Observable<List<Message>> getAllMessages();

    @GET("api/Message/GetChatPartners/{id}")
    Observable<List<User>> getAllChatPartnersByUserId(@Path("id") int id);
    //
    @GET("api/Message/GetMessagesBetweenUsers/{idUser1}/{idUser2}")
    Observable<List<Message>> getAllMessageByTwoUserId(@Path("idUser1") int idUser1, @Path("idUser2") int idUser2);
    // POST
    @POST("api/Message")
    Observable<Message> postMessage(@Body Message message);
}
