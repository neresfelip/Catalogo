package br.com.neresfelip.catalogo.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.model.Filme
import br.com.neresfelip.catalogo.ui.filme_detalhe.FilmeDetalheActivity
import kotlinx.android.synthetic.main.item_filme.view.*

class FilmeAdapter(private val filmes : List<Filme>, val onItemClick: () -> Unit = {}) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        return FilmeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_filme, parent, false))
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.set(filmes[position])
    }

    override fun getItemCount() = filmes.count()

    inner class FilmeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster = itemView.filmePoster
        private val titulo = itemView.filmeTitulo
        private val iconImage = itemView.iconImage

        fun set(filme : Filme) {
            titulo.text = filme.title
            filme.poster?.let {bitmap ->
                poster.setImageBitmap(bitmap)
                titulo.visibility = View.GONE
                iconImage.visibility = View.GONE

                /**
                 * Este trecho permite que o usuário, ao segurar o clique, a imagem do poster do filme
                 * escureça para que ele possa ver o nome, se no poster não tiver o nome dele ou não tiver claro qual é
                 * */
                itemView.setOnLongClickListener {
                    titulo.visibility = when (titulo.visibility) {
                        View.GONE -> View.VISIBLE
                        else -> View.GONE
                    }
                    return@setOnLongClickListener true
                }

            }

            itemView.setOnClickListener {

                if(titulo.visibility == View.VISIBLE && filme.poster != null) {
                    titulo.visibility = View.GONE
                    return@setOnClickListener
                }

                val intent = Intent(itemView.context, FilmeDetalheActivity::class.java)
                intent.putExtra(FilmeDetalheActivity.EXTRA_ID_FILME, filme.id)
                itemView.context.startActivity(intent)
                onItemClick()
            }

        }

    }

}