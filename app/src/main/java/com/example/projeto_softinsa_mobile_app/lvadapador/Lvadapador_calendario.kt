package com.example.projeto_softinsa_mobile_app.lvadapador
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_calendario
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_evento
import com.example.projeto_softinsa_mobile_app.R
import com.example.projeto_softinsa_mobile_app.Des_tudo.imagem_ideia
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale


class Lvadapador_calendario(private val context: Context, private val CalendarioArrayList: ArrayList<imagem_evento>) : BaseAdapter()  {

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return CalendarioArrayList.size
    }

    override fun getItem(position: Int): Any {
        return CalendarioArrayList[position]
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
            convertView = inflater.inflate(R.layout.lv_item_calendario, null, true)

            holder.tvTituloCalendario = convertView!!.findViewById(R.id.calendario_Titulo_Evento) as TextView
            holder.tvDiaCalendario = convertView!!.findViewById(R.id.calendario_Dia_Evento) as TextView
            holder.tvHoraCalendario = convertView!!.findViewById(R.id.calendario_Hora_Evento) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.tvTituloCalendario!!.setText(CalendarioArrayList[position].da_titulo_evento())
        holder.tvDiaCalendario!!.setText(getDateFromDateTime(CalendarioArrayList[position].da_dataInicio_evento()))
        holder.tvHoraCalendario!!.setText("${getTimeFromDate(CalendarioArrayList[position].da_dataInicio_evento())} - ${getTimeFromDate(CalendarioArrayList[position].da_dataFim_evento())}")
        return convertView
    }

    private inner class ViewHolder {

        var tvTituloCalendario: TextView? = null
        var tvDiaCalendario: TextView? = null
        var tvHoraCalendario: TextView? = null
    }

    private fun getTimeFromDate(dateTimeString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = dateFormat.parse(dateTimeString)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(date)
    }

    private fun getDateFromDateTime(dateTimeString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = dateFormat.parse(dateTimeString)
        val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return timeFormat.format(date)
    }
}