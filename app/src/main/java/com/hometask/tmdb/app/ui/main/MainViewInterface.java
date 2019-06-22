package com.hometask.tmdb.app.ui.main;

import com.hometask.tmdb.app.models.MovieResponse;

public interface MainViewInterface {

    void showToast(String s);
    void showProgressBar();
    void hideProgressBar();
    void displayMovies(MovieResponse movieResponse);
    void displayError(String s);
}
