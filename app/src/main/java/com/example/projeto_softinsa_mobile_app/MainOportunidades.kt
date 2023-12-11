package com.example.projeto_softinsa_mobile_app


import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

import com.google.android.material.tabs.TabLayout


class MainOportunidades : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var  adicionarOportunidade: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_oportunidades)
        findViewById<SearchView>(R.id.searchView).setIconified(false)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val adapter = Adaptador_ViewPagerOpor(supportFragmentManager)
        adicionarOportunidade = findViewById(R.id.adicionarOportunidade)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)


        adicionarOportunidade.setOnClickListener {
            val options = arrayOf("Negócio", "Parceria", "Investimento", "Projeto")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pretende adicionar ?")
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> {
                            // Redirect to Activity 1
                            val intent1 = Intent(this, AddEditNegocio::class.java)
                            startActivity(intent1)
                        }
                        1 -> {
                            // Redirect to Activity 2
                            val intent2 = Intent(this, AddEditParceria::class.java)
                            startActivity(intent2)
                        }
                        2 -> {
                            // Redirect to Activity 3
                            val intent1 = Intent(this, AddEditInvestimento::class.java)
                            startActivity(intent1)
                        }
                        3 -> {
                            val intent4 = Intent(this, AddEditProjeto::class.java)
                            startActivity(intent4)
                        }
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