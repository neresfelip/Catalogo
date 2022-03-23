package br.com.neresfelip.catalogo.network.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import br.com.neresfelip.catalogo.network.ApiImgService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DownloadImg {

    fun <T : Any> getImageFromApi(path: String, liveData: MutableLiveData<T>, retorno: (bitmap: Bitmap) -> Unit) {
        call(ApiImgService.service.getImage(path), liveData, retorno)
    }

    fun <T : Any> getImageW300FromApi(path: String, liveData: MutableLiveData<T>, retorno: (bitmap: Bitmap) -> Unit) {
        call(ApiImgService.service.getImageW300(path), liveData, retorno)
    }

    fun <T : Any> getImageW780FromApi(path: String, liveData: MutableLiveData<T>, retorno: (bitmap: Bitmap) -> Unit) {
        call(ApiImgService.service.getImageW780(path), liveData, retorno)
    }

    private fun <T : Any> call(call: Call<ResponseBody>, liveData: MutableLiveData<T>, retorno: (bitmap: Bitmap) -> Unit) {

        call.enqueue(object: Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                retorno(BitmapFactory.decodeStream(response.body()?.byteStream()))
                liveData.value = liveData.value
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                liveData.value = liveData.value
                t.printStackTrace()
            }

        })
    }

}