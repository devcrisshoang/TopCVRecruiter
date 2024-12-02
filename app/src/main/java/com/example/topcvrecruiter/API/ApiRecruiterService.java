package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.Model.Recruiter;
import java.util.concurrent.TimeUnit;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiRecruiterService {

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);


    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    ApiRecruiterService ApiRecruiterService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiRecruiterService.class);

    @GET("api/Recruiter/{id}")
    Observable <Recruiter> getRecruiterByUserId(@Path("id") int id);

    @POST("api/Recruiter")
    Observable<Recruiter> createRecruiter(@Body Recruiter recruiter);

    @PUT("api/Recruiter/{id}")
    Completable updateRecruiterById(@Path("id") int id, @Body Recruiter recruiter);

    @GET("api/Recruiter/{id}")
    Observable<Recruiter> getRecruiterById(@Path("id") int id);
}

