package br.com.neresfelip.catalogo.ui.filme_detalhe

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.neresfelip.catalogo.model.Filme
import br.com.neresfelip.catalogo.model.Banner
import br.com.neresfelip.catalogo.network.ApiService
import br.com.neresfelip.catalogo.network.response.ListBannersResponse
import br.com.neresfelip.catalogo.network.response.ListFilmesResponse
import br.com.neresfelip.catalogo.network.utils.DownloadImg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmeDetalheViewModel : ViewModel() {

    val filmeLiveData: MutableLiveData<Filme> = MutableLiveData()
    val posterLiveData: MutableLiveData<Any> = MutableLiveData()
    val bannersLiveData: MutableLiveData<MutableList<Bitmap>> = MutableLiveData()
    val listSimilaresLiveData: MutableLiveData<MutableList<Filme>> = MutableLiveData()

    var onDownloadError: ((throwable: Throwable) -> Unit)? = null

    private val quantMaxImagens = 6

    fun getFilme(movieId: Int) {

        ApiService.service.getFilme(movieId)
            .enqueue(object: Callback<Filme>{

                override fun onResponse(call: Call<Filme>, response: Response<Filme>) {

                    if(response.isSuccessful){
                        response.body()?.let { filme ->
                            filmeLiveData.value = filme
                            filme.poster_path?.let {path -> DownloadImg.getImageFromApi(path, filmeLiveData) { bitmap -> filme.poster = bitmap } }
                            filme.production_companies.forEach {
                                    estudio -> estudio.logo_path?.let{
                                DownloadImg.getImageFromApi(estudio.logo_path, posterLiveData) { bitmap -> estudio.logo = bitmap}
                            }}
                        }
                    }

                }

                override fun onFailure(call: Call<Filme>, t: Throwable) {
                    t.printStackTrace()
                }

            })

    }

    fun getFilmeBanners(movieId: Int) {

        ApiService.service.getFilmeBanners(movieId).enqueue(object: Callback<ListBannersResponse>{

            override fun onResponse(call: Call<ListBannersResponse>, response: Response<ListBannersResponse>) {

                if(!response.isSuccessful) {
                    onFailure(call, Throwable("Não obteve sucesso"))
                    return
                }

                if(response.body() == null) {
                    onFailure(call, Throwable("Não veio body"))
                    return
                }

                val imgs: List<Banner> = response.body()!!.backdrops

                if(imgs.isEmpty()) {
                    onFailure(call, Throwable("Lista de imagens vazia"))
                    return
                }

                for(indice in imgs.indices) {

                    if(indice == quantMaxImagens) break

                    DownloadImg.getImageW780FromApi(imgs[indice].file_path, bannersLiveData) {
                            bitmap ->
                        if(bannersLiveData.value == null)
                            bannersLiveData.value = mutableListOf()

                        bannersLiveData.value!!.add(bitmap)
                    }

                }

            }

            override fun onFailure(call: Call<ListBannersResponse>, t: Throwable) {
                bannersLiveData.value = mutableListOf()
                t.printStackTrace()
            }

        })

    }

    fun getListFilmes(movieId: Int) {

        ApiService.service.listSimilar(movieId).enqueue(object: Callback<ListFilmesResponse>{

            override fun onResponse(call: Call<ListFilmesResponse>, response: Response<ListFilmesResponse>) {

                if(!response.isSuccessful) {
                    onFailure(call, Throwable("Response não retornou com sucesso"))
                    return
                }

                val listFilmesResponse = response.body()

                if(listFilmesResponse == null) {
                    onFailure(call, Throwable("ResponseBody voltou nulo"))
                    return
                }

                if(listFilmesResponse.results.isEmpty()) {
                    onFailure(call, Throwable("Retornou uma lista vazia"))
                    return
                }

                listSimilaresLiveData.value = listFilmesResponse.results as MutableList<Filme>
                listSimilaresLiveData.value = listSimilaresLiveData.value

                getPosterFilmes(listFilmesResponse.results)

            }

            override fun onFailure(call: Call<ListFilmesResponse>, t: Throwable) {
                listSimilaresLiveData.value = mutableListOf()
                t.printStackTrace()
            }

        })

    }

    private fun getPosterFilmes(listFilmes: List<Filme>) {

        listFilmes.forEach { filme ->

            filme.poster_path?.let{
                    path -> DownloadImg.getImageFromApi(path, listSimilaresLiveData) { bitmap -> filme.poster = bitmap }
            }

        }

    }

}