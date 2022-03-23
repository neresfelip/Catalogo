package br.com.neresfelip.catalogo.network

import br.com.neresfelip.catalogo.BuildConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBImgService {

    @GET("/t/p/w154{path}")
    fun getImage(
        @Path("path") path: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ResponseBody>

    @GET("/t/p/w300{path}")
    fun getImageW300(
        @Path("path") path: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ResponseBody>

    @GET("/t/p/w780{path}")
    fun getImageW780(
        @Path("path") path: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ResponseBody>

}