package com.example.projeto_softinsa_mobile_app.lvadapador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_ben
import java.util.ArrayList


class lvadapatador_ben(private val context: Context, private val imageModelArrayList: ArrayList<imagem_ben>) : BaseAdapter() {

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
        return imageModelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return imageModelArrayList[position]
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
            convertView = inflater.inflate(R.layout.lv_item_ben, null, true)

            holder.tvTitulo = convertView!!.findViewById(R.id.Titulo) as TextView
            holder.tvTipo = convertView!!.findViewById(R.id.Tipo) as TextView
            //holder.iv = convertView.findViewById(R.id.imgView) as ImageView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.tvTitulo!!.setText(imageModelArrayList[position].da_titulo_ben())
        holder.tvTipo!!.setText(imageModelArrayList[position].da_tipo_ben())
        //holder.iv!!.setImageResource(imageModelArrayList[position].da_drawables_ben())

        return convertView
    }

    private inner class ViewHolder {

        var tvTitulo: TextView? = null
        var tvTipo: TextView? = null
        //internal var iv: ImageView? = null

    }

}