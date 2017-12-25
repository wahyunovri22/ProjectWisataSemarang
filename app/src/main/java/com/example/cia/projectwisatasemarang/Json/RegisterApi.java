package com.example.cia.projectwisatasemarang.Json;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cia on 04/10/2017.
 */

public interface RegisterApi {

    @GET("getwisata.php")
    Call<Value> view();
}
