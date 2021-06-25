package io.github.jwgibanez.catfacts.service;

import io.github.jwgibanez.catfacts.service.model.Fact;
import io.reactivex.Observable;
import retrofit2.Response;

public class FakeFactService implements FactService {

    @Override
    public Observable<Response<Fact>> getFact() {
        Fact fact = new Fact();
        fact.fact = "This is a fact.";
        return Observable.just(Response.success(fact));
    }
}
