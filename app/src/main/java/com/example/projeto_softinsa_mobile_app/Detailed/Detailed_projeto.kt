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
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_projeto
import com.example.projeto_softinsa_mobile_app.R
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.API.Estado
import com.example.projeto_softinsa_mobile_app.API.Negocio
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Projeto
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.API.Tipo_Projeto
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class Detailed_projeto : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var estadoApi: Estado

    var userId = 0
    private val prioridadeValues = arrayOf(
        AddEditProjeto.PrioridadeItem("Urgente", "1"),
        AddEditProjeto.PrioridadeItem("Normal", "2"),
        AddEditProjeto.PrioridadeItem("Pouco Urgente", "3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_projeto)

        val nomeProjeto = findViewById<TextView>(R.id.detailed_Nome_projeto)
        val tipo = findViewById<TextView>(R.id.detailed_Tipo_projeto)
        val orcamento = findViewById<TextView>(R.id.detailed_Orcamento_projeto)
        val desc = findViewById<TextView>(R.id.detailed_Desc_projeto)
        val data = findViewById<TextView>(R.id.detailed_DataCriacao_projeto)
        val nome = findViewById<TextView>(R.id.detailed_NomeUtilizador_projeto)
        val prioridade = findViewById<TextView>(R.id.detailed_Prioridade_projeto)
        val editarProjeto = findViewById<ImageButton>(R.id.bt_detailed_Editar_projeto)
        val eliminarProjeto = findViewById<ImageButton>(R.id.bt_detailed_Eliminar_projeto)
        val bt_alterarEstado = findViewById<Button>(R.id.bt_detailed_alterarEstado_projeto)
        val auth = Authorization(this, null)
        userId = auth.getUserId()

        val projetoJson = intent.getStringExtra("projetoJson") // Retrieve the vaga JSON string
        val gson = Gson()
        val selectedProjeto = gson.fromJson(projetoJson, imagem_projeto::class.java)

        nomeProjeto.text = selectedProjeto.projetoNome_img_projeto
        orcamento.text = selectedProjeto.orcamento_img_projeto
        desc.text = selectedProjeto.descricao_img_projeto
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(selectedProjeto.dataRegisto_img_projeto)
        val formattedDate = dateFormat.format(date)
        data.text = formattedDate
        val projeto = Projeto(this, null)
        Log.d("USER ID", userId.toString())
        Log.d("SELECTED USER ID", selectedProjeto.userId_img_projeto.toString())
        if (userId != selectedProjeto.userId_img_projeto)
        {
            editarProjeto.visibility = View.GONE
            eliminarProjeto.visibility = View.GONE
        }

        val selectedPrioridade = selectedProjeto.prioridade_img_projeto
        val selectedPrioridadeItem = prioridadeValues.find { it.numericValue == selectedPrioridade }
        val prioridadeText = selectedPrioridadeItem?.displayText ?: "N/A"
        prioridade.text = prioridadeText

        val tipo_projeto = Tipo_Projeto(this, null)
        if (selectedProjeto.tipoProjetoId_img_projeto != null) {
            tipo_projeto.getTipo_Projeto(selectedProjeto.tipoProjetoId_img_projeto!!, object : Tipo_Projeto.GetTipo_ProjetoSingleCallback {
                override fun onSuccess(tipo_projeto: Tipo_Projeto.Tipo_Projeto) {
                    tipo.text = tipo_projeto.tipoProjetoNome
                }

                override fun onFailure(errorMessage: String) {
                    tipo.text = "Error: $errorMessage"
                }
            })
        } else {
            tipo.text = "Necessário um tipo de projeto"
        }

        val user = Perfil(this, null)
        if (selectedProjeto.userId_img_projeto != null) {
            user.getUser(selectedProjeto.userId_img_projeto!! , object : Perfil.GetUserCallback {
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


        editarProjeto.setOnClickListener {
            val intent = Intent(this, AddEditProjeto::class.java)
            intent.putExtra("projetoJson", gson.toJson(selectedProjeto))
            intent.putExtra("isEditing", true)
            startActivity(intent)
        }


        eliminarProjeto.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmação")
            alertDialogBuilder.setMessage("Tem certeza de que deseja eliminar este negócio? Essa ação não pode ser desfeita!")

            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                selectedProjeto.da_projetoId_projeto()?.let { it1 ->
                    projeto.deleteProjeto(it1,  object : Projeto.CreateProjetoCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(this@Detailed_projeto, "Negócio eliminado com sucesso!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(applicationContext, MainOportunidades::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_projeto, errorMessage, Toast.LENGTH_SHORT).show()
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

            val builder = android.app.AlertDialog.Builder(this@Detailed_projeto)
            builder.setTitle("Pretende alterar para que estado?")
            builder.setItems(estadoNames) { dialog, which ->
                selectedProjeto.da_projetoId_projeto()?.let { it1 ->
                    projeto.updateProjetoEstado(it1, which + 1, object :
                        Projeto.CreateProjetoCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(
                                this@Detailed_projeto,
                                "Estado alterado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_projeto, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            val dialog = builder.create()
            dialog.show()
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
