package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.model.Applicant;
import com.example.topcvrecruiter.model.CV;
import com.example.topcvrecruiter.model.Job;

import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiDashboardService {
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
    ApiDashboardService apiDashboardService = new Retrofit.Builder()
            .baseUrl("https://10.0.2.2:7200/")  // Thay địa chỉ bằng IP của máy bạn hoặc server thật
            .client(okHttpClient)  // Áp dụng OkHttpClient bỏ qua SSL
            .addConverterFactory(GsonConverterFactory.create())  // Chuyển đổi JSON sang đối tượng Java
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())  // Sử dụng RxJava3
            .build()
            .create(ApiDashboardService.class);
    @GET("api/Recruiter/{id}/ApplicantCount")
    Observable<Integer> getApplicantCount(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/JobCount")
    Observable<Integer> getJobCount(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/ResumeCount")
    Observable<String> getResumeCount(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/ApplicationRatio")
    Observable<String> getApplicationRatio(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/SuggestedApplicants")
    Observable<List<Applicant>> getListSuggestedApplicants(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/ApplicantsForJob")
    Observable<List<Applicant>> getListApplicants(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/ResumesForJobs")
    Observable<List<CV>> getListResume(@Path("id") int recruiterId);

    @GET("api/Recruiter/{id}/JobsList")
    Observable<List<Job>> getListJobs(@Path("id") int recruiterId);

    @GET("api/Recruiter/{idRecruiter}/applicant/{idApplicant}/cv")
    Observable<CV> getCvForApplicant(@Path("idApplicant") int applicantId, @Path("idRecruiter") int recruiterId);

}
