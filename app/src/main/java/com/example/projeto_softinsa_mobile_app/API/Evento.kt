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
import org.json.JSONException
import org.json.JSONObject

class Evento(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Evento(
        val eventoId: Int,
        val titulo: String,
        val descricao: String,
        val tipo: String,
        val dataInicio: String,
        val dataFim: String,
        val estadoId: Int?,
        val userId: Int,
        val notas: String
    ){
    }



    fun getEvento(id: Int, callback: GetEventoSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/evento/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val evento = Evento(
                        eventoId = dataObject.optInt("eventoId"),
                        titulo = dataObject.optString("titulo"),
                        descricao = dataObject.optString("descricao"),
                        tipo = dataObject.optString("tipo"),
                        dataInicio = dataObject.optString("dataInicio"),
                        dataFim = dataObject.optString("dataFim"),
                        estadoId = dataObject.optInt("estadoId"),
                        userId = dataObject.optInt("userId"),
                        notas = dataObject.optString("notas")
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
                val errorMessage = "Falha ao obter dados do evento Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listEventos(callback: GetEventoCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/evento/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_evento = ArrayList<imagem_evento>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val eventoId = item.getInt("eventoId")
                        val titulo = item.getString("titulo")
                        val descricao = item.getString("descricao")
                        val tipo = item.getString("tipo")
                        val dataInicio = item.getString("dataInicio")
                        val dataFim = item.getString("dataFim")
                        val estadoId = item.optInt("estadoId")
                        val userId = item.getInt("userId")
                        val notas = item.optString("notas")

                        val imgItem = imagem_evento()
                        imgItem.atr_eventoId_evento(eventoId)
                        imgItem.atr_titulo_evento(titulo)
                        imgItem.atr_descricao_evento(descricao)
                        imgItem.atr_tipo_evento(tipo)
                        imgItem.atr_dataInicio_evento(dataInicio)
                        imgItem.atr_dataFim_evento(dataFim)
                        imgItem.atr_estadoId_evento(estadoId)
                        imgItem.atr_userId_evento(userId)
                        imgItem.atr_notas_evento(notas )
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

    fun createEvento(titulo: String, descricao: String, tipo: String, dataInicio: String, dataFim: String, estadoId: Int,  userId: Int, notas: String, callback: GetEventoSingleCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/evento/create"

        val body = JSONObject()
        try {
            body.put("titulo", titulo)
            body.put("descricao", descricao)
            body.put("tipo", tipo)
            body.put("dataInicio", dataInicio)
            body.put("dataFim", dataFim)
            body.put("estadoId", estadoId)
            body.put("userId", userId)
            body.put("notas", notas)
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

                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val evento = Evento(
                        eventoId = dataObject.optInt("eventoId"),
                        titulo = dataObject.optString("titulo"),
                        descricao = dataObject.optString("descricao"),
                        tipo = dataObject.optString("tipo"),
                        dataInicio = dataObject.optString("dataInicio"),
                        dataFim = dataObject.optString("dataFim"),
                        estadoId = dataObject.optInt("estadoId"),
                        userId = dataObject.optInt("userId"),
                        notas = dataObject.optString("notas")
                    )

                    callback.onSuccess(evento)
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

    fun updateEvento(eventoId: Int, titulo: String, descricao: String, tipo: String, dataInicio: String, dataFim: String, estadoId: Int,  userId: Int, notas: String, callback: CreateEventoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/evento/update/${eventoId}"

        val body = JSONObject()
        try {
            body.put("eventoId", eventoId)
            body.put("titulo", titulo)
            body.put("descricao", descricao)
            body.put("tipo", tipo)
            body.put("dataInicio", dataInicio)
            body.put("dataFim", dataFim)
            body.put("estadoId", estadoId)
            body.put("userId", userId)
            body.put("notas", notas)

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

    fun deleteEvento(eventoId: Int, callback: CreateEventoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/evento/delete"

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

    interface GetEventoCallback {
        fun onSuccess(eventos: List<imagem_evento>)
        fun onFailure(errorMessage: String)
    }

    interface GetEventoSingleCallback {
        fun onSuccess(evento: Evento)
        fun onFailure(errorMessage: String)
    }

    interface CreateEventoCallback {
        fun onSuccess(evento: String)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}