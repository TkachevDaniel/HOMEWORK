package com.hometask.tmdb.app.network;


import com.hometask.tmdb.app.models.MovieResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Запросы к серверу
 */

public interface NetworkInterface {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<MovieResponse> getTopMovies(@Query("api_key") String api_key);
}
