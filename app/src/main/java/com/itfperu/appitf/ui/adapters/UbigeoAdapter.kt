package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Ubigeo
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*

class UbigeoAdapter(
    private var tipo: Int, private val listener: OnItemClickListener.UbigeoListener
) :
    RecyclerView.Adapter<UbigeoAdapter.ViewHolder>() {

    private var menu = emptyList<Ubigeo>()

    fun addItems(list: List<Ubigeo>) {
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
        holder.bind(menu[position], tipo, listener)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Ubigeo, tipo: Int, listener: OnItemClickListener.UbigeoListener) =
            with(itemView) {
                when(tipo){
                    1 ->textViewTitulo.text = m.nombreDepartamento
                    2 ->textViewTitulo.text = m.provincia
                    else ->textViewTitulo.text = m.nombreDistrito
                }

                itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
            }
    }
}