package com.example.projeto_softinsa_mobile_app.API

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Ideia(private val context: Context, private val editor: SharedPreferences.Editor) {

    private lateinit var url: String
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    data class Ideia(
        val IDIdeia: Int?,
        val Utilizador: Int,
        val TipoIdeia: Int,
        val Titulo: String,
        val Descricao: String
    )

    fun inserirIdeia(Utilizador: Int, TipoIdeia: Int, Titulo: String, Descricao: String, callback: GetIdeiaCallback) {
        url = "https://softinsa-web-app-carreiras01.onrender.com/ideia/create"

        val body = JSONObject()
        try {
            body.put("Utilizador", Utilizador)
            body.put("TipoIdeia", TipoIdeia)
            body.put("Titulo", Titulo)
            body.put("Descricao", Descricao)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val request = JsonObjectRequest(Request.Method.POST, url, body,
            Response.Listener { response ->
                // Print the response JSON for troubleshooting
                Log.d("Authorization", "Response JSON: $response")

                // Verifique se o registro foi bem-sucedido com base na resposta do servidor
                val success = response.optBoolean("success")
                if (success) {

                    callback.onSuccess(true)
                } else {
                    val errorMessage = "Resposta JSON inválida. Por favor, tente novamente mais tarde."
                    callback.onFailure(errorMessage)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                // Lida com o erro de registro e chama o onFailure callback com a mensagem de erro
                val errorMessage = "Erro a adicionar Idiea"
                callback.onFailure(errorMessage)
            })

        requestQueue.add(request)
    }



    interface GetIdeiaCallback {
        fun onSuccess(sucesso: Boolean)
        fun onFailure(errorMessage: String)
    }


}