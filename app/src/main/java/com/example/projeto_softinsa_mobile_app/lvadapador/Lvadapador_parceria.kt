package com.example.projeto_softinsa_mobile_app.lvadapador
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.negocio_softinsa_mobile_app.Des_tudo.imagem_parceria
import com.example.projeto_softinsa_mobile_app.R


class Lvadapador_parceria(private val context: Context, private val ParceriaList: List<imagem_parceria>) : BaseAdapter()  {

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
        return ParceriaList.size
    }

    override fun getItem(position: Int): Any {
        return ParceriaList[position]
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
            convertView = inflater.inflate(R.layout.lv_item_parceria, null, true)

            holder.tvTituloOportunidade = convertView!!.findViewById(R.id.Nome_Parceria) as TextView
            holder.tvEmailParceria = convertView!!.findViewById(R.id.Email_Parceria) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        val parceria = ParceriaList[position]
        holder.tvTituloOportunidade?.text = parceria.nomeParceiro_img_parceria
        holder.tvEmailParceria?.text = parceria.email_img_parceria

        return convertView!!
    }

    private inner class ViewHolder {

        var tvTituloOportunidade: TextView? = null
        var tvEmailParceria: TextView? = null
    }
}