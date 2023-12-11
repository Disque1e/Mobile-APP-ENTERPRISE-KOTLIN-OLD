package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_parceria
import org.json.JSONException
import org.json.JSONObject

class Parceria(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Parceria(
        val parceriaId: Int,
        val nomeParceiro: String,
        val email: String,
        val telemovel: String,
        val dataRegisto: String?,
        val dataAtualizacao: String?,
        val userId: Int,
    ){
        // Additional methods or properties can be added here
    }


    fun getParceria(id: Int, callback: GetParceriaSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/parceria/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val parceria = Parceria(
                        parceriaId = dataObject.optInt("parceriaId"),
                        nomeParceiro = dataObject.optString("nomeParceiro"),
                        email = dataObject.optString("email"),
                        telemovel = dataObject.optString("telemovel"),
                        dataRegisto = dataObject.optString("dataRegisto"),
                        dataAtualizacao = dataObject.optString("dataAtualizacao"),
                        userId = dataObject.optInt("userId")
                    )

                    callback.onSuccess(parceria)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "Falha ao obter dados do parceria Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listParcerias(callback: GetParceriaCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/parceria/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_parceria = ArrayList<imagem_parceria>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val parceriaId = item.getInt("parceriaId")
                        val nomeParceiro = item.getString("nomeParceiro")
                        val email = item.getString("email")
                        val telemovel = item.getString("telemovel")
                        val dataRegisto = item.getString("dataRegisto")
                        val dataAtualizacao = item.getString("dataAtualizacao")
                        val userId = item.getInt("userId")

                        val imgItem = imagem_parceria()
                        imgItem.atr_parceriaId_parceria(parceriaId)
                        imgItem.atr_nomeParceiro_parceria(nomeParceiro)
                        imgItem.atr_email_parceria(email)
                        imgItem.atr_telemovel_parceria(telemovel)
                        imgItem.atr_dataRegisto_parceria(dataRegisto)
                        imgItem.atr_dataAtualizacao_parceria(dataAtualizacao)
                        imgItem.atr_userId_parceria(userId)

                        ar_img_list_parceria.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_parceria)
                } catch (e: JSONException) {
                    val errorMessage = "Parcerias - Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun createParceria(userId: Int, nomeParceiro: String, email: String, telemovel: String, callback: CreateParceriaCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/parceria/create"

        val body = JSONObject()
        try {
            body.put("nomeParceiro", nomeParceiro)
            body.put("email", email)
            body.put("telemovel", telemovel)
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
                    callback.onFailure(response.toString())
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            })

        Log.d("tag", "API Request: URL: $url")
        Log.d("tag", "API Request: Body: ${body.toString()}")

        requestQueue.add(request)
    }

    fun updateParceria(parceriaId: Int, nomeParceiro: String, email: String, telemovel: String, callback: CreateParceriaCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/parceria/update/${parceriaId}"

        val body = JSONObject()
        try {
            body.put("parceriaId", parceriaId)
            body.put("nomeParceiro", nomeParceiro)
            body.put("email", email)
            body.put("telemovel", telemovel)
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
                    callback.onFailure(response.toString())
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                val errorMessage = "Erro a atualizar parceria!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun deleteParceria(parceriaId: Int, callback: CreateParceriaCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/parceria/delete"

        val body = JSONObject()
        try {
            body.put("parceriaIds", parceriaId)

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
                    callback.onFailure(response.toString())
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Handle request error and call the onFailure callback with the error message
                callback.onFailure(error.toString())
            })

        requestQueue.add(request)
    }

    interface CreateParceriaCallback {
        fun onSuccess(parceria: String)
        fun onFailure(errorMessage: String)
    }

    interface GetParceriaCallback {
        fun onSuccess(parcerias: List<imagem_parceria>)
        fun onFailure(errorMessage: String)
    }

    interface GetParceriaSingleCallback {
        fun onSuccess(parceria: Parceria)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}