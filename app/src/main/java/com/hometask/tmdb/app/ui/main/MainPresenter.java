package com.hometask.tmdb.app.ui.main;


import android.util.Log;

import com.hometask.tmdb.app.models.MovieResponse;
import com.hometask.tmdb.app.network.NetworkClient;
import com.hometask.tmdb.app.network.NetworkInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements MainPresenterInterface {

    MainViewInterface mvi;
    private String TAG = "MainPresenter";

    public MainPresenter(MainViewInterface mvi) {
        this.mvi = mvi;
    }

    @Override
    public void getTopMovies() {
        getObservableTop().subscribeWith(getObserver());
    }

    @Override
    public void getPopularMovies() {
        getObservablePopular().subscribeWith(getObserver());
    }

    public Observable<MovieResponse> getObservableTop(){
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                            .getTopMovies("0d50d5daa10bb920d2385c43fef01459")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<MovieResponse> getObservablePopular(){
        return NetworkClient.getRetrofit().create(NetworkInterface.class)
                .getPopularMovies("0d50d5daa10bb920d2385c43fef01459")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<MovieResponse> getObserver(){
        return new DisposableObserver<MovieResponse>() {

            @Override
            public void onNext(@NonNull MovieResponse movieResponse) {
                Log.d(TAG,"OnNext"+movieResponse.getTotalResults());
                mvi.displayMovies(movieResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                mvi.displayError("Error load Movie Data. Try to turn on the internet");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
                mvi.hideProgressBar();
            }
        };
    }
}
