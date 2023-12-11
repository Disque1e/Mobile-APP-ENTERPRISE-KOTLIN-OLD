package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_tipoProjeto
import org.json.JSONException

class Tipo_Projeto(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Tipo_Projeto(
        val tipoProjetoId: Int,
        val tipoProjetoNome: String,
    ) {
        // Additional methods or properties can be added here
    }




    fun getTipo_Projeto(id: Int, callback: GetTipo_ProjetoSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/tipo-projeto/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val area_negocio = Tipo_Projeto(
                        tipoProjetoId = dataObject.optInt("tipoProjetoId"),
                        tipoProjetoNome = dataObject.optString("tipoProjetoNome"),
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
                val errorMessage = "Falha ao obter dados do tipoProjeto Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listTipo_Projetos(callback: GetTipo_ProjetoCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/tipo-projeto/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_tipoProjeto = ArrayList<imagem_tipoProjeto>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val tipoProjetoId = item.getInt("tipoProjetoId")
                        val tipoProjetoNome = item.getString("tipoProjetoNome")

                        val imgItem = imagem_tipoProjeto()
                        imgItem.atr_tipoProjetoId_tipoProjeto(tipoProjetoId)
                        imgItem.atr_tipoProjetoNome_tipoProjeto(tipoProjetoNome)


                        ar_img_list_tipoProjeto.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_tipoProjeto)
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

    interface GetTipo_ProjetoCallback {
        fun onSuccess(tipo_projeto: List<imagem_tipoProjeto>)
        fun onFailure(errorMessage: String)
    }

    interface GetTipo_ProjetoSingleCallback {
        fun onSuccess(tipo_projeto: Tipo_Projeto)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}