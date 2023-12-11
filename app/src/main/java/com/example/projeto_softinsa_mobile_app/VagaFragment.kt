 package com.example.projeto_softinsa_mobile_app
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.projeto_softinsa_mobile_app.API.Candidatura
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_vaga
import com.example.projeto_softinsa_mobile_app.Detailed.Detailed_vaga
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_vaga
import com.example.projeto_softinsa_mobile_app.API.Perfil
import com.example.projeto_softinsa_mobile_app.API.Vaga
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_candidatura
import com.example.projeto_softinsa_mobile_app.Detailed.Detailed_candidatura
import com.example.projeto_softinsa_mobile_app.login.Authorization
import com.example.projeto_softinsa_mobile_app.lvadapador.Lvadapador_candidatura
import com.google.gson.Gson

class vagaFragment : Fragment() {
    private var option: Int = 0
    private var lv_vaga: ListView? = null
    private var ada: Lvadapador_vaga? = null
    private var ada2: Lvadapador_candidatura? = null
    private lateinit var vagaApi: Vaga
    private lateinit var candidaturaApi: Candidatura
    var isColaborador = false
    private lateinit var fragmentContext: Context
    var userId = 0


    companion object {
        private const val ARG_OPTION = "option"

        fun newInstance(option: Int): vagaFragment {
            val fragment = vagaFragment()
            val args = Bundle()
            args.putInt(ARG_OPTION, option)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        option = arguments?.getInt(ARG_OPTION) ?: 0
        vagaApi = Vaga(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE).edit())
        candidaturaApi = Candidatura(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE).edit())
        val user = Perfil(fragmentContext, null)
        isColaborador = user.getStoredIsColaborador()
        val auth = Authorization(fragmentContext, null)
        userId = auth.getUserId()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_vaga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lv_vaga = view.findViewById(R.id.lv_vagas) as ListView
        val loadingLayout = view.findViewById<LinearLayout>(R.id.loadingLayout)

        lv_vaga!!.setOnItemClickListener { _, _, position, _ ->
            val selectedVaga = ada?.getItem(position) as imagem_vaga
            val gson = Gson()
            val vagaJson = gson.toJson(selectedVaga)
            val intent = Intent(requireContext(), Detailed_vaga::class.java)
            intent.putExtra("vagaJson", vagaJson)
            startActivity(intent)
        }




        // Perform the database request based on the selected option
        when (option) {
            0 -> {
                vagaApi.listVagas(object : Vaga.GetVagaCallback {
                    override fun onSuccess(vagas: List<imagem_vaga>) {
                        val filteredVagas = vagas.filter { it.isInterna_img_vaga == false }
                        ada = Lvadapador_vaga(requireContext(), filteredVagas)
                        lv_vaga!!.adapter = ada

                        loadingLayout.visibility = View.GONE
                    }

                    override fun onFailure(errorMessage: String) {
                    }
                })
            }
            1 -> {
                vagaApi.listVagas(object : Vaga.GetVagaCallback {
                    override fun onSuccess(vagas: List<imagem_vaga>) {
                        val filteredVagas = vagas.filter { it.isInterna_img_vaga == true }
                        ada = Lvadapador_vaga(requireContext(), filteredVagas)
                        lv_vaga!!.adapter = ada
                        loadingLayout.visibility = View.GONE
                    }

                    override fun onFailure(errorMessage: String) {
                    }
                })

            }
            2 -> {
                candidaturaApi.listCandidaturasUser(userId = userId, object : Candidatura.GetCandidaturaCallback {
                    override fun onSuccess(candidaturas: List<imagem_candidatura>) {

                        ada2 = Lvadapador_candidatura(requireContext(), candidaturas)
                        lv_vaga!!.adapter = ada2
                        loadingLayout.visibility = View.GONE
                    }

                    override fun onFailure(errorMessage: String) {
                    }
                })

                lv_vaga!!.setOnItemClickListener { _, _, position, _ ->
                    val selectCandidatura = ada2?.getItem(position) as imagem_candidatura
                    val gson = Gson()
                    val candidaturaJson = gson.toJson(selectCandidatura)
                    val intent = Intent(requireContext(), Detailed_candidatura::class.java)
                    intent.putExtra("candidaturaJson", candidaturaJson)
                    startActivity(intent)
                }

            }
        }
    }

}
