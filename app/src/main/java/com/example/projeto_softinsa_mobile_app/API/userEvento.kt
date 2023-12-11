package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_evento
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_user_evento
import org.json.JSONException
import org.json.JSONObject

class userEvento(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class userEvento(
        val eventoId: Int,
        val userId: Int,
        val titulo: String,
        val descricao: String,
        val dataInicio: String,
        val dataFim: String,
        val tipo: String,
        val email: String,
        val nome: String
    ){
    }



    fun getUserEvento(id: Int, callback: GetUserEventoSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/eventouser/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val evento = userEvento(
                        eventoId = dataObject.optInt("eventoId"),
                        userId = dataObject.optInt("userId"),
                        titulo = dataObject.optString("titulo"),
                        descricao = dataObject.optString("descricao"),
                        tipo = dataObject.optString("tipo"),
                        dataInicio = dataObject.optString("dataInicio"),
                        dataFim = dataObject.optString("dataFim"),
                        email = dataObject.optString("email"),
                        nome = dataObject.optString("nome")
                    )

                    callback.onSuccess(evento)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "Falha ao obter dados do User Evento Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listUserEventos(callback: GetUserEventoCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/eventouser/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_evento = ArrayList<imagem_user_evento>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val eventoId = item.getInt("eventoId")
                        val userId = item.getInt("userId")
                        val titulo = item.getString("titulo")
                        val descricao = item.getString("descricao")
                        val dataInicio = item.getString("dataInicio")
                        val dataFim = item.getString("dataFim")
                        val tipo = item.getString("tipo")
                        val email = item.optString("email")
                        val nome = item.optString("nome")

                        val imgItem = imagem_user_evento()
                        imgItem.atr_eventoId_img_user_evento(eventoId)
                        imgItem.atr_userId_img_user_evento(userId)
                        imgItem.atr_titulo_img_user_evento(titulo)
                        imgItem.atr_descricao_img_user_evento(descricao)
                        imgItem.atr_dataInicio_img_user_evento(dataInicio)
                        imgItem.atr_dataFim_img_user_evento(dataFim)
                        imgItem.atr_tipo_img_user_evento(tipo)
                        imgItem.atr_email_img_user_evento(email)
                        imgItem.atr_nome_img_user_evento(nome)
                        ar_img_list_evento.add(imgItem)

                    }

                    callback.onSuccess(ar_img_list_evento)
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

    fun createEvento(eventoId: Int, userId: Int, titulo: String, descricao: String, tipo: String, dataInicio: String, dataFim: String, email: String, nome: String, callback: CreateUserEventoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/eventouser/create"

        val body = JSONObject()
        try {
            body.put("eventoId", eventoId)
            body.put("userId", userId)
            body.put("titulo", titulo)
            body.put("descricao", descricao)
            body.put("tipo", tipo)
            body.put("dataInicio", dataInicio)
            body.put("dataFim", dataFim)
            body.put("email", email)
            body.put("nome", nome)
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

    fun deleteEvento(eventoId: Int, callback: CreateUserEventoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/eventouser/delete"

        val body = JSONObject()
        try {
            body.put("eventoId", eventoId)

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

    interface GetUserEventoCallback {
        fun onSuccess(eventos: List<imagem_user_evento>)
        fun onFailure(errorMessage: String)
    }

    interface GetUserEventoSingleCallback {
        fun onSuccess(evento: userEvento)
        fun onFailure(errorMessage: String)
    }

    interface CreateUserEventoCallback {
        fun onSuccess(evento: String)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}

