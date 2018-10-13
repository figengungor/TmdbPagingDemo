package com.figengungor.tmdbpagingdemo.data.remote;

public class NetworkState {
    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        NO_ITEM
    }

    private final Status status;
    private final Throwable error;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, Throwable error) {
        this.status = status;
        this.error = error;
    }

    public NetworkState(Status status) {
        this.status = status;
        this.error = null;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS);
        LOADING = new NetworkState(Status.RUNNING);
    }

    public Status getStatus() {
        return status;
    }

    public Throwable getError() {
        return error;
    }

    public static NetworkState error(Throwable error) {
        return new NetworkState(Status.FAILED, error);
    }
}
