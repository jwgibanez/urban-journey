package io.github.jwgibanez.catfacts.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ViewModelComponent;
import io.github.jwgibanez.catfacts.service.FactService;
import io.github.jwgibanez.catfacts.service.PhotoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn({ActivityComponent.class, ViewModelComponent.class})
public class ApiModule {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    private @interface PhotoRetrofit {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    private @interface FactRetrofit {}

    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder(HttpLoggingInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);
        return builder;
    }

    @PhotoRetrofit
    @Provides
    Retrofit providePhotoRetrofit(OkHttpClient.Builder builder) {
        return new Retrofit.Builder()
                .baseUrl(PhotoService.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    @FactRetrofit
    @Provides
    Retrofit provideFactRetrofit(OkHttpClient.Builder builder) {
        return new Retrofit.Builder()
                .baseUrl(FactService.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    @Provides
    public static PhotoService providePhotoService(@PhotoRetrofit Retrofit retrofit) {
        return retrofit.create(PhotoService.class);
    }

    @Provides
    public static FactService provideFactService(@FactRetrofit Retrofit retrofit) {
        return retrofit.create(FactService.class);
    }
}
