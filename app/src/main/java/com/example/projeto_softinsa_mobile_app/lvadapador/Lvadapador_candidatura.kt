package com.example.projeto_softinsa_mobile_app.lvadapador
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_candidatura
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.API.Perfil
import java.text.SimpleDateFormat
import java.util.*


class Lvadapador_candidatura(private val context: Context, private val CandidaturaList: List<imagem_candidatura>) : BaseAdapter()  {

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
        return CandidaturaList.size
    }

    override fun getItem(position: Int): Any {
        return CandidaturaList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item_candidatura, null, true)

            holder.tvNomeCandidatura = convertView!!.findViewById(R.id.candidatura_Nome_Candidato) as TextView
            holder.tvDataCandidatura = convertView!!.findViewById(R.id.candidatura_Data) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }
        val candidatura = CandidaturaList[position]
        val  userId = candidatura.userId_img_candidatura
        Log.d("tag", "$userId")
        val user = Perfil(context, null)
        if (userId != null) {
            user.getUser(userId , object : Perfil.GetUserCallback {
                override fun onSuccess(user: Perfil.User) {
                    Log.d("tag", "${user.primeiroNome}")
                    holder.tvNomeCandidatura?.text = user.primeiroNome + " " + user.ultimoNome
                }

                override fun onFailure(errorMessage: String) {
                    holder.tvNomeCandidatura?.text  = "Error: $errorMessage"
                }
            })
        } else {
            holder.tvNomeCandidatura?.text  = "O id não existe"
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(candidatura.da_dataCriacao_candidatura())
        val formattedDate = dateFormat.format(date)
        holder.tvDataCandidatura?.text = formattedDate
        return convertView
    }

    private inner class ViewHolder {

        var tvNomeCandidatura: TextView? = null
        var tvDataCandidatura: TextView? = null
    }
}