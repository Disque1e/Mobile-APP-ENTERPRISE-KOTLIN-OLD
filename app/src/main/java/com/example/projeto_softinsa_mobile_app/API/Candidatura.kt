package com.example.projeto_softinsa_mobile_app.API;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.volley.Request
import com.android.volley.RequestQueue;
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley;
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_candidatura
import org.json.JSONException
import android.util.Base64
import android.util.Log
import org.json.JSONObject

class Candidatura(private val context:Context, private val editor: SharedPreferences.Editor?)  {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    private fun encodeCVData(cvData: ByteArray): String {
        return Base64.encodeToString(cvData, Base64.DEFAULT)
    }

    // Helper method to convert CV data from String to ByteArray
    private fun decodeCVData(cvData: String): ByteArray {
        return Base64.decode(cvData, Base64.DEFAULT)
    }

    fun listCandidaturas(callback: GetCandidaturaCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/candidatura/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_Candidatura= ArrayList<imagem_candidatura>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val candidaturaId = item.getInt("candidaturaId")
                        val dataCriacao = item.getString("dataCriacao")
                        val dataAtualizacao = item.getString("dataAtualizacao")
                        val isAtiva = item.getBoolean("isAtiva")
                        val userId = item.getInt("userId")
                        val vagaId = item.getInt("vagaId")
                        val cvDataObject = item.optJSONObject("cv")
                        val cvDataArray = cvDataObject?.optJSONArray("data")
                        val cv = cvDataArray?.let { jsonArray ->
                            val cvBytes = ByteArray(jsonArray.length())
                            for (i in 0 until jsonArray.length()) {
                                cvBytes[i] = jsonArray.getInt(i).toByte()
                            }
                            imagem_candidatura.CV(
                                type = cvDataObject?.optString("type"),
                                data = cvBytes.map { it.toInt() }
                            )
                        }


                        val imgItem = imagem_candidatura()
                        imgItem.atr_candidaturaId_candidatura(candidaturaId)
                        imgItem.atr_dataCriacao_candidatura(dataCriacao)
                        imgItem.atr_dataAtualizacao_candidatura(dataAtualizacao)
                        imgItem.atr_isAtiva_candidatura(isAtiva)
                        imgItem.atr_userId_candidatura(userId)
                        imgItem.atr_vagaId_candidatura(vagaId)

                        imgItem.atr_cv_candidatura(cv)


                        ar_img_list_Candidatura.add(imgItem)
                    }

                    callback.onSuccess(ar_img_list_Candidatura)
                } catch (e: JSONException) {
                    val errorMessage = "Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }

    fun listCandidaturasUser(userId: Int, callback: GetCandidaturaCallback) {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/candidatura/list"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    val ar_img_list_Candidatura = ArrayList<imagem_candidatura>()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val itemUserId = item.getInt("userId") // Get the userId from the JSON

                        if (itemUserId == userId) { // Check if the userId matches the desired userId
                            val candidaturaId = item.getInt("candidaturaId")
                            val dataCriacao = item.getString("dataCriacao")
                            val dataAtualizacao = item.getString("dataAtualizacao")
                            val isAtiva = item.getBoolean("isAtiva")
                            val vagaId = item.getInt("vagaId")
                            val cvDataObject = item.optJSONObject("cv")
                            val cvDataArray = cvDataObject?.optJSONArray("data")
                            val cv = cvDataArray?.let { jsonArray ->
                                val cvBytes = ByteArray(jsonArray.length())
                                for (i in 0 until jsonArray.length()) {
                                    cvBytes[i] = jsonArray.getInt(i).toByte()
                                }
                                imagem_candidatura.CV(
                                    type = cvDataObject?.optString("type"),
                                    data = cvBytes.map { it.toInt() }
                                )
                            }

                            val imgItem = imagem_candidatura()
                            imgItem.atr_candidaturaId_candidatura(candidaturaId)
                            imgItem.atr_dataCriacao_candidatura(dataCriacao)
                            imgItem.atr_dataAtualizacao_candidatura(dataAtualizacao)
                            imgItem.atr_isAtiva_candidatura(isAtiva)
                            imgItem.atr_userId_candidatura(userId) // Use the passed userId
                            imgItem.atr_vagaId_candidatura(vagaId)
                            imgItem.atr_cv_candidatura(cv)

                            ar_img_list_Candidatura.add(imgItem)
                        }
                    }

                    callback.onSuccess(ar_img_list_Candidatura)
                } catch (e: JSONException) {
                    val errorMessage = "Erro ao analisar a resposta do servidor: ${e.message}"
                    callback.onFailure(errorMessage)
                }
            },
            { error ->
                val errorMessage = "Erro ao obter dados do servidor: ${error.message}"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }


    fun createCandidatura(userId: Int, vagaId: Int, cvData: ByteArray, callback: GetCandidaturaSingleCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/candidatura/create"

        val body = JSONObject()
        try {
            body.put("cv", cvData)
            body.put("userId", userId)
            body.put("vagaId", vagaId)
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
                    callback.onSuccess(sucesso)
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

    fun deleteCandidatura(candidaturaId: Int, callback: CreateCandidaturaCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/projeto/delete"

        val body = JSONObject()
        try {
            body.put("candidaturaId", candidaturaId)

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

    interface CreateCandidaturaCallback {
        fun onSuccess(candidatura: String)
        fun onFailure(errorMessage: String)
    }

    interface GetCandidaturaCallback {
        fun onSuccess(candidaturas: List<imagem_candidatura>)
        fun onFailure(errorMessage: String)
    }

    interface GetCandidaturaSingleCallback {
        fun onSuccess(candidatura: Boolean)
        fun onFailure(errorMessage: String)
    }

}
