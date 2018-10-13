package com.figengungor.tmdbpagingdemo.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieListActivityViewModelFactory implements ViewModelProvider.Factory {

    private MovieDataSourceFactory movieDataSourceFactory;

    MovieListActivityViewModelFactory(MovieDataSourceFactory movieDataSourceFactory) {
        this.movieDataSourceFactory = movieDataSourceFactory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieListActivityViewModel(movieDataSourceFactory);
    }
}
