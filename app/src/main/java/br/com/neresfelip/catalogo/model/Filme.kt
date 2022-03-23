package br.com.neresfelip.catalogo.model

import android.graphics.Bitmap
import java.util.*

data class Filme (
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val vote_average: Double,
    val vote_count: Int,
    val poster_path: String?,
    val backdrop_path: String?,
    val genres: List<Genero>,
    val homepage: String?,
    val production_companies: List<Estudio>,
    var poster: Bitmap?,
) {

    val release_date: String = ""
        get() =
            if(field.length==10)
                "${field.substring(8)}-${field.substring(5, 7)}-${field.substring(0, 4)}"
            else field

}