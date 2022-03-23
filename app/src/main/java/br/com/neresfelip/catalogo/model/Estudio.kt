package br.com.neresfelip.catalogo.model

import android.graphics.Bitmap

class Estudio (
    val id: Int,
    val name: String,
    val logo_path: String?,
    val origin_country: String,
    var logo: Bitmap?,
)