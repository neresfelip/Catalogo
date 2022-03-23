package br.com.neresfelip.catalogo.model

import android.graphics.Bitmap

data class Genero (
    val id: Int,
    val name: String,
    var image: Bitmap?,
)