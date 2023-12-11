    package com.example.projeto_softinsa_mobile_app.API
    import android.content.Context
    import android.content.SharedPreferences
    import com.android.volley.Request
    import com.android.volley.RequestQueue
    import com.android.volley.Response
    import com.android.volley.toolbox.JsonObjectRequest
    import com.android.volley.toolbox.Volley

    class Departamento(private val context: Context, private val editor: SharedPreferences.Editor?) {

        private lateinit var url: String

        private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        data class Departamento(
            val deparmentoId: Int,
            val departamentoNome: String,
            val dataCriacao: String,
            val descricao: String,
        )

        fun getDepartamento(callback: GetDepartamentoCallback, id: Int) {
            val url = "https://softinsa-web-app-carreiras01.onrender.com/departamento/get/$id"

            val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    if (response.has("data")) {
                        val dataObject = response.getJSONObject("data")
                            val departamento = Departamento(
                                deparmentoId = dataObject.optInt("deparmentoId"),
                                departamentoNome = dataObject.optString("departamentoNome"),
                                dataCriacao = dataObject.optString("dataCriacao"),
                                descricao = dataObject.optString("descricao"),
                        )


                        callback.onSuccess(departamento)
                    } else {
                        val errorMessage = "Resposta JSON Inválida"
                        callback.onFailure(errorMessage)
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    // Handle request error and call the onFailure callback with the error message
                    val errorMessage = "A obter a filial"
                    callback.onFailure(errorMessage)
                })

            requestQueue.add(request)
        }

        interface GetDepartamentoCallback {
            fun onSuccess(departamento: Departamento)
            fun onFailure(errorMessage: String)
        }
    }