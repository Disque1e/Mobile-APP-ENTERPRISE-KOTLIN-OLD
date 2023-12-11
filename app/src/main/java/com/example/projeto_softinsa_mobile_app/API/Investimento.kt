package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_investimento
import org.json.JSONException
import org.json.JSONObject

class Investimento(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Investimento(
        val investimentoId: Int,
        val montante: String,
        val descricao: String,
        val dataRegisto: String?,
        val dataAtualizacao: String?,
        val dataInvestimento: String?,
        val estadoId: Int?,
        val userId: Int,
    ){
        // Additional methods or properties can be added here
    }




    fun getInvestimento(id: Int, callback: GetInvestimentoSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/investimento/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val investimento = Investimento(
                        investimentoId = dataObject.optInt("investimentoId"),
                        montante = dataObject.optString("montante"),
                        descricao = dataObject.optString("descricao"),
                        dataRegisto = dataObject.optString("dataRegisto"),
                        dataAtualizacao = dataObject.optString("dataAtualizacao"),
                        dataInvestimento = dataObject.optString("dataInvestimento"),
                        estadoId = dataObject.optInt("estadoId"),
                        userId = dataObject.optInt("userId"),
                    )

                    callback.onSuccess(investimento)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "Falha ao obter dados do investimento Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listInvestimentos(callback: GetInvestimentoCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/investimento/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_investimento = ArrayList<imagem_investimento>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val investimentoId = item.getInt("investimentoId")
                        val montante = item.getString("montante")
                        val descricao = item.getString("descricao")
                        val dataRegisto = item.getString("dataRegisto")
                        val dataAtualizacao = item.getString("dataAtualizacao")
                        val dataInvestimento = item.getString("dataInvestimento")
                        val estadoId = item.optInt("estadoId")
                        val userId = item.getInt("userId")

                        val imgItem = imagem_investimento()
                        imgItem.atr_investimentoId_investimento(investimentoId)
                        imgItem.atr_montante_investimento(montante)
                        imgItem.atr_descricao_investimento(descricao)
                        imgItem.atr_dataRegisto_investimento(dataRegisto)
                        imgItem.atr_dataAtualizacao_investimento(dataAtualizacao)
                        imgItem.atr_dataInvestimento_investimento(dataInvestimento)
                        imgItem.atr_estadoId_investimento(estadoId)
                        imgItem.atr_userId_investimento(userId)


                        ar_img_list_investimento.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_investimento)
                } catch (e: JSONException) {
                    val errorMessage = "Investimentos - Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun createInvestimento(userId: Int, montante: String, descricao: String, callback: CreateInvestimentoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/investimento/create"

        val body = JSONObject()
        try {
            body.put("montante", montante)
            body.put("descricao", descricao)
            body.put("userId", userId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            Response.Listener { response ->
                if (response.has("data")) {
                    val responseData = response.toString()
                    Log.d("tag", responseData)
                    val sucesso = true
                    callback.onSuccess(responseData)
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        Log.d("tag", "API Request: URL: $url")
        Log.d("tag", "API Request: Body: ${body.toString()}")

        requestQueue.add(request)
    }

    fun updateInvestimento(investimentoId: Int, montante: String, descricao: String, callback: CreateInvestimentoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/investimento/update/${investimentoId}"

        val body = JSONObject()
        try {
            body.put("montante", montante)
            body.put("descricao", descricao)
            body.put("investimentoId", investimentoId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, body,
            Response.Listener { response ->
                // Check if the response JSON contains the field "data"
                if (response.has("data")) {
                    callback.onSuccess(response.toString())
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro a atualizar investimento!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun updateInvestimentoEstado(investimentoId: Int, estadoId: Int, callback: CreateInvestimentoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/update/${investimentoId}"

        val body = JSONObject()
        try {
            body.put("estadoId", estadoId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, body,
            Response.Listener { response ->
                // Check if the response JSON contains the field "data"
                if (response.has("data")) {
                    callback.onSuccess(response.toString())
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro a atualizar estado do investimento!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun deleteInvestimento(investimentoId: Int, callback: CreateInvestimentoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/investimento/delete"

        val body = JSONObject()
        try {
            body.put("investimentoIds", investimentoId)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, body,
            Response.Listener { response ->
                // Check if the response JSON contains the field "data"
                if (response.has("message")) {
                    callback.onSuccess(response.toString())
                } else {
                    val errorMessage = "Resposta JSON Inválida"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                callback.onFailure(error.toString())
            })

        requestQueue.add(request)
    }

    interface CreateInvestimentoCallback {
        fun onSuccess(investimento: String)
        fun onFailure(errorMessage: String)
    }


    interface GetInvestimentoCallback {
        fun onSuccess(investimentos: List<imagem_investimento>)
        fun onFailure(errorMessage: String)
    }

    interface GetInvestimentoSingleCallback {
        fun onSuccess(investimento: Investimento)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}