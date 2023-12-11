import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.ListUser.MainListUsers
import com.example.projeto_softinsa_mobile_app.MainPage
import com.example.projeto_softinsa_mobile_app.MainOportunidades
import com.example.projeto_softinsa_mobile_app.MainIdeia
import com.example.projeto_softinsa_mobile_app.MainOferta_Vaga
import com.example.projeto_softinsa_mobile_app.MainBeneficio
import com.example.projeto_softinsa_mobile_app.PerfilActivity
import com.example.projeto_softinsa_mobile_app.MainCalendario
import com.example.projeto_softinsa_mobile_app.MainActivity
import com.example.projeto_softinsa_mobile_app.MainReporting
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.google.android.material.navigation.NavigationView

object NavigationViewHelper {
    fun setupNavigationView(
        activity: AppCompatActivity,
        drawerLayout: DrawerLayout,
        navigationView: NavigationView
    ) {
        val toggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var cargoId = 0


        val user = Perfil(activity, null)
        cargoId = user.getStoredCargoId()

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent1 = Intent(activity, MainPage::class.java)
                    intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity.startActivity(intent1)
                }
                R.id.nav_oportunities -> {
                    val intent2 = Intent(activity, MainOportunidades::class.java)
                    activity.startActivity(intent2)
                }
                R.id.nav_ideas -> {
                    val intent3 = Intent(activity, MainIdeia::class.java)
                    activity.startActivity(intent3)
                }
                R.id.nav_job_offers -> {
                    val intent4 = Intent(activity, MainOferta_Vaga::class.java)
                    activity.startActivity(intent4)
                }
                R.id.nav_benefits -> {
                    val intent5 = Intent(activity, MainBeneficio::class.java)
                    activity.startActivity(intent5)
                }
                R.id.nav_list_users -> {
                    if(cargoId!=1)
                    {
                        Toast.makeText(activity, "Não tens permissões para aceder à lista de utilizadores.", Toast.LENGTH_SHORT).show()
                    }else{
                        val intent7 = Intent(activity, MainListUsers::class.java)
                        activity.startActivity(intent7)
                    }
                }

                R.id.reporting -> {
                    if(cargoId!=1)
                    {
                        Toast.makeText(activity, "Não tens permissões para aceder ao reporting.", Toast.LENGTH_SHORT).show()
                    }else
                    {
                        val intent = Intent(activity, MainReporting::class.java)
                        activity.startActivity(intent)
                    }

                }

                R.id.nav_profile -> {
                    if (cargoId <= 2) {
                        val intent8 = Intent(activity, PerfilActivity::class.java)
                        activity.startActivity(intent8)
                    } else {
                    }
                }

                R.id.nav_calendario -> {
                    val intent9 = Intent(activity, MainCalendario::class.java)
                    activity.startActivity(intent9)
                }
                R.id.nav_logout -> {
                    val intent6 = Intent(activity, MainActivity::class.java)
                    //nao deixar voltar atras :3
                    intent6.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    Toast.makeText(activity, "Login Out", Toast.LENGTH_SHORT).show()
                    activity.startActivity(intent6)
                    activity.finish() // Encerra a atividade atual
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val navProfileItem = navigationView.menu.findItem(R.id.nav_profile)
        if (cargoId > 2) {
            navProfileItem.isVisible = false
        }
    }
}
