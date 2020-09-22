package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Perfil
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*

class ComboPerfilAdapter(private val listener: OnItemClickListener.PerfilListener) :
    RecyclerView.Adapter<ComboPerfilAdapter.ViewHolder>() {

    private var menu = emptyList<Perfil>()

    fun addItems(list: List<Perfil>) {
        menu = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu[position], listener)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Perfil, listener: OnItemClickListener.PerfilListener) = with(itemView) {
            textViewTitulo.text = m.descripcion
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}