package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Actividad
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_actividad.view.*
import kotlinx.android.synthetic.main.cardview_actividad.view.card
import kotlinx.android.synthetic.main.cardview_actividad.view.textView1
import kotlinx.android.synthetic.main.cardview_actividad.view.textView2
import kotlinx.android.synthetic.main.cardview_actividad.view.textView4
import kotlinx.android.synthetic.main.cardview_actividad.view.textView6

class ActividadAdapter(private var listener: OnItemClickListener.ActividadListener) :
    RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {

    private var perfiles = emptyList<Actividad>()

    fun addItems(list: List<Actividad>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_actividad, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(perfiles[position], listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Actividad, listener: OnItemClickListener.ActividadListener) =
            with(itemView) {
                if (p.active != 0) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.divider)
                    )
                }
                textView1.text = p.usuario
                textView2.text = p.fechaActividad
                textView4.text = p.detalle
                textView6.text = p.aprobador
                textView8.text = p.observacion
                textView9.text = p.descripcionEstado
                textView10.text = p.descripcionDuracion
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}