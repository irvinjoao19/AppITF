package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Especialidad
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_categoria.view.*
import kotlinx.android.synthetic.main.cardview_especialidad.view.*
import kotlinx.android.synthetic.main.cardview_especialidad.view.card
import kotlinx.android.synthetic.main.cardview_especialidad.view.imgDelete
import kotlinx.android.synthetic.main.cardview_especialidad.view.imgEdit
import kotlinx.android.synthetic.main.cardview_especialidad.view.textView1
import kotlinx.android.synthetic.main.cardview_especialidad.view.textView2
import kotlinx.android.synthetic.main.cardview_especialidad.view.textView3

class EspecialidadAdapter(private var listener: OnItemClickListener.EspecialidadListener) :
    RecyclerView.Adapter<EspecialidadAdapter.ViewHolder>() {

    private var perfiles = emptyList<Especialidad>()

    fun addItems(list: List<Especialidad>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_especialidad, parent, false)
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
            p: Especialidad,
            position: Int,
            listener: OnItemClickListener.EspecialidadListener
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
                textView1.text = p.descripcion
                textView2.text = p.codigo
                textView3.text = p.estado
                imgEdit.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                imgDelete.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}