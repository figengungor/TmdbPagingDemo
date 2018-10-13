package com.figengungor.tmdbpagingdemo.data;

import com.figengungor.tmdbpagingdemo.data.model.MovieListResponse;
import com.figengungor.tmdbpagingdemo.data.remote.TmdbCallback;
import com.figengungor.tmdbpagingdemo.data.remote.TmdbService;
import com.figengungor.tmdbpagingdemo.data.remote.TmdbServiceFactory;

import retrofit2.Call;

public class DataManager {

    private static DataManager instance;
    private TmdbService tmdbService;

    private DataManager() {
        this.tmdbService = TmdbServiceFactory.createService();
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public Call getMovieList(String movieListType, int page,  TmdbCallback<MovieListResponse> listener) {
        Call<MovieListResponse> call = tmdbService.getMovies(movieListType, page);
        call.enqueue(listener);
        return call;
    }

}
