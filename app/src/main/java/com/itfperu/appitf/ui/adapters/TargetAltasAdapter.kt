package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetCab
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_target_altas.view.*

class TargetAltasAdapter(private var listener: OnItemClickListener.TargetAltasListener) :
    RecyclerView.Adapter<TargetAltasAdapter.ViewHolder>() {

    private var perfiles = emptyList<TargetCab>()

    fun addItems(list: List<TargetCab>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_target_altas, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(perfiles[position], position, listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TargetCab, position: Int, listener: OnItemClickListener.TargetAltasListener) =
            with(itemView) {
                textView1.text = String.format("N° : %s", p.targetCabId)
                if (p.usuarioSolicitante.isNotEmpty()) {
                    textViewUsuario.visibility = View.VISIBLE
                    textViewUsuario.text = p.usuarioSolicitante
                }
                textView2.text = p.fechaSolicitud
                textView4.text = p.aprobador
                textView5.text = p.nombreEstado
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}