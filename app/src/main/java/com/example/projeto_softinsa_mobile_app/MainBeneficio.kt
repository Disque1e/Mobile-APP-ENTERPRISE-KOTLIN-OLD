package com.example.projeto_softinsa_mobile_app

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_ben
import com.example.projeto_softinsa_mobile_app.Detailed.Detailed_ben
import com.example.projeto_softinsa_mobile_app.ListUser.MainListUsers
import com.example.projeto_softinsa_mobile_app.lvadapador.lvadapatador_ben
import com.google.android.material.navigation.NavigationView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainBeneficio : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    //BASE DE DADOS EXEMPLO:
    private var lv: ListView? = null
    private var ada: lvadapatador_ben? = null
    private var ar_img_list_ben: ArrayList<imagem_ben>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_beneficio)

        lv = findViewById(R.id.lv)

        fetchDataFromServer()

        lv!!.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, Detailed_ben::class.java)
            intent.putExtra("Titulo", ar_img_list_ben!![position].titulo_img_ben)
            intent.putExtra("Tipo", ar_img_list_ben!![position].tipo_img_ben)
            intent.putExtra("Descricao", ar_img_list_ben!![position].descricao_img_ben)
            startActivity(intent)
        }

        /*-----------------------------NAVGATION MENU BAR-----------------------------*/
        var drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val menuBtn = findViewById<ImageButton>(R.id.menu_btn)
        menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        NavigationViewHelper.setupNavigationView(this, drawerLayout, navigationView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchDataFromServer() {
        val url = "https://softinsa-web-app-carreiras01.onrender.com/beneficio/list"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Erro ao obter dados do servidor: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                try {
                    val jsonObject = JSONObject(responseBody)
                    val jsonArray = jsonObject.getJSONArray("data")

                    ar_img_list_ben = ArrayList()

                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val Titulo = item.getString("titulo")
                        val Tipo = item.getString("tipo")
                        val Descricao = item.getString("descricao")

                        val imgItem = imagem_ben()
                        imgItem.titulo_img_ben = Titulo
                        imgItem.tipo_img_ben = Tipo
                        imgItem.descricao_img_ben = Descricao

                        ar_img_list_ben!!.add(imgItem)
                    }

                    runOnUiThread {
                        ada = lvadapatador_ben(applicationContext, ar_img_list_ben!!)
                        lv!!.adapter = ada
                    }

                } catch (e: JSONException) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Erro ao analisar a resposta do servidor: $e", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}