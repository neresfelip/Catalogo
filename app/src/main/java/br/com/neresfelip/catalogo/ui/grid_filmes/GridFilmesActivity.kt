package br.com.neresfelip.catalogo.ui.grid_filmes

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.ui.adapters.FilmeAdapter
import kotlinx.android.synthetic.main.activity_grid_filmes.*

class GridFilmesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_LIST: String = "ID_LIST"
        const val EXTRA_GENERO: String = "NOME_GENERO"
    }

    private lateinit var viewModel: GridFilmesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_grid_filmes)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** Este Id pode ser recebido como um resourceId ou como um Id de gênero,
         * Ele definirá lá no ViewModel qual a lista de filmes que ele deve trazer */
        val id = intent.getIntExtra(EXTRA_ID_LIST, 0)

        title = resources.getText(id, intent.getStringExtra(EXTRA_GENERO))

        initViewModel(id)

        /**
         * Sempre que o usuário arrastar a tela até o final, o ViewModel vai adicionar mais filmes ao GridView
         */
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                val totalItemCount = recyclerView.adapter!!.itemCount
                val lastVisible = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()

                if (totalItemCount <= lastVisible+1){
                    viewModel.getListFilmes(id)
                }

            }

        })

    }

    private fun initViewModel(id: Int) {

        viewModel = ViewModelProvider(this)[GridFilmesViewModel::class.java]

        with(viewModel) {

            onListDownloadError = {

                AlertDialog.Builder(this@GridFilmesActivity)
                    .setMessage(R.string.sem_conexao)
                    .setNegativeButton(R.string.cancelar) {_,_ -> finish()}
                    .setOnCancelListener{ finish() }
                    .show()

            }

            liveData.observe(this@GridFilmesActivity) {filmes ->

                if(recyclerView.adapter == null)
                    recyclerView.adapter = FilmeAdapter(filmes)
                else
                    recyclerView.adapter!!.notifyDataSetChanged()

            }

            getListFilmes(id)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}