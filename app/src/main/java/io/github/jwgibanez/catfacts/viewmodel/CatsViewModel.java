package io.github.jwgibanez.catfacts.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.github.jwgibanez.catfacts.database.AppDatabase;
import io.github.jwgibanez.catfacts.database.model.CatFact;
import io.github.jwgibanez.catfacts.service.FactService;
import io.github.jwgibanez.catfacts.service.model.Fact;
import io.github.jwgibanez.catfacts.service.model.Photo;
import io.github.jwgibanez.catfacts.service.PhotoService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@HiltViewModel
public class CatsViewModel extends ViewModel {

    private static final int NUMBER_OF_THREADS = 1;
    public static final ExecutorService networkRequestExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private final AppDatabase db;
    private final PhotoService photoService;
    private final FactService factService;

    public LiveData<List<CatFact>> facts;
    public MutableLiveData<String> error = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    @Inject
    public CatsViewModel(AppDatabase db, PhotoService photoService, FactService factService) {
        this.db = db;
        this.photoService = photoService;
        this.factService = factService;
        facts = db.catFactDao().getAll();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        error.setValue(null);
        loading.setValue(false);
    }

    public void add(Activity activity) {
        if (!isNetworkConnected(activity)) {
            error.setValue("Network not connected.");
            return;
        }
        networkRequestExecutor.execute(() ->
            fetchNewCatFact(photoService, factService)
                .safeSubscribe(new Observer<Pair<Response<Photo>, Response<Fact>>>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        loading.postValue(true);
                    }

                    @Override
                    public void onNext(@NotNull Pair<Response<Photo>, Response<Fact>> pair) {
                        if (pair.first.isSuccessful() && pair.second.isSuccessful()) {
                            Photo photo = pair.first.body();
                            Fact fact = pair.second.body();
                            if (photo != null && fact != null) {
                                AppDatabase.databaseWriteExecutor.execute(() -> {
                                    CatFact catFact = new CatFact();
                                    catFact.id = photo.id;
                                    catFact.url = photo.url;
                                    catFact.fact = fact.fact;
                                    db.catFactDao().insertAll(catFact);
                                });
                                return;
                            }
                        }
                        error.postValue("Request error.");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        error.postValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        loading.postValue(false);
                    }
                })
        );
    }

    public void delete(CatFact fact) {
        AppDatabase.databaseWriteExecutor.execute(() ->
            db.catFactDao().delete(fact));
    }

    public static Observable<Pair<Response<Photo>, Response<Fact>>> fetchNewCatFact(
            PhotoService photoService, FactService factService) {
        return photoService.getPhoto().subscribeOn(Schedulers.io())
                .zipWith(factService.getFact().subscribeOn(Schedulers.io()), Pair::new)
                .subscribeOn(Schedulers.io());
    }

    private static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
