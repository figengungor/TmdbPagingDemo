package com.figengungor.tmdbpagingdemo.data.remote;

import com.figengungor.tmdbpagingdemo.data.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by figengungor on 2/19/2018.
 */

public interface  TmdbService {

    @GET("movie/{type}")
    Call<MovieListResponse> getMovies(@Path("type") String type, @Query("page") int page);

}
