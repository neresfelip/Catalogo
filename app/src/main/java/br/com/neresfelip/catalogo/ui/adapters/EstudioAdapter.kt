package br.com.neresfelip.catalogo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.neresfelip.catalogo.R
import br.com.neresfelip.catalogo.model.Estudio
import kotlinx.android.synthetic.main.item_estudio.view.*

class EstudioAdapter(private val estudios: List<Estudio>) : RecyclerView.Adapter<EstudioAdapter.EstudioHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudioHolder =
        EstudioHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_estudio, parent, false))

    override fun onBindViewHolder(holder: EstudioHolder, position: Int) {
        holder.set(estudios[position])
    }

    override fun getItemCount() = estudios.count()

    class EstudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun set(estudio: Estudio) {
            if(estudio.logo != null) {
                itemView.estudio_logo.setImageBitmap(estudio.logo)
            } else {
                itemView.estudio_nome.text = estudio.name
            }
        }
    }

}