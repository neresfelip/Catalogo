package br.com.neresfelip.catalogo.network.response

import br.com.neresfelip.catalogo.model.Genero

data class ListGenerosResponse (
    val genres: List<Genero>
) {
    override fun toString(): String {
        return genres.toString()
    }
}