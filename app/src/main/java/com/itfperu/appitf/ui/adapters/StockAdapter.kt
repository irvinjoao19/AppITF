package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.StockMantenimiento
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_actividad.view.*
import kotlinx.android.synthetic.main.cardview_actividad.view.card
import kotlinx.android.synthetic.main.cardview_actividad.view.textView1
import kotlinx.android.synthetic.main.cardview_actividad.view.textView2
import kotlinx.android.synthetic.main.cardview_actividad.view.textView4
import kotlinx.android.synthetic.main.cardview_actividad.view.textView6

class StockAdapter :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    private var perfiles = emptyList<StockMantenimiento>()

    fun addItems(list: List<StockMantenimiento>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_stock_mantenimiento, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(perfiles[position])
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: StockMantenimiento) =
            with(itemView) {
                textView1.text = String.format("%s - %s", p.codigoProducto, p.descripcion)
                Util.getTextStyleHtml(String.format("<strong>Lote :</strong> %s", p.lote), textView2)
                Util.getTextStyleHtml(
                    String.format(
                        "<strong>Cant. Asignada :</strong> %s",
                        p.cantidadAsignada
                    ), textView3
                )
                Util.getTextStyleHtml(
                    String.format("<strong>Stock :</strong> %s", p.stock),
                    textView4
                )
            }
    }
}