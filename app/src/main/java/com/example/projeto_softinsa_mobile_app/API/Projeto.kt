package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_projeto
import org.json.JSONException
import org.json.JSONObject

class Projeto(private val context: Context, private val editor: SharedPreferences.Editor?) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    class Projeto(
        val projetoId: Int,
        val projetoNome: String,
        val descricao: String,
        val orcamento: String,
        val prioridade: String,
        val dataRegisto: String,
        val dataAtualizacao: String?,
        val dataInicio: String?,
        val dataFim: String?,
        val estadoId: Int?,
        val userId: Int,
        val tipoProjetoId: Int
    ) {
        // Additional methods or properties can be added here
    }




    fun getProjeto(id: Int, callback: GetProjetoSingleCallback)
    {
        url = "https://softinsa-web-app-carreiras01.onrender.com/projeto/get/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // Verificar se a resposta JSON contém o campo "data"
                if (response.has("data")) {
                    val dataObject = response.getJSONObject("data")

                    // Extrair os campos desejados do objeto "data"
                    val projeto = Projeto(
                        projetoId = dataObject.optInt("projetoId"),
                        projetoNome = dataObject.optString("projetoNome"),
                        descricao = dataObject.optString("descricao"),
                        orcamento = dataObject.optString("orcamento"),
                        prioridade = dataObject.getString("prioridade"),
                        dataRegisto = dataObject.optString("dataRegisto"),
                        dataAtualizacao = dataObject.optString("dataAtualizacao"),
                        dataInicio = dataObject.optString("dataInicio"),
                        dataFim = dataObject.optString("dataFim"),
                        estadoId = dataObject.optInt("estadoId"),
                        userId = dataObject.optInt("userId"),
                        tipoProjetoId = dataObject.optInt("tipoProjetoId")
                    )

                    callback.onSuccess(projeto)
                } else {
                    // Lidar com a resposta JSON inválida ou ausência do campo "data"
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lidar com o erro de requisição e chamar o callback onFailure com a mensagem de erro
                val errorMessage = "Falha ao obter dados do projeto Por favor, tente novamente mais tarde."
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun listProjetos(callback: GetProjetoCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/projeto/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_projetotunidade = ArrayList<imagem_projeto>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val projetoId = item.getInt("projetoId")
                        val projetoNome = item.getString("projetoNome")
                        val descricao = item.getString("descricao")
                        val orcamento = item.getString("orcamento")
                        val prioridade = item.getString("prioridade")
                        val dataRegisto = item.getString("dataRegisto")
                        val dataAtualizacao = item.getString("dataAtualizacao")
                        val dataInicio = item.getString("dataInicio")
                        val dataFim = item.getString("dataFim")
                        val estadoId = item.optInt("estadoId")
                        val userId = item.getInt("userId")
                        val tipoProjetoId = item.getInt("tipoProjetoId")

                        val imgItem = imagem_projeto()
                        imgItem.atr_projetoId_projeto(projetoId)
                        imgItem.atr_projetoNome_projeto(projetoNome)
                        imgItem.atr_descricao_projeto(descricao)
                        imgItem.atr_orcamento_projeto(orcamento)
                        imgItem.atr_prioridade_projeto(prioridade)
                        imgItem.atr_dataRegisto_projeto(dataRegisto)
                        imgItem.atr_dataInicio_projeto(dataInicio)
                        imgItem.atr_dataAtualizacao_projeto(dataAtualizacao)
                        imgItem.atr_dataFim_projeto(dataFim)
                        imgItem.atr_estadoId_projeto(estadoId)
                        imgItem.atr_userId_projeto(userId)
                        imgItem.atr_tipoProjetoId_projeto(tipoProjetoId)

                        ar_img_list_projetotunidade.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_projetotunidade)
                } catch (e: JSONException) {
                    val errorMessage = "Projeto - Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun createProjeto(userId: Int, projetoNome: String, descricao: String, orcamento: String, prioridade: Int, tipoProjetoId: Int, callback: CreateProjetoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/projeto/create"

        val body = JSONObject()
        try {
            body.put("userId", userId)
            body.put("projetoNome", projetoNome)
            body.put("descricao", descricao)
            body.put("orcamento", orcamento)
            body.put("prioridade", prioridade)
            body.put("tipoProjetoId", tipoProjetoId)
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

    fun updateProjeto(projetoId: Int, projetoNome: String, descricao: String, orcamento: String, prioridade: String?, tipoProjetoId: Int, callback: CreateProjetoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/projeto/update/${projetoId}"

        val body = JSONObject()
        try {
            body.put("projetoNome", projetoNome)
            body.put("descricao", descricao)
            body.put("orcamento", orcamento)
            body.put("prioridade", prioridade)
            body.put("tipoProjetoId", tipoProjetoId)
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
                val errorMessage = "Erro a atualizar projeto!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun updateProjetoEstado(projetoId: Int, estadoId: Int, callback: CreateProjetoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/negocio/update/${projetoId}"

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
                val errorMessage = "Erro a atualizar estado do projeto!"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun deleteProjeto(projetoId: Int, callback: CreateProjetoCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/projeto/delete"

        val body = JSONObject()
        try {
            body.put("projetoIds", projetoId)

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

    interface CreateProjetoCallback {
        fun onSuccess(projeto: String)
        fun onFailure(errorMessage: String)
    }

    interface GetProjetoCallback {
        fun onSuccess(projetos: List<imagem_projeto>)
        fun onFailure(errorMessage: String)
    }

    interface GetProjetoSingleCallback {
        fun onSuccess(projeto: Projeto)
        fun onFailure(errorMessage: String)
    }

    interface GetUpdateCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }

}