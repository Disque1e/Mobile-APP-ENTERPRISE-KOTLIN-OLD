package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_area_negocio
import org.json.JSONException

class Area_Negocio(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Area_Negocio(
        val areaNegocioId: Int,
        val areaNegocioNome: String,
    ) {
        // Additional methods or properties can be added here
    }




    fun getArea_Negocio(id: Int, callback: GetArea_NegocioSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/area-negocio/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val area_negocio = Area_Negocio(
                        areaNegocioId = dataObject.optInt("areaNegocioId"),
                        areaNegocioNome = dataObject.optString("areaNegocioNome"),
                    )

                    callback.onSuccess(area_negocio)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "Falha ao obter dados da areaNegocio Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listArea_Negocios(callback: GetArea_NegocioCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/area-negocio/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_area_negocio = ArrayList<imagem_area_negocio>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val areaNegocioId = item.getInt("areaNegocioId")
                        val areaNegocioNome = item.getString("areaNegocioNome")

                        val imgItem = imagem_area_negocio()
                        imgItem.atr_areaNegocioId_area_negocio(areaNegocioId)
                        imgItem.atr_areaNegocioNome_area_negocio(areaNegocioNome)


                        ar_img_list_area_negocio.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_area_negocio)
                } catch (e: JSONException) {
                    val errorMessage = "Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    interface GetArea_NegocioCallback {
        fun onSuccess(area_negocios: List<imagem_area_negocio>)
        fun onFailure(errorMessage: String)
    }

    interface GetArea_NegocioSingleCallback {
        fun onSuccess(area_negocio: Area_Negocio)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}