package br.com.neresfelip.catalogo.ui.main.fragment_home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.neresfelip.catalogo.model.Filme
import br.com.neresfelip.catalogo.network.ApiService
import br.com.neresfelip.catalogo.network.response.ListFilmesResponse
import br.com.neresfelip.catalogo.network.utils.DownloadImg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    val listPopularesLiveData: MutableLiveData<MutableList<Filme>> = MutableLiveData()
    val listMelhoresAvaliados: MutableLiveData<MutableList<Filme>> = MutableLiveData()
    val listProxLancamentosLiveData: MutableLiveData<MutableList<Filme>> = MutableLiveData()

    var onListDownloadError: ((throwable: Throwable) -> Unit)? = null

    fun getListPopulares() {
        getListFilmes(ApiService.service.listPopular(), listPopularesLiveData)
    }

    fun getMelhoresAvaliados() {
        getListFilmes(ApiService.service.listMelhoresAvaliados(), listMelhoresAvaliados)
    }

    fun getProxLancamentos() {
        getListFilmes(ApiService.service.listProxLancamentos(), listProxLancamentosLiveData)
    }

    private fun getListFilmes(call: Call<ListFilmesResponse>, liveData: MutableLiveData<MutableList<Filme>>) {

        call.enqueue(object: Callback<ListFilmesResponse> {

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

                if(liveData.value == null)
                    liveData.value = listFilmesResponse.results as MutableList<Filme>
                else {
                    liveData.value!!.addAll(listFilmesResponse.results)
                    liveData.value = liveData.value
                }

                liveData.value?.forEach {
                    filme -> filme.poster_path?.let{
                        DownloadImg.getImageFromApi(filme.poster_path, liveData) { bitmap ->  filme.poster = bitmap}
                    }
                }

            }

            override fun onFailure(call: Call<ListFilmesResponse>, t: Throwable) {
                onListDownloadError?.let { execute -> execute(t) }
            }

        })

    }

}