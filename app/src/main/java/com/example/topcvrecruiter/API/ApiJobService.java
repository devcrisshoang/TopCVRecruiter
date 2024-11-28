package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.Model.JobDetails;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;

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

    @GET("api/Job/{jobId}")
    Call<Job> getJobById(@Path("jobId") int jobId);

    @POST("api/Job")
    Call<Job> postJob (@Body Job job);

    @POST("api/JobDetails")
    Call<JobDetails> postJobDetails (@Body JobDetails jobDetails);

    @GET("api/JobDetails/ByJob/{jobId}")
    Call<List<JobDetails>> getJobDetailsByJobId(@Path("jobId") int jobId);

    @DELETE("api/Job/{id}")
    Call<Void> deleteJobById(@Path("id") int id);

    @PUT("api/Job/{id}")
    Call<Job> updateJob(@Path("id") int jobId, @Body Job job);

    @PUT("api/JobDetails/{id}")
    Call<JobDetails> putJobDetails(@Path("id") int jobDetailsId, @Body JobDetails jobDetails);

    @GET("api/Job/ByRecruiter/{recruiterId}")
    Call<List<Job>> getJobsByRecruiter(@Path("recruiterId") int recruiterId);
}
