package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_negocio
import com.example.projeto_softinsa_mobile_app.R
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Area_Negocio
import com.example.projeto_softinsa_mobile_app.API.Estado
import com.example.projeto_softinsa_mobile_app.API.Investimento
import com.example.projeto_softinsa_mobile_app.API.Negocio
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.API.Tipo_Projeto
import com.example.projeto_softinsa_mobile_app.API.Vaga
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_estado
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class Detailed_negocio : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var estadoApi: Estado

    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_negocio)
        estadoApi = Estado(this, null)

        val area = findViewById<TextView>(R.id.detailed_Area_Negocio_negocio)
        val email = findViewById<TextView>(R.id.detailed_Email_negocio)
        val telemovel = findViewById<TextView>(R.id.detailed_Telemovel_negocio)
        val orcamento = findViewById<TextView>(R.id.detailed_Orcamento_negocio)
        val desc = findViewById<TextView>(R.id.detailed_Desc_negocio)
        val data = findViewById<TextView>(R.id.detailed_DataCriacao_negocio)
        val nome = findViewById<TextView>(R.id.detailed_NomeUtilizador_negocio)
        val eliminarNegocio = findViewById<ImageButton>(R.id.bt_detailed_Eliminar_negocio)
        val editarNegocio = findViewById<ImageButton>(R.id.bt_detailed_Editar_negocio)
        val bt_alterarEstado = findViewById<Button>(R.id.bt_detailed_alterarEstado_negocio)

        val negocioJson = intent.getStringExtra("negocioJson") // Retrieve the vaga JSON string
        val gson = Gson()
        val selectedNegocio = gson.fromJson(negocioJson, imagem_negocio::class.java)

        email.text = selectedNegocio.email_img_negocio
        telemovel.text = selectedNegocio.telemovel_img_negocio
        orcamento.text = selectedNegocio.orcamento_img_negocio
        desc.text = selectedNegocio.descricao_img_negocio
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(selectedNegocio.dataRegisto_img_negocio)
        val formattedDate = dateFormat.format(date)
        data.text = formattedDate
        val auth = Authorization(this, null)
        userId = auth.getUserId()
        val negocio = Negocio(this, null)


        if (userId != selectedNegocio.userId_img_negocio)
        {
            eliminarNegocio.visibility = View.GONE
            editarNegocio.visibility = View.GONE
        }

        val area_negocio = Area_Negocio(this, null)
        if (selectedNegocio.areaNegocioId_img_negocio != null) {
            area_negocio.getArea_Negocio(selectedNegocio.areaNegocioId_img_negocio!!, object : Area_Negocio.GetArea_NegocioSingleCallback {
                override fun onSuccess(area_negocio: Area_Negocio.Area_Negocio) {
                    area.text = area_negocio.areaNegocioNome
                }

                override fun onFailure(errorMessage: String) {
                    area.text = "Error: $errorMessage"
                }
            })
        } else {
            area.text = "Necessário um departamento"
        }

        val user = Perfil(this, null)
        if (selectedNegocio.userId_img_negocio != null) {
            user.getUser(selectedNegocio.userId_img_negocio!! , object : Perfil.GetUserCallback {
                override fun onSuccess(user: Perfil.User) {
                    Log.d("tag", "${user.primeiroNome}")
                    nome.text = user.primeiroNome + " " + user.ultimoNome
                }

                override fun onFailure(errorMessage: String) {
                    nome.text = "Error: $errorMessage"
                }
            })
        } else {
            nome.text = "O utilizador foi eliminado!"
        }


        editarNegocio.setOnClickListener {
            val intent = Intent(this, AddEditNegocio::class.java)
            intent.putExtra("negocioJson", gson.toJson(selectedNegocio))
            intent.putExtra("isEditing", true)
            startActivity(intent)
        }


        eliminarNegocio.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmação")
            alertDialogBuilder.setMessage("Tem certeza de que deseja eliminar este negócio? Essa ação não pode ser desfeita!")
            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                selectedNegocio.da_negocioId_negocio()?.let { it1 ->
                    negocio.deleteNegocio(it1,  object : Negocio.CreateNegocioCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(this@Detailed_negocio, "Negócio eliminado com sucesso!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(applicationContext, MainOportunidades::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_negocio, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
                dialog.cancel()
            }
            alertDialogBuilder.show()
        }

        bt_alterarEstado.setOnClickListener()
        {
            //TODO: Está a buscar os estados de forma estática
            val estadoNames = arrayOf("Aprovado/a", "Em revisão", "Pendente", "Recusado/a", "Em andamento","Concluído/a", "Suspenso/a", "Cancelado/a", "Atrasado/a", "Em fase de testes", "Em fase de planeamento", "Em espera")

            val builder = android.app.AlertDialog.Builder(this@Detailed_negocio)
            builder.setTitle("Pretende alterar para que estado?")
            builder.setItems(estadoNames) { dialog, which ->
                selectedNegocio.da_negocioId_negocio()?.let { it1 ->
                    negocio.updateNegocioEstado(it1, which + 1, object :
                        Negocio.CreateNegocioCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(
                                this@Detailed_negocio,
                                "Estado alterado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_negocio, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            val dialog = builder.create()
            dialog.show()
        }

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

