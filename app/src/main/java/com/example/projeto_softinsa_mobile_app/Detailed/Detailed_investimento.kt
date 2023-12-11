package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_investimento
import com.example.projeto_softinsa_mobile_app.R
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.API.Estado
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Investimento
import com.example.projeto_softinsa_mobile_app.API.Negocio
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class Detailed_investimento : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var estadoApi: Estado
    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_investimento)
        estadoApi = Estado(this, null)

        val orcamento = findViewById<TextView>(R.id.detailed_Montante_investimento)
        val desc = findViewById<TextView>(R.id.detailed_Desc_Investimento)
        val data = findViewById<TextView>(R.id.detailed_DataCriacao_investimento)
        val nome = findViewById<TextView>(R.id.detailed_NomeUtilizador_investimento)

        val investimentoJson = intent.getStringExtra("investimentoJson") // Retrieve the vaga JSON string
        val gson = Gson()
        val selectedInvestimento = gson.fromJson(investimentoJson, imagem_investimento::class.java)
        val editarInvestimento = findViewById<ImageButton>(R.id.bt_detailed_Editar_investimento)
        val eliminarInvestimento = findViewById<ImageButton>(R.id.bt_detailed_Eliminar_investimento)
        val bt_alterarEstado = findViewById<Button>(R.id.bt_detailed_alterarEstado_investimento)

        val auth = Authorization(this, null)
        userId = auth.getUserId()
        val investimento = Investimento(this, null)

        if (userId != selectedInvestimento.userId_img_investimento)
        {
            eliminarInvestimento.visibility = GONE
            editarInvestimento.visibility = GONE
        }


        orcamento.text = selectedInvestimento.montante_img_investimento
        desc.text = selectedInvestimento.descricao_img_investimento
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(selectedInvestimento.dataRegisto_img_investimento)
        val formattedDate = dateFormat.format(date)
        data.text = formattedDate

        val user = Perfil(this, null)
        if (selectedInvestimento.userId_img_investimento != null) {
            user.getUser(selectedInvestimento.userId_img_investimento!! , object : Perfil.GetUserCallback {
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



        editarInvestimento.setOnClickListener {
            val intent = Intent(this, AddEditInvestimento::class.java)
            intent.putExtra("investimentoJson", gson.toJson(selectedInvestimento))
            intent.putExtra("isEditing", true)
            startActivity(intent)
        }


        eliminarInvestimento.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmação")
            alertDialogBuilder.setMessage("Tem certeza de que deseja eliminar este negócio? Essa ação não pode ser desfeita!")
            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                selectedInvestimento.da_investimentoId_investimento()?.let { it1 ->
                    investimento.deleteInvestimento(it1,  object : Investimento.CreateInvestimentoCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(this@Detailed_investimento, "Negócio eliminado com sucesso!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(applicationContext, MainOportunidades::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_investimento, errorMessage, Toast.LENGTH_SHORT).show()
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

            val builder = android.app.AlertDialog.Builder(this@Detailed_investimento)
            builder.setTitle("Pretende alterar para que estado?")
            builder.setItems(estadoNames) { dialog, which ->
                selectedInvestimento.da_investimentoId_investimento()?.let { it1 ->
                    investimento.updateInvestimentoEstado(it1, which + 1, object :
                        Investimento.CreateInvestimentoCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(
                                this@Detailed_investimento,
                                "Estado alterado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onFailure(errorMessage: String) {
                            Toast.makeText(this@Detailed_investimento, errorMessage, Toast.LENGTH_SHORT).show()
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
