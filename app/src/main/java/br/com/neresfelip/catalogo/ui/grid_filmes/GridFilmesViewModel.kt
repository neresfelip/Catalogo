package br.com.neresfelip.catalogo.ui.grid_filmes

import androidx.lifecycle.*
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.model.Filme
import br.com.neresfelip.catalogo.network.ApiService
import br.com.neresfelip.catalogo.network.response.ListFilmesResponse
import br.com.neresfelip.catalogo.network.utils.DownloadImg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GridFilmesViewModel : ViewModel() {

    private var loading = false

    private var page = 1

    val liveData: MutableLiveData<MutableList<Filme>> = MutableLiveData()

    // esta função será chamada casa ocorra algum erro ao buscar a lista de filmes
    var onListDownloadError: ((throwable: Throwable) -> Unit)? = null

    fun getListFilmes(id: Int) {

        if(loading) return

        loading = true

        //Definindo qual requisição fará de acordo com o ID recebido
        val call = when(id) {
            R.string.populares -> ApiService.service.listPopular(page)
            R.string.melhores_avaliados -> ApiService.service.listMelhoresAvaliados(page)
            R.string.prox_lancamentos -> ApiService.service.listProxLancamentos(page)
            else -> ApiService.service.listPorGenero(id, page)
        }

        call.enqueue(object: Callback<ListFilmesResponse>{

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
                    return
                }

                if(liveData.value == null)
                    liveData.value = listFilmesResponse.results as MutableList<Filme>
                else {
                    liveData.value!!.addAll(listFilmesResponse.results)
                    liveData.value = liveData.value
                }

                getPosterFilmes(listFilmesResponse.results)

                loading = false
                this@GridFilmesViewModel.page++

            }

            override fun onFailure(call: Call<ListFilmesResponse>, t: Throwable) {
                loading = false
                onListDownloadError?.let{ execute -> execute(t) }
            }

        })

    }

    /** este método percorrerá na lista de filmes para baixar os posters deles */
    private fun getPosterFilmes(listFilmes: List<Filme>) {

        listFilmes.forEach { filme ->

            filme.poster_path?.let{
                    path -> DownloadImg.getImageFromApi(path, liveData) { bitmap -> filme.poster = bitmap }
            }

        }

    }

}