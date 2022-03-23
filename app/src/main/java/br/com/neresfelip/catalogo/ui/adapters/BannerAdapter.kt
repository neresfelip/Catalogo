package br.com.neresfelip.catalogo.ui.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.neresfelip.catalogo.R
import kotlinx.android.synthetic.main.item_poster.view.*

class BannerAdapter(private val imagens: List<Bitmap>) : RecyclerView.Adapter<BannerAdapter.PosterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PosterHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_poster, parent, false))

    override fun onBindViewHolder(holder: PosterHolder, position: Int) {
        holder.set(imagens[position])
    }

    override fun getItemCount() = imagens.count()

    class PosterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun set(imagem: Bitmap) {
            itemView.posterImage.setImageBitmap(imagem)
        }

    }

}