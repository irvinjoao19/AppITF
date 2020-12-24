package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.PuntoContacto
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_punto_contacto.view.*

class PuntoContactoAdapter(private var listener: OnItemClickListener.PuntoContactoListener) :
    RecyclerView.Adapter<PuntoContactoAdapter.ViewHolder>() {

    private var puntoContactos = emptyList<PuntoContacto>()

    fun addItems(list: List<PuntoContacto>) {
        puntoContactos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_punto_contacto, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(puntoContactos[position], listener)
    }

    override fun getItemCount(): Int {
        return puntoContactos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: PuntoContacto, listener: OnItemClickListener.PuntoContactoListener) =
            with(itemView) {
                if (p.active != 0) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.divider)
                    )
                }

                when (p.estadoId) {
                    30 -> view.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.colorWhiteBlue)
                    )
                    31 -> view.setBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.colorGreen)
                    )
                }

                textView1.text = p.descripcion
                textView2.text = String.format("Fecha: %s", p.fechaPuntoContacto)
                textView3.text = String.format("Estado: %s", p.descripcionEstado)
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}