package com.figengungor.tmdbpagingdemo.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.figengungor.tmdbpagingdemo.data.DataManager;
import com.figengungor.tmdbpagingdemo.data.model.Movie;
import com.figengungor.tmdbpagingdemo.data.model.MovieListResponse;
import com.figengungor.tmdbpagingdemo.data.remote.NetworkState;
import com.figengungor.tmdbpagingdemo.data.remote.TmdbCallback;

/**
 * Created by figengungor on 6/23/2018.
 */
public class PageKeyedMovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private DataManager dataManager;
    private MutableLiveData<NetworkState> networkState;
    private MutableLiveData<NetworkState> initialLoading;

    PageKeyedMovieDataSource(DataManager dataManager) {
        this.dataManager = dataManager;
        networkState = new MutableLiveData<>();
        initialLoading = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {

        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        dataManager.getMovieList("now_playing", 1, new TmdbCallback<MovieListResponse>() {
            @Override
            public void onSuccess(MovieListResponse response) {

                if (response.getMovies().size() > 0) {
                    callback.onResult(response.getMovies(), null, response.getPage() + 1);

                    networkState.postValue(NetworkState.LOADED);
                    initialLoading.postValue(NetworkState.LOADED);
                } else {
                    networkState.postValue(new NetworkState(NetworkState.Status.NO_ITEM));
                    initialLoading.postValue(new NetworkState(NetworkState.Status.NO_ITEM));
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                networkState.postValue(NetworkState.error(throwable));
                initialLoading.postValue(NetworkState.error(throwable));
            }
        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkState.LOADING);
        dataManager.getMovieList("now_playing", params.key, new TmdbCallback<MovieListResponse>() {
            @Override
            public void onSuccess(MovieListResponse response) {
                if (response.getMovies().size() > 0) {
                    callback.onResult(response.getMovies(), response.getPage() + 1);
                    networkState.postValue(NetworkState.LOADING);
                } else {
                    networkState.postValue(NetworkState.LOADED);
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                networkState.postValue(NetworkState.error(throwable));
            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        // ignored, since we only ever append to our initial load
    }
}
