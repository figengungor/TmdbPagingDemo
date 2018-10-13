package com.figengungor.tmdbpagingdemo.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.figengungor.tmdbpagingdemo.data.DataManager;
import com.figengungor.tmdbpagingdemo.data.model.Movie;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<PageKeyedMovieDataSource> pageKeyedMovieDataSourceMutableLiveData;
    private static MovieDataSourceFactory INSTANCE;
    private DataManager dataManager;

    private MovieDataSourceFactory(DataManager dataManager) {
        this.dataManager = dataManager;
        pageKeyedMovieDataSourceMutableLiveData = new MutableLiveData<>();
    }

    public static MovieDataSourceFactory getInstance(DataManager dataManager){
        if (INSTANCE == null){
            INSTANCE = new MovieDataSourceFactory(dataManager);
        }

        return INSTANCE;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        PageKeyedMovieDataSource pagedKeyedMovieDataSource = new PageKeyedMovieDataSource(dataManager);
        pageKeyedMovieDataSourceMutableLiveData.postValue(pagedKeyedMovieDataSource);

        return pagedKeyedMovieDataSource;
    }

    public MutableLiveData<PageKeyedMovieDataSource> getPageKeyedMovieDataSourceMutableLiveData() {
        return pageKeyedMovieDataSourceMutableLiveData;
    }
}