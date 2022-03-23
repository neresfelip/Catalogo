package br.com.neresfelip.catalogo.ui.main.fragment_list_generos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.databinding.FragmentListGenerosBinding
import br.com.neresfelip.catalogo.ui.adapters.GeneroAdapter

class ListGenerosFragment : Fragment() {

    private var _binding: FragmentListGenerosBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentListGenerosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewModel()

        return root
    }

    private fun initViewModel() {

        val viewModel: ListGenerosViewModel = ViewModelProvider(this)[ListGenerosViewModel::class.java]

        with(viewModel) {

            onListDownloadError = {
                AlertDialog.Builder(context)
                    .setMessage(R.string.sem_conexao)
                    .setNegativeButton(R.string.cancelar) {_,_ -> activity?.finish()}
                    .setOnCancelListener{ activity?.finish() }
                    .show()
            }

            generoLiveData.observe(this@ListGenerosFragment) {
                generos -> binding.recyclerView.adapter = GeneroAdapter(generos, false)
                    binding.progress.visibility = View.GONE
            }
            getGeneros()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}