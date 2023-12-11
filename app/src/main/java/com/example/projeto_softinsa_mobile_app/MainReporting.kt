package com.example.projeto_softinsa_mobile_app

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import co.yml.charts.axis.AxisData
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_investimento
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_negocio
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_parceria
import com.example.projeto_softinsa_mobile_app.API.Investimento
import com.example.projeto_softinsa_mobile_app.API.Negocio
import com.example.projeto_softinsa_mobile_app.API.Parceria
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.API.Projeto
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_lista_user
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_projeto
import com.google.android.material.navigation.NavigationView
import co.yml.charts.ui.barchart.models.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.projeto_softinsa_mobile_app.API.Candidatura
import com.example.projeto_softinsa_mobile_app.API.Vaga
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_candidatura
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_vaga
import java.util.Calendar

var cor = Color(0xFF3399FF)
val cargoNames = mapOf(
    1 to "Administrador",
    2 to "Gestor",
    3 to "Colaborador",
    4 to "Candidato",
    5 to "Visitante"
)

val cargoColors = listOf(
    "#8884d8", // Administrador
    "#82ca9d", // Gestor
    "#ffbb28", // Colaborador
    "#ff8042", // Candidato
    "#0088FE"  // Visitante
)


class MainReporting : AppCompatActivity(), DatePickerDialog.OnDateSetListener{

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var projetoApi: Projeto
    private lateinit var negocioApi: Negocio
    private lateinit var investimentoApi: Investimento
    private lateinit var parceriaApi: Parceria
    private lateinit var userApi: Perfil
    private lateinit var candidaturaApi: Candidatura
    private lateinit var vagaApi: Vaga

    private lateinit var negociosLista: List<imagem_negocio>
    private lateinit var projetosLista:  List<imagem_projeto>
    private lateinit var parceriasLista: List<imagem_parceria>
    private lateinit var investimentosLista:  List<imagem_investimento>
    private lateinit var usersLista:  List<imagem_lista_user>
    private lateinit var vagasLista: List<imagem_vaga>
    private lateinit var candidaturasLista: List<imagem_candidatura>

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0

    private var isInvestimentosLoaded = false
    private var isNegociosLoaded = false
    private var isProjetosLoaded = false
    private var isParceriasLoaded = false

    private fun tryShowBarchart() {
        if (isInvestimentosLoaded && isNegociosLoaded) {
            val composeView = findViewById<ComposeView>(R.id.composeView)
            composeView.setContent {
                BarchartWithSolidBars(investimentosLista, negociosLista, projetosLista, parceriasLista)
            }

            val composeView2 = findViewById<ComposeView>(R.id.composeView2)
            composeView2.setContent {
                BarchartWithSolidBarsVagas(vagasLista, candidaturasLista)
            }

            val composeView3 = findViewById<ComposeView>(R.id.composeView3)
            composeView3.setContent {
                PieChartUsersCargos(usersLista)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_reporting)
        projetoApi = Projeto(this, null)
        negocioApi = Negocio(this, null)
        investimentoApi = Investimento(this, null)
        parceriaApi = Parceria(this, null)
        userApi = Perfil(this, null)
        candidaturaApi  = Candidatura(this, null)
        vagaApi = Vaga(this, null)

       val picker1 = findViewById<Button>(R.id.btnPicker)
        val picker2 = findViewById<Button>(R.id.btnPicker2)

        picker1.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@MainReporting, this@MainReporting, year, month,day)
            datePickerDialog.show()
        }


        negocioApi.listNegocios(object : Negocio.GetNegocioCallback {
            override fun onSuccess(negocios: List<imagem_negocio>) {
                negociosLista = negocios
                isNegociosLoaded = true
                tryShowBarchart()
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

        parceriaApi.listParcerias(object : Parceria.GetParceriaCallback {
            override fun onSuccess(parcerias: List<imagem_parceria>) {
                    parceriasLista = parcerias
                isParceriasLoaded = true
                tryShowBarchart()
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

        investimentoApi.listInvestimentos(object : Investimento.GetInvestimentoCallback {
            override fun onSuccess(investimentos: List<imagem_investimento>) {
              investimentosLista = investimentos
                isInvestimentosLoaded = true
                tryShowBarchart()

            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

        projetoApi.listProjetos(object : Projeto.GetProjetoCallback {
            override fun onSuccess(projetos: List<imagem_projeto>) {
               projetosLista = projetos
                isProjetosLoaded = true
                tryShowBarchart()
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

        userApi.listUser(object : Perfil.GetUserListaCallback {
            override fun onSuccess(users: List<imagem_lista_user>) {
                usersLista = users
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

        candidaturaApi.listCandidaturas(object : Candidatura.GetCandidaturaCallback {
            override fun onSuccess(candidaturas: List<imagem_candidatura>) {
                candidaturasLista = candidaturas
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

        vagaApi.listVagas(object : Vaga.GetVagaCallback {
            override fun onSuccess(vagas: List<imagem_vaga>) {
                vagasLista = vagas
            }

            override fun onFailure(errorMessage: String) {
                Log.d("tag", errorMessage)
                // Handle the failure case
            }
        })

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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
    }

}



fun transformInvestimentosToBarData(investimentos: List<imagem_investimento>): BarData {

        // Extract the necessary data from the Investimentos object
        val label = investimentos.count().toFloat()// Replace with the actual method to get the label // Replace with the actual method to get the value
        val point = Point(x = 0f, y = label)
        // Create a BarData object with the extracted data
        return BarData(label = "Investimentos", point = point, color = cor)


}fun transformNegocioToBarData(negocios: List<imagem_negocio>): BarData {

        // Extract the necessary data from the Investimentos object
        val label = negocios.count().toFloat()// Replace with the actual method to get the label // Replace with the actual method to get the value
        val point = Point(x = 1f, y = label)
        // Create a BarData object with the extracted data
        return BarData(label = "Negócios", point = point, color = cor)

}

fun transformProjetoToBarData(projetos: List<imagem_projeto>): BarData {

        // Extract the necessary data from the Investimentos object
        val label = projetos.count().toFloat()// Replace with the actual method to get the label // Replace with the actual method to get the value
        val point = Point(x = 2f, y = label)
        // Create a BarData object with the extracted data
       return BarData(label = "Projetos", point = point, color = cor)

}

fun transformParceriaToBarData(parcerias: List<imagem_parceria>): BarData {

        // Extract the necessary data from the Investimentos object
        val label = parcerias.count().toFloat()// Replace with the actual method to get the label // Replace with the actual method to get the value
        val point = Point(x = 3f, y = label)
        // Create a BarData object with the extracted data
        return BarData(label = "Parcerias", point = point , color = cor)

}

fun transformCandidaturaToBarData(candidaturas: List<imagem_candidatura>): BarData {

    // Extract the necessary data from the Investimentos object
    val label = candidaturas.count().toFloat()// Replace with the actual method to get the label // Replace with the actual method to get the value
    val point = Point(x = 0f, y = label)
    // Create a BarData object with the extracted data
    return BarData(label = "Candidaturas", point = point , color = cor)

}

fun transformVagaToBarData(vagas: List<imagem_vaga>): BarData {

    // Extract the necessary data from the Investimentos object
    val label = vagas.count().toFloat()// Replace with the actual method to get the label // Replace with the actual method to get the value
    val point = Point(x = 1f, y = label)
    // Create a BarData object with the extracted data
    return BarData(label = "Vagas", point = point , color = cor)

}


fun transformUserToPieData(users: List<imagem_lista_user>): PieChartData {
    val cargoCounts = mutableMapOf<Int, Int>() // Map to store cargo ID and its count

    // Count the users for each cargo
    for (user in users) {
        val cargoId = user.da_Cargo_lista_user()
        if (cargoId != null) {
            cargoCounts[cargoId] = cargoCounts.getOrDefault(cargoId, 0) + 1
        }
    }

    // Create PieChartData using cargoCounts and cargoNames
    val pieChartSlices = cargoCounts.mapNotNull { (cargoId, count) ->
        val cargoName = cargoNames[cargoId]
        val cargoColor = cargoColors.getOrNull(cargoId - 1) // Adjust index since cargoIds start from 1
        if (cargoName != null && cargoColor != null) {
            PieChartData.Slice(color = Color(android.graphics.Color.parseColor(cargoColor)), label = cargoName, value = count.toFloat())
        } else {
            null
        }
    }

    return PieChartData(slices = pieChartSlices, plotType = PlotType.Pie)
}

fun combineBarData(
    investimentos: List<imagem_investimento>,
    negocios: List<imagem_negocio>,
    projetos: List<imagem_projeto>,
    parcerias: List<imagem_parceria>
): List<BarData> {
    val combinedData = mutableListOf<BarData>()

    val investimentosData = transformInvestimentosToBarData(investimentos)
    val negociosData = transformNegocioToBarData(negocios)
    val projetosData = transformProjetoToBarData(projetos)
    val parceriasData = transformParceriaToBarData(parcerias)

    combinedData.add(investimentosData)
    combinedData.add(negociosData)
    combinedData.add(projetosData)
    combinedData.add(parceriasData)

    return combinedData
}

fun combineBarDataVagasCandidaturas(
    candidaturas: List<imagem_candidatura>,
    vagas: List<imagem_vaga>,
): List<BarData> {
    val combinedData = mutableListOf<BarData>()

    val candidaturasData = transformCandidaturaToBarData(candidaturas)
    val vagasData = transformVagaToBarData(vagas)

    combinedData.add(candidaturasData)
    combinedData.add(vagasData)

    return combinedData
}

@Composable
private fun BarchartWithSolidBars(investimentos: List<imagem_investimento>, negocios: List<imagem_negocio>, projetos: List<imagem_projeto>, parcerias: List<imagem_parceria>) {
    val maxRange = maxOf(
        investimentos.count(),
        negocios.count(),
        projetos.count(),
        parcerias.count()
    ) + 1
    val barData = combineBarData(investimentos,negocios,projetos,parcerias)
    val yStepSize = maxRange

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(30.dp)
        .startDrawPadding(40.dp)
        .axisLabelFontSize(12.sp)
        .labelData { index -> barData[index].label }
        .build()
    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(10.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()
    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = 55.dp,
            barWidth = 30.dp,
        ),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 10.dp,
    )

    BarChart(modifier = Modifier, barChartData = barChartData)
}

@Composable
private fun BarchartWithSolidBarsVagas(vagas: List<imagem_vaga>, candidaturas: List<imagem_candidatura>) {
    val maxRange = maxOf(
        vagas.count(),
        candidaturas.count(),
    ) + 1
    val barData = combineBarDataVagasCandidaturas(candidaturas, vagas)
    val yStepSize = maxRange

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(30.dp)
        .startDrawPadding(100.dp)
        .axisLabelFontSize(12.sp)
        .labelData { index -> barData[index].label }
        .build()
    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(10.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()
    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = 100.dp,
            barWidth = 60.dp,
        ),
        showYAxis = true,
        showXAxis = true,
        horizontalExtraSpace = 10.dp,
    )

    BarChart(modifier = Modifier, barChartData = barChartData)
}

@Composable
private fun PieChartUsersCargos(users: List<imagem_lista_user>)
{
    val pieChartData = transformUserToPieData(users)
    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        animationDuration = 1500,
        chartPadding = 60,
        labelVisible = false,
        showSliceLabels = false,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
    )

    Column(modifier = Modifier.fillMaxSize()) {
        PieChart(
            modifier = Modifier.weight(1f),
            pieChartData = pieChartData,
            pieChartConfig = pieChartConfig
        )

        Spacer(modifier = Modifier.height(16.dp)) // Adjust the height as needed

        Row(
            modifier = Modifier
                .padding(start = 36.dp) // Add padding to the start (left side)
                .align(Alignment.CenterHorizontally) // Center horizontally
        ) {
            Legends(
                legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData, 2)
            )
        }
    }
}



