package io.github.jwgibanez.catfacts.service;

import io.github.jwgibanez.catfacts.service.model.Photo;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface PhotoService {
    String HOST = "https://cataas.com";
    @GET("cat?json=true")
    Observable<Response<Photo>> getPhoto();
}
