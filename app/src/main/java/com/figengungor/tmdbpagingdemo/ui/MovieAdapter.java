package com.figengungor.tmdbpagingdemo.ui;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.figengungor.tmdbpagingdemo.R;
import com.figengungor.tmdbpagingdemo.data.model.Movie;
import com.figengungor.tmdbpagingdemo.data.remote.NetworkState;
import com.figengungor.tmdbpagingdemo.utils.ImageUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private List<Movie> movies;

    protected MovieAdapter() {
        super(Movie.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (viewType == R.layout.item_movie) {
            View view= layoutInflater.inflate(
                    R.layout.item_movie, parent, false);

            return new MovieViewHolder(view);
        } else {
            View view = layoutInflater.inflate(
                    R.layout.network_state_item, parent, false);

            return new NetworkViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_movie:
                if (getItem(position) != null) {
                    ((MovieViewHolder) holder).bindTo(movies.get(position));
                }

                break;

            case R.layout.network_state_item:
                ((NetworkViewHolder) holder).bindTo(networkState);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        }
        return super.getItemCount();
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.item_movie;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<Movie> currentList) {
        super.onCurrentListChanged(currentList);

        movies = currentList;

        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterIv;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterIv = itemView.findViewById(R.id.posterIv);
        }

        public void bindTo(Movie movie) {
            ImageUtils.loadImageUrl(movie.getPosterPath(), posterIv, ImageUtils.ImageType.POSTER);
            posterIv.setContentDescription(movie.getTitle());
        }
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.loadingPw)
        ProgressWheel loadingPw;
        @BindView(R.id.messageTv)
        TextView messageTv;
        @BindView(R.id.retryBtn)
        Button retryBtn;
        @BindView(R.id.container)
        ConstraintLayout container;

        NetworkViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(NetworkState networkState) {

            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                container.setVisibility(View.VISIBLE);
                loadingPw.setVisibility(View.VISIBLE);
                retryBtn.setVisibility(View.GONE);
                messageTv.setVisibility(View.GONE);
            } else if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                container.setVisibility(View.VISIBLE);
                loadingPw.setVisibility(View.GONE);
                retryBtn.setVisibility(View.VISIBLE);
                messageTv.setVisibility(View.VISIBLE);
            } else {
                container.setVisibility(View.GONE);
                loadingPw.setVisibility(View.GONE);
                retryBtn.setVisibility(View.GONE);
                messageTv.setVisibility(View.GONE);
            }
        }
    }
}