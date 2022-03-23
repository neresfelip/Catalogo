package br.com.neresfelip.catalogo.network

import br.com.neresfelip.catalogo.BuildConfig
import retrofit2.Retrofit

object ApiImgService {

    private fun initRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL_IMG)
            .build()
    }

    val service: TMDBImgService = initRetrofit().create(TMDBImgService::class.java)

}