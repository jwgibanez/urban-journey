package io.github.jwgibanez.catfacts.service;

import io.github.jwgibanez.catfacts.service.model.Photo;
import io.reactivex.Observable;
import retrofit2.Response;

public class FakePhotoService implements PhotoService {

    @Override
    public Observable<Response<Photo>> getPhoto() {
        Photo photo = new Photo();
        photo.id = "43445645654";
        photo.url = "https://www.url.com";
        return Observable.just(Response.success(photo));
    }
}
