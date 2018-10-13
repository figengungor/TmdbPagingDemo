package com.figengungor.tmdbpagingdemo.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.figengungor.tmdbpagingdemo.R;
import com.figengungor.tmdbpagingdemo.data.DataManager;
import com.figengungor.tmdbpagingdemo.data.model.Movie;
import com.figengungor.tmdbpagingdemo.data.remote.NetworkState;
import com.figengungor.tmdbpagingdemo.utils.ErrorUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity {

    //Helpful Resources
    //https://riyaz-ali.github.io/android-support-paging.html
    //https://github.com/nuhkoca/Trippo-The_Travel_Guide/tree/master/app/src/main/java/com/nuhkoca/trippo/ui/searchable

    MovieListActivityViewModel viewModel;
    MovieAdapter movieAdapter;
    @BindView(R.id.movieRv)
    RecyclerView movieRv;
    @BindView(R.id.loadingPw)
    ProgressWheel loadingPw;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.retryBtn)
    Button retryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieAdapter = new MovieAdapter();
        movieRv.setLayoutManager(layoutManager);
        movieRv.setAdapter(movieAdapter);

        viewModel = ViewModelProviders.of(this,
                new MovieListActivityViewModelFactory(
                        MovieDataSourceFactory.getInstance(
                                DataManager.getInstance())))
                .get(MovieListActivityViewModel.class);

        viewModel.getMovieResult().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(@Nullable PagedList<Movie> countryResults) {
                movieAdapter.submitList(countryResults);
            }
        });


        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                movieAdapter.setNetworkState(networkState);
            }
        });

        viewModel.getInitialLoading().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (networkState != null) {
                    if (networkState.getStatus() == NetworkState.Status.SUCCESS) {
                        loadingPw.setVisibility(View.GONE);
                        messageTv.setVisibility(View.GONE);
                        retryBtn.setVisibility(View.GONE);

                    } else if (networkState.getStatus() == NetworkState.Status.FAILED) {
                        loadingPw.setVisibility(View.GONE);
                        messageTv.setVisibility(View.VISIBLE);
                        messageTv.setText(ErrorUtils.displayFriendlyErrorMessage(MovieListActivity.this, networkState.getError()));
                        retryBtn.setVisibility(View.VISIBLE);

                    } else if (networkState.getStatus() == NetworkState.Status.NO_ITEM) {
                        loadingPw.setVisibility(View.GONE);
                        messageTv.setVisibility(View.VISIBLE);
                        messageTv.setText("No result");
                        retryBtn.setVisibility(View.GONE);

                    } else {
                        loadingPw.setVisibility(View.VISIBLE);
                        messageTv.setVisibility(View.GONE);
                        retryBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
}
