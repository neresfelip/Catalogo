package br.com.neresfelip.catalogo.network

import br.com.neresfelip.catalogo.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private fun initRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service : TMDBService = initRetrofit().create(TMDBService::class.java)

}