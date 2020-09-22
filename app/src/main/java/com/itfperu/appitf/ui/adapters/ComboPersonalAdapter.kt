package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Personal
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*

class ComboPersonalAdapter(private val listener: OnItemClickListener.PersonalListener) :
    RecyclerView.Adapter<ComboPersonalAdapter.ViewHolder>() {

    private var menu = emptyList<Personal>()

    fun addItems(list: List<Personal>) {
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
        fun bind(m: Personal, listener: OnItemClickListener.PersonalListener) = with(itemView) {
            textViewTitulo.text = String.format("%s %s %s", m.nombre, m.apellidoP, m.apellidoM)
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}