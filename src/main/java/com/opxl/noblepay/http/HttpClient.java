package com.opxl.noblepay.http;

import lombok.SneakyThrows;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

public interface HttpClient {
    Response post(Map<String, String> headerLIst, String jsonPayload, String url)throws IOException;
    Response postAndMaskData(Map<String, String> headerLIst, String jsonPayload, String url, String maskedDate) throws  IOException;

    Response get(Map<String, String> headerLIst, Map<String, Object> params,String url) throws  IOException;
    Response getNoParam(Map<String, String> headerLIst, String url) throws  IOException;
    <T> T toPojo(String json,Class<T> type);

    String toJson(Object obj);

    @SneakyThrows
    <T> T post(Map<String, String> headerList, String jsonPayload, String url, Class<T> type);
}
