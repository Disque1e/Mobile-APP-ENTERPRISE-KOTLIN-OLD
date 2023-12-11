package com.example.projeto_softinsa_mobile_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.google.android.material.navigation.NavigationView
import com.example.projeto_softinsa_mobile_app.API.Perfil

class PerfilActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var perfil: Perfil
    var userId = 0

    fun exibirDialogAlterarPass(perfil: Perfil) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alterar Password")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        builder.setView(input)
        input.hint = "Insira a sua nova password"
        builder.setPositiveButton("OK") { dialog, _ ->
            val novaPass = input.text.toString()

            perfil.changePassword(userId, novaPass,  object : Perfil.GetUpdateCallback {
                override fun onSuccess(sucesso: Boolean) {
                    Toast.makeText(this@PerfilActivity, "Password atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorMessage: String) {
                    Toast.makeText(this@PerfilActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        val authorization = Authorization(this, null)
        userId = authorization.getUserId()
        //Necessário buscar os dados do user
        val Email = findViewById<EditText>(R.id.perfil_Email)
        val Telefone = findViewById<EditText>(R.id.perfil_Telefone)
        val Localidade = findViewById<EditText>(R.id.perfil_Local)
        val Cargo = findViewById<EditText>(R.id.perfil_Cargo)
        var CargoCargo =""
        val Nome = findViewById<TextView>(R.id.perfil_Nome_Utilizador)
        val PrimeiroNome = findViewById<EditText>(R.id.perfil_PrimeiroNome)
        val UltimoNome = findViewById<EditText>(R.id.perfil_UltimoNome)
        perfil = Perfil(this, getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit())

        perfil.getUser(userId, object : Perfil.GetUserCallback {
            override fun onSuccess(user: Perfil.User) {
                Email.text = TextUtils.isEmpty(user.email).let { if (it) null else Editable.Factory.getInstance().newEditable(user.email) }
                Telefone.text = TextUtils.isEmpty(user.telemovel).let { if (it) null else Editable.Factory.getInstance().newEditable(user.telemovel) }
                if (TextUtils.isEmpty(user.morada) || user.morada == "null") {
                    Localidade.hint = "Insira uma morada"
                } else {
                    Localidade.text = Editable.Factory.getInstance().newEditable(user.morada)
                }
                Cargo.text = TextUtils.isEmpty(user.cargo?.cargoNome).let { if (it) null else Editable.Factory.getInstance().newEditable(user.cargo?.cargoNome) }
                CargoCargo = Cargo.text.toString()
                PrimeiroNome.text = TextUtils.isEmpty(user.primeiroNome).let { if (it) null else Editable.Factory.getInstance().newEditable(user.primeiroNome) }
                UltimoNome.text = TextUtils.isEmpty(user.ultimoNome).let { if (it) null else Editable.Factory.getInstance().newEditable(user.ultimoNome) }
                Nome.text = TextUtils.isEmpty(user.primeiroNome).let { if (it) null else Editable.Factory.getInstance().newEditable("${user.primeiroNome} ${user.ultimoNome}") }
            }

            override fun onFailure(errorMessage: String) {
                Toast.makeText(this@PerfilActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        val bt_editar = findViewById<Button>(R.id.bt_perfil_Editar)
        val bt_guardar = findViewById<Button>(R.id.bt_perfil_Guardar)
        val bt_eliminiar = findViewById<Button>(R.id.bt_perfil_Eliminar)

        bt_editar.setOnClickListener {

            PrimeiroNome.isEnabled = true
            UltimoNome.isEnabled = true
            Email.isEnabled = true
            Telefone.isEnabled = true
            Localidade.isEnabled = true
            Cargo.isEnabled = true // Passar a ser para editar a password!!!!
            Cargo.isFocusable = false
            UltimoNome.visibility = View.VISIBLE
            PrimeiroNome.visibility = View.VISIBLE
            Cargo.text = TextUtils.isEmpty("Alterar password").let { if (it) null else Editable.Factory.getInstance().newEditable("Alterar password") }
            val layoutParams = Cargo.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.topMargin = 50
            Email.layoutParams = layoutParams
            bt_editar.visibility = View.GONE
            bt_guardar.visibility = View.VISIBLE
            bt_eliminiar.visibility = View.VISIBLE
        }

        bt_guardar.setOnClickListener{
            bt_editar.visibility = View.VISIBLE
            bt_guardar.visibility = View.GONE
            bt_eliminiar.visibility = View.GONE
            Email.isEnabled = false
            Telefone.isEnabled = false
            Localidade.isEnabled = false
            Cargo.text = TextUtils.isEmpty(CargoCargo).let { if (it) null else Editable.Factory.getInstance().newEditable(CargoCargo) }
            Cargo.isEnabled = false // Volta a ser para o cargo
            UltimoNome.visibility = View.GONE
            PrimeiroNome.visibility = View.GONE
            perfil.updateUserPerfil(userId, Email.text.toString(), Telefone.text.toString(), Localidade.text.toString(), PrimeiroNome.text.toString(), UltimoNome.text.toString(),  object : Perfil.GetUpdateCallback {
                override fun onSuccess(sucesso: Boolean) {
                    Toast.makeText(this@PerfilActivity, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorMessage: String) {
                    Toast.makeText(this@PerfilActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }

        bt_eliminiar.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Confirmação")
            alertDialogBuilder.setMessage("Tem certeza de que deseja eliminar o seu perfil? Essa ação não pode ser desfeita!")
            alertDialogBuilder.setPositiveButton("Sim") { dialog, _ ->
                perfil.deleteUser(userId,  object : Perfil.GetUpdateCallback {
                    override fun onSuccess(sucesso: Boolean) {
                        Toast.makeText(this@PerfilActivity, "Perfil eliminado com sucesso!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        //nao deixar voltar atras :3
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(errorMessage: String) {
                        Toast.makeText(this@PerfilActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                })

                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("Não") { dialog, _ ->
                dialog.cancel()
            }
            alertDialogBuilder.show()
        }

        Cargo.setOnClickListener {
            exibirDialogAlterarPass(perfil)
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