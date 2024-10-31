package com.example.topcvrecruiter.API;

import com.example.topcvrecruiter.model.Article;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiPostingService {


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



    ApiPostingService apiService = retrofit.create(ApiPostingService.class);



    @GET("api/Article")
    Call<List<Article>> getArticles();

    @GET("api/Article/{id}")
    Single<Article> getArticleById(@Path("id") int articleId);

    @POST("api/Article")
    Call<Article> postArticle(@Body Article article);

    @PUT("api/Article/{id}")
    Call<Article> updateArticle(
            @Path("id") int articleId,  // @Path annotation cho ID
            @Body Article article       // @Body annotation cho article
    );
    @PUT("api/Article/Image/{id}")
    Call<Void> updateArticleImage(@Path("id") int id, @Body String imageUri);

    @DELETE("api/Article/{id}")
    Call<Void> deleteArticle(@Path("id") int id);

}
