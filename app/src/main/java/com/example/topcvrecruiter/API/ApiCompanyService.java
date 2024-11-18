package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.model.Company;
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

public interface ApiCompanyService {
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
    ApiCompanyService apiCompanyService = retrofit.create(ApiCompanyService.class);

    @GET("api/Company")
    Observable<List<Company>> getAllCompany();

    @GET("api/Company/recruiter/{id}")
    Observable<Company> getCompanyByRecruiterId(@Path("id") int recruiterId);

    @PUT("api/Company/{id}")
    Observable<Response<Void>> updateCompanyById(@Path("id") int companyId, @Body Company company);




}
