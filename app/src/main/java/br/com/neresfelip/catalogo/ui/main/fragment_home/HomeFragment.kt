package br.com.neresfelip.catalogo.ui.main.fragment_home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import br.com.neresfelip.catalogo.ui.grid_filmes.GridFilmesActivity
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.databinding.FragmentHomeBinding
import br.com.neresfelip.catalogo.databinding.ItemListFilmesBinding
import br.com.neresfelip.catalogo.model.Filme
import br.com.neresfelip.catalogo.ui.adapters.FilmeAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var autoScrollExecute = true

    lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewModel()

        return root

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        with(viewModel) { with(binding) {

            onListDownloadError = {
                AlertDialog.Builder(context)
                    .setMessage(R.string.sem_conexao)
                    .setNegativeButton(R.string.cancelar) {_,_ -> activity?.finish()}
                    .setOnCancelListener{ activity?.finish() }
                    .show()
            }

            setupListFilme(
                listPopulares,
                R.string.populares,
                listPopularesLiveData,
                getListPopulares(),
            )

            setupListFilme(
                listMaisVotados,
                R.string.melhores_avaliados,
                listMelhoresAvaliados,
                getMelhoresAvaliados(),
            )

            setupListFilme(
                listProximosLancamentos,
                R.string.prox_lancamentos,
                listProxLancamentosLiveData,
                getProxLancamentos(),
            )

        }}

    }

    /* Este método recebe as recyclerViews com lista de filmes definido no xml
    *  ele define o título e qual lista de filme ele deve receber da api (populares, mais votados ou lançamentos)
    * */
    private fun setupListFilme(view: ItemListFilmesBinding, resTitulo: Int, liveData: MutableLiveData<MutableList<Filme>>, getter: Unit) {

        view.titulo.text = getString(resTitulo)

        liveData.observe(this) {
            it?.let { filmes ->

                with(view.recyclerView) {
                    if(adapter == null)
                        adapter = FilmeAdapter(filmes)
                    else
                        adapter!!.notifyDataSetChanged()

                }

                view.progress.visibility = View.GONE

            }
        }

        /**
         * ao clicar no botão "ver mais" ele enviará para o GridFilmesActivity
         * o resouce id do titulo, por exemplo: (R.id.populares)
         * */
        view.btVerMais.setOnClickListener {
            val intent = Intent(context, GridFilmesActivity::class.java)
            intent.putExtra(GridFilmesActivity.EXTRA_ID_LIST, resTitulo)
            startActivity(intent)
        }

        try {
            return getter
        } catch (e: Exception) {
            AlertDialog.Builder(context)
                .setMessage(e.message)
                .setNegativeButton(getString(R.string.cancelar)) {_,_ -> activity?.finish()}
                .setOnCancelListener{ activity?.finish() }
                .show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollExecute = false
        _binding = null
    }

}