package com.example.projeto_softinsa_mobile_app.Detailed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_parceria
import com.example.projeto_softinsa_mobile_app.R
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.*
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.API.Parceria
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class Detailed_parceria : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_parceria)

        val data = findViewById<TextView>(R.id.detailed_DataCriacao_parceria)
        val nome = findViewById<TextView>(R.id.detailed_NomeUtilizador_parceria)
        val nomeParceiro = findViewById<TextView>(R.id.detailed_nome_parceria)
        val email = findViewById<TextView>(R.id.detailed_email_parceria)
        val telemovel = findViewById<TextView>(R.id.detailed_telemovel_parceria)
        val editarParceria = findViewById<ImageButton>(R.id.bt_detailed_Editar_parceria)
        val eliminarParceria = findViewById<ImageButton>(R.id.bt_detailed_Eliminar_parceria)
        val auth = Authorization(this, null)
        userId = auth.getUserId()


        val parceriaJson = intent.getStringExtra("parceriaJson") // Retrieve the vaga JSON string
        val gson = Gson()
        val selectedParceria = gson.fromJson(parceriaJson, imagem_parceria::class.java)

        if (userId != selectedParceria.userId_img_parceria)
        {
            editarParceria.visibility = View.GONE
            eliminarParceria.visibility = View.GONE
        }


        nomeParceiro.text = selectedParceria.nomeParceiro_img_parceria
        email.text = selectedParceria.email_img_parceria
        telemovel.text = selectedParceria.telemovel_img_parceria
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(selectedParceria.dataRegisto_img_parceria)
        val formattedDate = dateFormat.format(date)
        data.text = formattedDate


        val user = Perfil(this, null)
        if (selectedParceria.userId_img_parceria != null) {
            user.getUser(selectedParceria.userId_img_parceria!! , object : Perfil.GetUserCallback {
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

        editarParceria.setOnClickListener {
            val intent = Intent(this, AddEditParceria::class.java)
            intent.putExtra("parceriaJson", gson.toJson(selectedParceria))
            intent.putExtra("isEditing", true)
            startActivity(intent)
        }

        eliminarParceria.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmação")
            alertDialogBuilder.setMessage("Tem certeza de que deseja eliminar esta parceria? Essa ação não pode ser desfeita!")
            val parceria = Parceria(this, null)
            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                selectedParceria.da_parceriaId_parceria()?.let { it1 ->
                    parceria.deleteParceria(it1,  object : Parceria.CreateParceriaCallback {
                        override fun onSuccess(sucesso: String) {
                            Toast.makeText(this@Detailed_parceria, "Parceria eliminada com sucesso!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(applicationContext, MainOportunidades::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(errorMessage: String) {
                            Log.d("tag", "PASSEI AQUI")
                            Toast.makeText(this@Detailed_parceria, errorMessage, Toast.LENGTH_SHORT).show()
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
