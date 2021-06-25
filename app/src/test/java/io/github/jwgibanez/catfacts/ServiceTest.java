package io.github.jwgibanez.catfacts;

import androidx.core.util.Pair;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import io.github.jwgibanez.catfacts.service.FakeFactService;
import io.github.jwgibanez.catfacts.service.FakePhotoService;
import io.github.jwgibanez.catfacts.service.PhotoService;
import io.github.jwgibanez.catfacts.service.model.Fact;
import io.github.jwgibanez.catfacts.service.model.Photo;
import io.github.jwgibanez.catfacts.viewmodel.CatsViewModel;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import retrofit2.Response;

public class ServiceTest {

    @Test
    public void happyPath() {
        PhotoService photoService = new FakePhotoService();
        FakeFactService factService = new FakeFactService();
        TestObserver<Pair<Response<Photo>, Response<Fact>>> observer =
                CatsViewModel.fetchNewCatFact(photoService, factService).test();
        observer.awaitTerminalEvent();
        observer.assertNoErrors()
                .assertValue(check(t -> {
                    MatcherAssert.assertThat("Photo response is null", t.first != null);
                    MatcherAssert.assertThat("Photo response body is null", t.first.body() != null);
                    MatcherAssert.assertThat("Photo id is null", t.first.body().id != null);
                    MatcherAssert.assertThat("Photo url null", t.first.body().url != null);

                    MatcherAssert.assertThat("Fact response is null", t.second != null);
                    MatcherAssert.assertThat("Fact response body is null", t.second.body() != null);
                    MatcherAssert.assertThat("Fact fact is null", t.second.body().fact != null);
                }));
    }

    static <T> Predicate<T> check(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return true;
        };
    }
}
