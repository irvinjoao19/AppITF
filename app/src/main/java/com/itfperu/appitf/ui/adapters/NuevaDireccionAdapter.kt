package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.NuevaDireccion
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_nueva_direccion.view.*

class NuevaDireccionAdapter(private var listener: OnItemClickListener.NuevaDireccionListener) :
    RecyclerView.Adapter<NuevaDireccionAdapter.ViewHolder>() {

    private var perfiles = emptyList<NuevaDireccion>()

    fun addItems(list: List<NuevaDireccion>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_nueva_direccion, parent, false)
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
        fun bind(p: NuevaDireccion, listener: OnItemClickListener.NuevaDireccionListener) =
            with(itemView) {
                if (p.active != 0) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.divider)
                    )
                }
                textView1.text = p.nombreMedico
                textView2.text = p.fechaSolicitud
                textView3.text = p.nombreDireccion
                textView5.text = p.aprobador
                textView6.text = p.descripcionEstado
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}