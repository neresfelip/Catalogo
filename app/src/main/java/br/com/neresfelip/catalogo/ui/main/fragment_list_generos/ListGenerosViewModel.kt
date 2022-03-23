package br.com.neresfelip.catalogo.ui.main.fragment_list_generos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.neresfelip.catalogo.model.Genero
import br.com.neresfelip.catalogo.network.ApiService
import br.com.neresfelip.catalogo.network.response.ListFilmesResponse
import br.com.neresfelip.catalogo.network.response.ListGenerosResponse
import br.com.neresfelip.catalogo.network.utils.DownloadImg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListGenerosViewModel : ViewModel() {

    val generoLiveData: MutableLiveData<List<Genero>> = MutableLiveData()
    var onListDownloadError: ((throwable: Throwable) -> Unit)? = null

    fun getGeneros() {

        ApiService.service.getListGeneros()
            .enqueue(object: Callback<ListGenerosResponse>{

                override fun onResponse(call: Call<ListGenerosResponse>, response: Response<ListGenerosResponse>) {

                    if(response.isSuccessful) {
                        response.body()?.let {genResponse ->
                            generoLiveData.value = genResponse.genres
                            getGenerosImg(genResponse.genres)
                        }
                        Log.i("teste", response.body().toString())
                    }

                }

                override fun onFailure(call: Call<ListGenerosResponse>, t: Throwable) {}

            })

    }

    /** este método percorrerá na lista de gêneros e buscará na api a lista de filmes dele
     *  em seguida selecionará aleatoriamente um deles e puxará um banner dele para ser exibido
     *  com o nome dele na lista de gêneros
     *
     *  Dessa forma, todas as vezes que o usuário abrir a lista de gêneros, a imagem exibida em cada
     *  gênero da lista irá variar
     * */
    fun getGenerosImg(generos: List<Genero>) {

        generos.forEach {genero ->
            ApiService.service.listPorGenero(genero.id)
                .enqueue(object: Callback<ListFilmesResponse>{

                    override fun onResponse(call: Call<ListFilmesResponse>, response: Response<ListFilmesResponse>) {

                        if(response.isSuccessful && response.body() != null) {

                            val path: String? = response.body()!!.results[Random().nextInt(20)].backdrop_path

                            if(path != null)
                            DownloadImg.getImageFromApi(path, generoLiveData) {
                                bitmap -> genero.image = bitmap
                            }

                        }
                    }

                    override fun onFailure(call: Call<ListFilmesResponse>, t: Throwable) {
                        onListDownloadError?.let { execute -> execute(t) }
                    }

                })
        }

    }

}