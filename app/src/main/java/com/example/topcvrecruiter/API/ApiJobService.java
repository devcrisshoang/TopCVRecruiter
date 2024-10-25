package com.example.topcvrecruiter.API;


import com.example.topcvrecruiter.model.Job;
import com.example.topcvrecruiter.model.JobDetails;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiJobService {


    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();


    Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://10.0.2.2:7200/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();



    ApiJobService apiService = retrofit.create(ApiJobService.class);


    @GET("api/Job")
    Call<List<Job>> getJobs();

    @POST("api/Job")
    Call<Job> postJob (@Body Job job);


    @POST("api/JobDetails")
    Call<JobDetails> postJobDetails (@Body JobDetails jobDetails);
}
