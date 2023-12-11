package com.example.projeto_softinsa_mobile_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_candidatura
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_candidatura
import com.example.projeto_softinsa_mobile_app.Detailed.Detailed_candidatura
import com.example.projeto_softinsa_mobile_app.API.Candidatura
import com.google.gson.Gson

class MainCandidatura : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var filteredCandidaturas: List<imagem_candidatura>
    //BASE DE DADOS EXEMPLO:
    private var lv_candidaturas: ListView? = null
    private var ada: Lvadapador_candidatura? = null
    private lateinit var candidaturaApi: Candidatura

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidaturas)

        findViewById<SearchView>(R.id.searchView).setIconified(false)
        candidaturaApi = Candidatura(this, getPreferences(Context.MODE_PRIVATE).edit())
        lv_candidaturas = findViewById(R.id.lv_candidaturas) as ListView

        val ID = intent.getIntExtra("ID", 0)

        candidaturaApi.listCandidaturas(object : Candidatura.GetCandidaturaCallback {
            override fun onSuccess(candidaturas: List<imagem_candidatura>) {
                filteredCandidaturas = candidaturas.filter { candidatura ->
                    candidatura.da_vagaId_candidatura() == ID
                }
                if (filteredCandidaturas.isNotEmpty()) {
                    ada = Lvadapador_candidatura(this@MainCandidatura, filteredCandidaturas)
                    lv_candidaturas!!.adapter = ada
                } else {
                    Toast.makeText(this@MainCandidatura, "Não existem candidatos à vaga", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
            }
        })

        lv_candidaturas!!.setOnItemClickListener { _, _, position, _ ->
            val selectedCandidatura = ada?.getItem(position) as imagem_candidatura
            val gson = Gson()
            val candidaturaJson = gson.toJson(selectedCandidatura)
            val intent = Intent(this, Detailed_candidatura::class.java)
            intent.putExtra("candidaturaJson", candidaturaJson)
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

}