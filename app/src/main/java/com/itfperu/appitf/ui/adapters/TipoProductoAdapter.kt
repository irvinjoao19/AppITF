package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TipoProducto
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_categoria.view.*
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.*
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.card
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.imgDelete
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.imgEdit
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.textView1
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.textView2
import kotlinx.android.synthetic.main.cardview_tipo_producto.view.textView3

class TipoProductoAdapter(private var listener: OnItemClickListener.TipoProductoListener) :
    RecyclerView.Adapter<TipoProductoAdapter.ViewHolder>() {

    private var perfiles = emptyList<TipoProducto>()

    fun addItems(list: List<TipoProducto>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_tipo_producto, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(perfiles[position], position, listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            p: TipoProducto, position: Int, listener: OnItemClickListener.TipoProductoListener
        ) =
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
                textView1.text = p.codigo
                textView2.text = p.descripcion
                textView3.text = p.estado
                imgEdit.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                imgDelete.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}