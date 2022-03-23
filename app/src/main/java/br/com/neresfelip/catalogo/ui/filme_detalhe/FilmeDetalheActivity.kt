package br.com.neresfelip.catalogo.ui.filme_detalhe

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.ui.adapters.EstudioAdapter
import br.com.neresfelip.catalogo.ui.adapters.FilmeAdapter
import br.com.neresfelip.catalogo.ui.adapters.GeneroAdapter
import br.com.neresfelip.catalogo.ui.adapters.BannerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_filme_detalhe.*
import kotlinx.android.synthetic.main.item_list_filmes.view.*

class FilmeDetalheActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID_FILME : String = "ID_FILME"
    }

    private var filmeId: Int = 0
    private lateinit var viewModel: FilmeDetalheViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filme_detalhe)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = null

        filmeId = intent.getIntExtra(EXTRA_ID_FILME, 0)

        initViewModel()

        filme_similares.titulo.text = getString(R.string.similares)
        filme_similares.bt_ver_mais.visibility = View.GONE

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(this)[FilmeDetalheViewModel::class.java]

        with(viewModel) {

            val owner: LifecycleOwner = this@FilmeDetalheActivity

            onDownloadError = {
                AlertDialog.Builder(this@FilmeDetalheActivity)
                    .setMessage(R.string.sem_conexao)
                    .setNegativeButton(R.string.sem_conexao) {_,_ -> finish()}
                    .setOnCancelListener{ finish() }
                    .show()
            }

            filmeLiveData.observe(owner) {filme ->

                with(filme) {

                    if(poster != null) {
                        filme_poster.setImageBitmap(poster)
                        filme_poster_progress.visibility = View.GONE
                    } else if(poster_path == null) {
                        filme_poster_progress.visibility = View.GONE
                    }

                    filme_titulo.text = title
                    filme_original_titulo.text = original_title
                    filme_avaliacao.text = "($vote_average)"
                    filme_avaliacao_ratingbar.rating = (vote_average/2).toFloat()

                    filme_lancamento.text = when {
                        release_date.isNotEmpty() -> release_date
                        else -> getString(R.string.indefinido)
                    }

                    filme_generos.adapter = GeneroAdapter(genres, true)
                    filme_sinopse.text = when {
                        overview.isNotEmpty() -> overview
                        else -> getString(R.string.sem_sinopse)
                    }

                    if (production_companies.isNotEmpty())
                        filme_estudios.adapter = EstudioAdapter(production_companies)
                    else
                        filme_estudios_vazio.visibility = View.VISIBLE

                }

            }
            getFilme(filmeId)

            bannersLiveData.observe(owner) { imgList ->

                with(banners) {
                    if(adapter == null) {
                        adapter = BannerAdapter(imgList)
                        TabLayoutMediator(bannersIndicators, banners) { _,_ ->}.attach()
                    } else
                        adapter!!.notifyDataSetChanged()
                }

                Log.i("teste", "aqui")

                filme_banner_progress.visibility = View.GONE

            }
            getFilmeBanners(filmeId)

            listSimilaresLiveData.observe(owner) { filmes ->

                with(filme_similares.recyclerView){

                    if(adapter == null)
                        adapter = FilmeAdapter(filmes) { finish() }
                    else
                        adapter!!.notifyDataSetChanged()

                }

                filme_similares.progress.visibility = View.GONE

            }
            getListFilmes(filmeId)

        }

    }

}