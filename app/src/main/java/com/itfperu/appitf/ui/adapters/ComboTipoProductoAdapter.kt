package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TipoProducto
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*

class ComboTipoProductoAdapter(private val listener: OnItemClickListener.TipoProductoListener) :
    RecyclerView.Adapter<ComboTipoProductoAdapter.ViewHolder>() {

    private var menu = emptyList<TipoProducto>()

    fun addItems(list: List<TipoProducto>) {
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
        fun bind(m: TipoProducto, listener: OnItemClickListener.TipoProductoListener) = with(itemView) {
            textViewTitulo.text = m.descripcion
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}