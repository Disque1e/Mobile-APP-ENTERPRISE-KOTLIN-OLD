package com.example.projeto_softinsa_mobile_app.lvadapador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_lista_user
import com.example.projeto_softinsa_mobile_app.R
import java.util.ArrayList

class Lvadapador_lista_user(private val context: Context, private val ListaUserArrayList: ArrayList<imagem_lista_user>) : BaseAdapter() {
    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return ListaUserArrayList.size
    }

    override fun getItem(position: Int): Any {
        return ListaUserArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: Lvadapador_lista_user.ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item_list_users, null, true)

            holder.tvNomeListaUsers = convertView!!.findViewById(R.id.Lista_user_Nome) as TextView
            holder.tvSobrenomeListaUsers = convertView!!.findViewById(R.id.Lista_user_Sobrenome) as TextView
            holder.tvEmailListaUsers = convertView!!.findViewById(R.id.Lista_user_Email) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as Lvadapador_lista_user.ViewHolder
        }

        holder.tvNomeListaUsers!!.setText(ListaUserArrayList[position].da_Nome_lista_user())
        holder.tvSobrenomeListaUsers!!.setText(ListaUserArrayList[position].da_Sobrenome_lista_user())
        holder.tvEmailListaUsers!!.setText(ListaUserArrayList[position].da_Email_lista_user())

        return convertView
    }
    private inner class ViewHolder {

        var tvNomeListaUsers: TextView? = null
        var tvSobrenomeListaUsers: TextView? = null
        var tvEmailListaUsers: TextView? = null

    }
}