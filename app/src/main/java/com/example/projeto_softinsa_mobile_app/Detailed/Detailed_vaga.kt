package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.R
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_vaga
import com.example.projeto_softinsa_mobile_app.API.Departamento
import com.example.projeto_softinsa_mobile_app.API.Filial
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class Detailed_vaga : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    var cargoId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_vaga)

        val titulo = findViewById<TextView>(R.id.detailed_Vaga_Titulo)
        val area = findViewById<TextView>(R.id.detailed_Vagas_Area)
        val habilitacao = findViewById<TextView>(R.id.detailed_Vagas_Habilitacao)
        val local = findViewById<TextView>(R.id.detailed_Vagas_Localizacao)
        val dataCriacao = findViewById<TextView>(R.id.detailed_Vagas_Data_Criacao)
        val descricao = findViewById<TextView>(R.id.detailed_Vagas_Desc)
        val responsavel = findViewById<TextView>(R.id.detailed_NomeUtilizador)
        val verCandidaturaBtn = findViewById<Button>(R.id.bt_detailed_Vagas_Candidaturas)

        val user = Perfil(this, null)
        cargoId = user.getStoredCargoId()


        if (cargoId > 2) {
            findViewById<ImageButton>(R.id.bt_detailed_Eliminar_vaga).visibility = GONE
            findViewById<ImageButton>(R.id.bt_detalied_Editar_Vaga).visibility = GONE
            verCandidaturaBtn.visibility = GONE

        }

        val vagaJson = intent.getStringExtra("vagaJson") // Retrieve the vaga JSON string
        val gson = Gson()
        val selectedVaga = gson.fromJson(vagaJson, imagem_vaga::class.java)

        titulo.text = selectedVaga.titulo_img_vaga

        habilitacao.text = selectedVaga.habilitacoesMin_img_vaga
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(selectedVaga.dataRegisto_img_vaga)
        val formattedDate = dateFormat.format(date)
        dataCriacao.text = formattedDate
        descricao.text = selectedVaga.descricao_img_vaga

        val filial = Filial(this, null)
        if (selectedVaga.filialId_img_vaga != null) {
            filial.getFilial(object : Filial.GetFilialCallback {
                override fun onSuccess(filial: Filial.Filial) {
                    Log.d("tag", "${filial.filialNome}")
                    local.text = filial.filialNome
                }

                override fun onFailure(errorMessage: String) {
                    local.text = "Error: $errorMessage"
                }
            }, selectedVaga.filialId_img_vaga!!)
        } else {
            local.text = "Necessário um filialId"
        }

        val departamento = Departamento(this, null)
        if (selectedVaga.departamentoId_img_vaga != null) {
            departamento.getDepartamento(object : Departamento.GetDepartamentoCallback {
                override fun onSuccess(departamento: Departamento.Departamento) {
                    Log.d("tag", "${departamento.departamentoNome}")
                    area.text = departamento.departamentoNome
                }

                override fun onFailure(errorMessage: String) {
                    local.text = "Error: $errorMessage"
                }
            }, selectedVaga.departamentoId_img_vaga!!)
        } else {
            local.text = "Necessário um departamento"
        }


        if (selectedVaga.userId_img_vaga != null) {
            user.getUser(selectedVaga.userId_img_vaga!! , object : Perfil.GetUserCallback {
                override fun onSuccess(user: Perfil.User) {
                    Log.d("tag", "${user.primeiroNome}")
                    responsavel.append(user.primeiroNome + " " + user.ultimoNome)
                }

                override fun onFailure(errorMessage: String) {
                    responsavel.text = "Error: $errorMessage"
                }
            })
        } else {
            responsavel.text = "O utilizador foi eliminado!"
        }



        verCandidaturaBtn.setOnClickListener{
            val intent = Intent(applicationContext, MainCandidatura::class.java) // Colocar para oportunidades
            intent.putExtra("ID", selectedVaga.vagaId_img_vaga)
            startActivity(intent)

        }

        val candidatarBtn = findViewById<Button>(R.id.bt_detailed_Vagas_Candidatar)
        candidatarBtn.setOnClickListener{
            val intent = Intent(applicationContext, AddCandidatura::class.java) // Colocar para oportunidades
            intent.putExtra("ID", selectedVaga.vagaId_img_vaga)
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
