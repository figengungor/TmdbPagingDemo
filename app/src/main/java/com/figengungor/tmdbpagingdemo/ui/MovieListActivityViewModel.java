package com.figengungor.tmdbpagingdemo.ui;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.figengungor.tmdbpagingdemo.data.model.Movie;
import com.figengungor.tmdbpagingdemo.data.remote.NetworkState;
import com.figengungor.tmdbpagingdemo.utils.AppsExecutor;

public class MovieListActivityViewModel extends ViewModel {

    private LiveData<NetworkState> networkState;
    private LiveData<NetworkState> initialLoading;
    private LiveData<PagedList<Movie>> movieList;
    private MovieDataSourceFactory movieDataSourceFactory;

    MovieListActivityViewModel(MovieDataSourceFactory movieDataSourceFactory) {
        this.movieDataSourceFactory = movieDataSourceFactory;

        networkState = Transformations.switchMap(movieDataSourceFactory.getPageKeyedMovieDataSourceMutableLiveData(), new Function<PageKeyedMovieDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(PageKeyedMovieDataSource input) {
                return input.getNetworkState();
            }
        });

        initialLoading = Transformations.switchMap(movieDataSourceFactory.getPageKeyedMovieDataSourceMutableLiveData(),
                new Function<PageKeyedMovieDataSource, LiveData<NetworkState>>() {
                    @Override
                    public LiveData<NetworkState> apply(PageKeyedMovieDataSource input) {
                        return input.getInitialLoading();
                    }
                });

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(5)
                .build();

        movieList = new LivePagedListBuilder<>(movieDataSourceFactory, config)
                .setFetchExecutor(AppsExecutor.networkIO())
                .build();
    }

    public LiveData<PagedList<Movie>> getMovieResult() {
        return movieList;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    public LiveData<PagedList<Movie>> refreshMovieResults() {
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setPrefetchDistance(5)
                .build();

        movieList = new LivePagedListBuilder<>(movieDataSourceFactory, config)
                .setFetchExecutor(AppsExecutor.networkIO())
                .build();

        return movieList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}