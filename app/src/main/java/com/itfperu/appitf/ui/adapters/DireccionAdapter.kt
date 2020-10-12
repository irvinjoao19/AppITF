package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.MedicoDireccion
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_medico_direccion.view.*

class DireccionAdapter(private var listener: OnItemClickListener.MedicoDireccionListener) :
    RecyclerView.Adapter<DireccionAdapter.ViewHolder>() {

    private var perfiles = emptyList<MedicoDireccion>()

    fun addItems(list: List<MedicoDireccion>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_medico_direccion, parent, false)
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
        fun bind(
            p: MedicoDireccion, listener: OnItemClickListener.MedicoDireccionListener
        ) =
            with(itemView) {
                textView1.text = p.direccion
                imgEdit.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}