package com.example.projeto_softinsa_mobile_app.lvadapador


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_ideia
import com.example.projeto_softinsa_mobile_app.API.Perfil
import java.util.ArrayList



class Lvadapador_ideia(private val context: Context, private val IdeiaArrayList: ArrayList<imagem_ideia>) : BaseAdapter() {

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
        return IdeiaArrayList.size
    }

    override fun getItem(position: Int): Any {
        return IdeiaArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val convertViewToUse: View

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewToUse = inflater.inflate(R.layout.lv_item_ideia, null, true)

            holder.tvTituloIdeia = convertViewToUse.findViewById(R.id.Titulo_ideia) as TextView
            holder.tvTipoIdeia = convertViewToUse.findViewById(R.id.Tipo_ideia) as TextView
            holder.tvUserIdeia = convertViewToUse.findViewById(R.id.User_ideia) as TextView

            convertViewToUse.tag = holder
        } else {
            convertViewToUse = convertView
            holder = convertViewToUse.tag as ViewHolder
        }

        holder.tvTituloIdeia?.text = IdeiaArrayList[position].da_titulo_ideia()
        holder.tvTipoIdeia?.text = IdeiaArrayList[position].da_tipo_ideia()
        val user_ideia = IdeiaArrayList[position]
        holder.tvUserIdeia?.text = IdeiaArrayList[position].da_User_ideia().toString()
        val userId = user_ideia.tipo_img_User_ideia
        Log.d("tag", "$userId")
        val user = Perfil(context, null)
        if (userId != null) {
            user.getUser(userId, object : Perfil.GetUserCallback {
                override fun onSuccess(user: Perfil.User) {
                    Log.d("tag", "${user.primeiroNome}")
                    holder.tvUserIdeia?.text = user.primeiroNome + " " + user.ultimoNome
                }

                override fun onFailure(errorMessage: String) {
                    holder.tvUserIdeia?.text = "Error: $errorMessage"
                }
            })
        } else {
            holder.tvUserIdeia?.text = "Necessário um filialId"
        }
        holder.tvUserIdeia?.text = IdeiaArrayList[position].da_User_ideia().toString()
        return convertViewToUse
    }

    private inner class ViewHolder {
        var tvTituloIdeia: TextView? = null
        var tvUserIdeia: TextView? = null
        var tvTipoIdeia: TextView? = null
    }
}