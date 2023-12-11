package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_negocio
import org.json.JSONException
import org.json.JSONObject

class Negocio(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Negocio(
        val negocioId: Int,
        val email: String,
        val telemovel: String,
        val orcamento: String,
        val descricao: String,
        val dataRegisto: String?,
        val dataAtualizacao: String?,
        val estadoId: Int?,
        val userId: Int,
        val areaNegocioId: Int?
    ){
        // Additional methods or properties can be added here
    }




    fun getNegocio(id: Int, callback: GetNegocioSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val negocio = Negocio(
                        negocioId = dataObject.optInt("negocioId"),
                        email = dataObject.optString("email"),
                        telemovel = dataObject.optString("telemovel"),
                        orcamento = dataObject.optString("orcamento"),
                        descricao = dataObject.optString("descricao"),
                        dataRegisto = dataObject.optString("dataRegisto"),
                        dataAtualizacao = dataObject.optString("dataAtualizacao"),
                        estadoId = dataObject.optInt("estadoId"),
                        userId = dataObject.optInt("userId"),
                        areaNegocioId = dataObject.optInt("areaNegocioId")
                    )

                    callback.onSuccess(negocio)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "Falha ao obter dados do negocio Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listNegocios(callback: GetNegocioCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_negocio = ArrayList<imagem_negocio>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val negocioId = item.getInt("negocioId")
                        val email = item.getString("email")
                        val telemovel = item.getString("telemovel")
                        val orcamento = item.getString("orcamento")
                        val descricao = item.getString("descricao")
                        val dataRegisto = item.getString("dataRegisto")
                        val dataAtualizacao = item.getString("dataAtualizacao")
                        val estadoId = item.optInt("estadoId")
                        val userId = item.getInt("userId")
                        val areaNegocioId = item.optInt("areaNegocioId")

                        val imgItem = imagem_negocio()
                        imgItem.atr_negocioId_negocio(negocioId)
                        imgItem.atr_email_negocio(email)
                        imgItem.atr_telemovel_negocio(telemovel)
                        imgItem.atr_orcamento_negocio(orcamento)
                        imgItem.atr_descricao_negocio(descricao)
                        imgItem.atr_dataRegisto_negocio(dataRegisto)
                        imgItem.atr_dataAtualizacao_negocio(dataAtualizacao)
                        imgItem.atr_estadoId_negocio(estadoId)
                        imgItem.atr_userId_negocio(userId)
                        imgItem.atr_areaNegocioId_negocio(areaNegocioId )

                        ar_img_list_negocio.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_negocio)
                } catch (e: JSONException) {
                    val errorMessage = "Negocios - Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun createNegocio(userId: Int, email: String, telemovel: String, orcamento: String, descricao: String, areaNegocioId: Int, callback: CreateNegocioCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/create"

        val body = JSONObject()
        try {
            body.put("email", email)
            body.put("telemovel", telemovel)
            body.put("orcamento", orcamento)
            body.put("descricao", descricao)
            body.put("userId", userId)
            body.put("areaNegocioId", areaNegocioId)
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

    fun updateNegocio(negocioId: Int, email: String?, telemovel: String?, orcamento: String, descricao: String, areaNegocioId: Int, callback: CreateNegocioCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/update/${negocioId}"

        val body = JSONObject()
        try {
            body.put("email", email)
            body.put("telemovel", telemovel)
            body.put("orcamento", orcamento)
            body.put("descricao", descricao)
            body.put("areaNegocioId", areaNegocioId)

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
                val errorMessage = "Erro a atualizar utilizador!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun updateNegocioEstado(negocioId: Int, estadoId: Int, callback: CreateNegocioCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/update/${negocioId}"

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
                val errorMessage = "Erro a atualizar utilizador!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun deleteNegocio(negocioId: Int, callback: CreateNegocioCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/delete"

        val body = JSONObject()
        try {
            body.put("negocioIds", negocioId)

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

    interface GetNegocioCallback {
        fun onSuccess(negocios: List<imagem_negocio>)
        fun onFailure(errorMessage: String)
    }

    interface GetNegocioSingleCallback {
        fun onSuccess(negocio: Negocio)
        fun onFailure(errorMessage: String)
    }

    interface CreateNegocioCallback {
        fun onSuccess(negocio: String)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}