package com.example.projeto_softinsa_mobile_app.lvadapador
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_vaga
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.API.Departamento
import com.example.projeto_softinsa_mobile_app.API.Filial


class Lvadapador_vaga(private val context: Context, private val vagaList: List<imagem_vaga>) : BaseAdapter()  {

    override fun  getViewTypeCount(): Int {
        if(count > 0){
            return count;
        }else {
            return super.getViewTypeCount();

        } }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return vagaList.size
    }

    override fun getItem(position: Int): Any {
        return vagaList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item_vaga, null, true)

            holder.tvNomeVaga = convertView.findViewById(R.id.Nome_Vaga) as TextView
            holder.tvFilialId = convertView.findViewById(R.id.Localizacao_Vaga) as TextView
            holder.tvDepartamentoId = convertView.findViewById(R.id.Area_Vaga) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val vaga = vagaList[position]
        holder.tvNomeVaga?.text = vaga.titulo_img_vaga

        val filialId = vaga.filialId_img_vaga
        Log.d("tag", "$filialId")
        val filial = Filial(context, null)
        if (filialId != null) {
            filial.getFilial(object : Filial.GetFilialCallback {
                override fun onSuccess(filial: Filial.Filial) {
                    Log.d("tag", "${filial.filialNome}")
                    holder.tvFilialId?.text = filial.filialNome
                }

                override fun onFailure(errorMessage: String) {
                    holder.tvFilialId?.text = "Error: $errorMessage"
                }
            }, filialId)
        } else {
            holder.tvFilialId?.text = "Necessário um filialId"
        }

        val departamentoId = vaga.departamentoId_img_vaga
        Log.d("tag", "$departamentoId")
        val departamento = Departamento(context, null)
        if (departamentoId != null) {
            departamento.getDepartamento(object : Departamento.GetDepartamentoCallback {
                override fun onSuccess(departamento: Departamento.Departamento) {
                    Log.d("tag", "${departamento.departamentoNome}")
                    holder.tvDepartamentoId?.text = departamento.departamentoNome
                }

                override fun onFailure(errorMessage: String) {
                    holder.tvDepartamentoId?.text  = "Error: $errorMessage"
                }
            }, departamentoId)
        } else {
            holder.tvDepartamentoId?.text  = "Necessário um departamentoID"
        }

       

        return convertView!!
    }

    private inner class ViewHolder {

        var tvNomeVaga: TextView? = null
        var tvFilialId: TextView? = null
        var tvDepartamentoId: TextView? = null
    }
}