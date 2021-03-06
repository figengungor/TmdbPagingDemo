package com.figengungor.tmdbpagingdemo.utils;

import android.content.Context;

import com.figengungor.tmdbpagingdemo.R;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by figengungor on 2/20/2018.
 */

public class ErrorUtils {

    public static String displayFriendlyErrorMessage(Context context, Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof SocketTimeoutException) {
            return context.getString(R.string.time_out);
        } else if (throwable instanceof IOException) {
            return context.getString(R.string.no_internet);
        } else if (throwable.getMessage() != null) {
            String message = throwable.getMessage();
            /*if (message.equals(EMPTY)) {
                return context.getString(R.string.no_data_found);
            } else {
                return message;
            }*/
            return message;
        } else {
            return context.getString(R.string.unexpected_error);
        }
    }
}
