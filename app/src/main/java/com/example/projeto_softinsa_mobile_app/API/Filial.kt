package com.example.projeto_softinsa_mobile_app.API
import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class Filial(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    data class Filial(
        val filialId: Int,
        val filialNome: String,
        val morada: String,
        val telemovel: String,
        val email: String,
    )

    fun getFilial(callback: GetFilialCallback, id: Int) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/filial/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")
                        val filial = Filial(
                            filialId = dataObject.optInt("filialId"),
                            filialNome = dataObject.optString("filialNome"),
                            morada = dataObject.optString("morada"),
                            telemovel = dataObject.optString("telemovel"),
                            email = dataObject.optString("email")

                    )
                    callback.onSuccess(filial)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "A obter a filial"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    interface GetFilialCallback {
        fun onSuccess(user: Filial)
        fun onFailure(errorMessage: String)
    }
}