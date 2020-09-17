package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TipoProducto
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.*

class TipoProductoAdapter(private var listener: OnItemClickListener.TipoProductoListener) :
    RecyclerView.Adapter<TipoProductoAdapter.ViewHolder>() {

    private var perfiles = emptyList<TipoProducto>()

    fun addItems(list: List<TipoProducto>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cardview_tipo_producto, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(perfiles[position], position, listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TipoProducto, position: Int, listener: OnItemClickListener.TipoProductoListener) =
            with(itemView) {
                if (position % 2 == 1) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.colorGrey)
                    )
                } else {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.colorWhite)
                    )
                }
//                textView1.text = p.descripcion
//                textView2.text = p.codigo
//                textView3.text = p.simbolo
//                textView4.text = p.estado
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}