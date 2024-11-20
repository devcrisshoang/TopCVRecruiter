package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.Model.CV;
import com.example.topcvrecruiter.Model.Job;
import com.example.topcvrecruiter.model.ApplicantJob;

import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
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
    //--------------------JOB-------------------------//
    @GET("api/Job/ByRecruiter/{id}")
    Observable<List<Job>> getListJobs(@Path("id") int recruiterId);
    //--------------------APPLICANT of JOB-------------------------//
    @GET("api/Recruiter/recruiter/{idRecruiter}/job/{idJob}/applicants")
    Observable<List<ApplicantJob>> getListApplicantsForJob(@Path("idRecruiter") int recruiterId, @Path("idJob") int jobId);
    //--------------------RATE ACCEPT-------------------------//
    @GET("api/Recruiter/{id}/AcceptedApplicants")
    Observable<List<ApplicantJob>> getAcceptedApplicants(@Path("id") int recruiterId);
    //--------------------RATE REJECT-------------------------//
    @GET("api/Recruiter/{id}/RejectedApplicants")
    Observable<List<ApplicantJob>> getRejectedApplicants(@Path("id") int recruiterId);
    //--------------------Suggest-------------------------//
    @GET("api/Recruiter/{id}/SuggestedApplicants")
    Observable<List<ApplicantJob>> getListSuggestedApplicants(@Path("id") int recruiterId);
    //--------------------RESUME-------------------------//
    @GET("api/Recruiter/{idRecruiter}/applicant/{idApplicant}/cv")
    Observable<CV> getCvForApplicant(@Path("idApplicant") int applicantId, @Path("idRecruiter") int recruiterId);
    //----------------------PUT RATE -----------------------------//
    @PUT("api/Recruiter/{recruiterId}/applicants/{applicantId}/acceptance")
    Single<Response<Void>> updateAcceptanceStatus(
            @Path("recruiterId") int recruiterId,
            @Path("applicantId") int applicantId,
            @Body Boolean isAccepted
    );

}
