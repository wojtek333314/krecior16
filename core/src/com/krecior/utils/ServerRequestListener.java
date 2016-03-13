package com.krecior.utils;

public interface ServerRequestListener {
    void onSuccess(String json);

    void onError(int code, String description);

    void onConnectionError();
}