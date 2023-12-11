package com.example.projeto_softinsa_mobile_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_investimento
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_negocio
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_parceria
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_projeto
import com.example.projeto_softinsa_mobile_app.Detailed.*
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_investimento
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_negocio
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_parceria
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_projeto
import com.example.projeto_softinsa_mobile_app.API.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class oportunidadeFragment : Fragment() {
    private var option: Int = 0
    private var lv_opor: ListView? = null
    private var ada_projetos: Lvadapador_projeto? = null
    private var ada_negocios: Lvadapador_negocio? = null
    private var ada_investimentos: Lvadapador_investimento? = null
    private var ada_parcerias: Lvadapador_parceria? = null
    private lateinit var projetoApi: Projeto
    private lateinit var negocioApi: Negocio
    private lateinit var investimentoApi: Investimento
    private lateinit var parceriaApi: Parceria
    private var loadingLayout: View? = null
    private lateinit var fragmentContext: Context
    var cargoId = 0
    var userId = 0
    companion object {
        private const val ARG_OPTION = "option"

        fun newInstance(option: Int): oportunidadeFragment {
            val fragment = oportunidadeFragment()
            val args = Bundle()
            args.putInt(ARG_OPTION, option)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        option = arguments?.getInt(ARG_OPTION) ?: 0
        projetoApi = Projeto(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE).edit())
        negocioApi = Negocio(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE).edit())
        investimentoApi = Investimento(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE).edit())
        parceriaApi = Parceria(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE).edit())
        val user = Perfil(fragmentContext, null)
        cargoId = user.getStoredCargoId()
        val auth = Authorization(fragmentContext, null)
        userId = auth.getUserId()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar o layout do fragmento
        return inflater.inflate(R.layout.fragment_oportunidade, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lv_opor = view.findViewById(R.id.lv_oportunidades) as ListView
        val loadingLayout = view.findViewById<LinearLayout>(R.id.loadingLayout)

        when (option) {
            0 -> {
                negocioApi.listNegocios(object : Negocio.GetNegocioCallback {
                    override fun onSuccess(negocios: List<imagem_negocio>) {
                        loadingLayout.visibility = View.GONE
                        if (cargoId <= 2) {
                            ada_negocios = Lvadapador_negocio(requireContext(), negocios)
                            lv_opor!!.adapter = ada_negocios
                            Log.d("tag", negocios.toString())
                        } else {
                            val filteredNegocios = negocios.filter { it.userId_img_negocio == userId }
                            ada_negocios = Lvadapador_negocio(requireContext(), filteredNegocios)
                            lv_opor!!.adapter = ada_negocios
                            Log.d("tag", filteredNegocios.toString())
                        }
                    }

                    override fun onFailure(errorMessage: String) {
                        Log.d("tag", errorMessage)
                        // Handle the failure case
                    }
                })

                lv_opor!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedNegocio = ada_negocios?.getItem(position) as imagem_negocio
                    val gson = Gson()
                    val negocioJson = gson.toJson(selectedNegocio)
                    val intent = Intent(requireContext(), Detailed_negocio::class.java)
                    intent.putExtra("negocioJson", negocioJson)
                    startActivity(intent)
                }
            }
            1 -> {
                parceriaApi.listParcerias(object : Parceria.GetParceriaCallback {
                    override fun onSuccess(parcerias: List<imagem_parceria>) {
                        loadingLayout.visibility = View.GONE
                        if (cargoId <= 2) {
                            ada_parcerias = Lvadapador_parceria(requireContext(), parcerias)
                            lv_opor!!.adapter = ada_parcerias
                            Log.d("tag", parcerias.toString())
                        } else {
                            val filteredParcerias = parcerias.filter { it.userId_img_parceria == userId }
                            ada_parcerias = Lvadapador_parceria(requireContext(), filteredParcerias)
                            lv_opor!!.adapter = ada_parcerias
                            Log.d("tag", filteredParcerias.toString())
                        }
                    }

                    override fun onFailure(errorMessage: String) {
                        Log.d("tag", errorMessage)
                        // Handle the failure case
                    }
                })

                lv_opor!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedParceria = ada_parcerias?.getItem(position) as imagem_parceria
                    val gson = Gson()
                    val parceriaJson = gson.toJson(selectedParceria)
                    val intent = Intent(requireContext(), Detailed_parceria::class.java)
                    intent.putExtra("parceriaJson", parceriaJson)
                    startActivity(intent)
                }
            }
            2 -> {
                investimentoApi.listInvestimentos(object : Investimento.GetInvestimentoCallback {
                    override fun onSuccess(investimentos: List<imagem_investimento>) {
                        loadingLayout.visibility = View.GONE
                        if (cargoId <= 2) {
                            ada_investimentos = Lvadapador_investimento(requireContext(), investimentos)
                            lv_opor!!.adapter = ada_investimentos
                            Log.d("tag", investimentos.toString())
                        } else {
                            val filteredInvestimentos = investimentos.filter { it.userId_img_investimento == userId }
                            ada_investimentos = Lvadapador_investimento(requireContext(), filteredInvestimentos)
                            lv_opor!!.adapter = ada_investimentos
                            Log.d("tag", filteredInvestimentos.toString())
                        }
                    }

                    override fun onFailure(errorMessage: String) {
                        Log.d("tag", errorMessage)
                        // Handle the failure case
                    }
                })

                lv_opor!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedInvestimento = ada_investimentos?.getItem(position) as imagem_investimento
                    val gson = Gson()
                    val investimentoJson = gson.toJson(selectedInvestimento)
                    val intent = Intent(requireContext(), Detailed_investimento::class.java)
                    intent.putExtra("investimentoJson", investimentoJson)
                    startActivity(intent)
                }
            }
            3 -> {
                projetoApi.listProjetos(object : Projeto.GetProjetoCallback {
                    override fun onSuccess(oportunidades: List<imagem_projeto>) {
                        loadingLayout.visibility = View.GONE
                        if (cargoId <= 2) {
                            ada_projetos = Lvadapador_projeto(requireContext(), oportunidades)
                            lv_opor!!.adapter = ada_projetos
                            Log.d("tag", oportunidades.toString())
                        } else {
                            val filteredProjetos = oportunidades.filter { it.userId_img_projeto == userId }
                            ada_projetos = Lvadapador_projeto(requireContext(), filteredProjetos)
                            lv_opor!!.adapter = ada_projetos
                            Log.d("tag", filteredProjetos.toString())
                        }
                    }

                    override fun onFailure(errorMessage: String) {
                        Log.d("tag", errorMessage)
                        // Handle the failure case
                    }
                })

                lv_opor!!.setOnItemClickListener { _, _, position, _ ->
                    val selectedProjeto = ada_projetos?.getItem(position) as imagem_projeto
                    val gson = Gson()
                    val projetoJson = gson.toJson(selectedProjeto)
                    val intent = Intent(requireContext(), Detailed_projeto::class.java)
                    intent.putExtra("projetoJson", projetoJson)
                    startActivity(intent)
                }
            }

        }

    }
}

