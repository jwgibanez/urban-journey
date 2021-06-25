package io.github.jwgibanez.catfacts.service;

import io.github.jwgibanez.catfacts.service.model.Fact;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

public interface FactService {
    String HOST = "https://catfact.ninja";
    @GET("fact")
    Observable<Response<Fact>> getFact();
}
