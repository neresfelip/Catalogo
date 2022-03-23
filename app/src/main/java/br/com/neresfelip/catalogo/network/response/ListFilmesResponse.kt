package br.com.neresfelip.catalogo.network.response

import br.com.neresfelip.catalogo.model.Filme

data class ListFilmesResponse (
    val page: Int,
    val results: List<Filme>,
    val total_pages: Int,
    val total_results: Int,
)