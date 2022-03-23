package br.com.neresfelip.catalogo.network.response

import br.com.neresfelip.catalogo.model.Banner

data class ListBannersResponse (
    val backdrops: List<Banner>,
)