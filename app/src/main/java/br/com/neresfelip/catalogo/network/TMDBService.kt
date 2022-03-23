package br.com.neresfelip.catalogo.network

import br.com.neresfelip.catalogo.BuildConfig
import br.com.neresfelip.catalogo.model.Filme
import br.com.neresfelip.catalogo.network.response.ListBannersResponse
import br.com.neresfelip.catalogo.network.response.ListFilmesResponse
import br.com.neresfelip.catalogo.network.response.ListGenerosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface TMDBService {

    @GET("/3/movie/popular?limit=1")
    fun listPopular(
        @Query("page") page: Int = 1,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListFilmesResponse>

    @GET("/3/movie/top_rated")
    fun listMelhoresAvaliados(
        @Query("page") page: Int = 1,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListFilmesResponse>

    @GET("/3/movie/upcoming")
    fun listProxLancamentos(
        @Query("page") page: Int = 1,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("region") region: String = Locale.getDefault().country,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListFilmesResponse>

    @GET("/3/discover/movie")
    fun listPorGenero(
        @Query("with_genres") id_genero: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListFilmesResponse>

    @GET("/3/movie/{movie_id}/similar")
    fun listSimilar(
        @Path("movie_id") movie_id: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListFilmesResponse>

    @GET("/3/genre/movie/list")
    fun getListGeneros(
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListGenerosResponse>

    @GET("/3/movie/{movie_id}")
    fun getFilme(
        @Path("movie_id") movie_id: Int,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<Filme>

    @GET("/3/movie/{movie_id}/images")
    fun getFilmeBanners(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
    ) : Call<ListBannersResponse>

}