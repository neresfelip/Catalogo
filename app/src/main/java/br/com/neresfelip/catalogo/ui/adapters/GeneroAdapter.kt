package br.com.neresfelip.catalogo.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.model.Genero
import br.com.neresfelip.catalogo.ui.grid_filmes.GridFilmesActivity
import kotlinx.android.synthetic.main.item_genero.view.*

class GeneroAdapter(
    private val generos: List<Genero>,
    private val mini: Boolean
    ) : RecyclerView.Adapter<GeneroAdapter.AbsGeneroHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsGeneroHolder {
        return when {
            mini -> GeneroMiniHolder(parent)
            else -> GeneroHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: AbsGeneroHolder, position: Int) {
        holder.set(generos[position])
    }

    override fun getItemCount() = generos.count()

    class GeneroHolder(
        parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_genero, parent, false)
    ) : AbsGeneroHolder(itemView) {

        override fun set(genero: Genero) {
            itemView.nome.text = genero.name
            genero.image?.let {bitmap ->
                itemView.imgFundo.setImageBitmap(bitmap)
                itemView.imgEfeito.visibility = View.VISIBLE
            }


            /**
             * ao clicar num gênero ele enviará para o GridFilmesActivity o ID do gênero
             * */
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, GridFilmesActivity::class.java)
                intent.putExtra(GridFilmesActivity.EXTRA_ID_LIST, genero.id)
                intent.putExtra(GridFilmesActivity.EXTRA_GENERO, genero.name)
                itemView.context.startActivity(intent)
            }
        }

    }

    class GeneroMiniHolder(
        parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_genero_mini, parent, false)
    ) : AbsGeneroHolder(itemView) {

        override fun set(genero: Genero) {
            itemView.nome.text = genero.name
        }

    }

    abstract class AbsGeneroHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun set(genero: Genero)
    }

}