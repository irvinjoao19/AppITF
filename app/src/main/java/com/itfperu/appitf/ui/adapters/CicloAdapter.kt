package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_ciclo.view.*

class CicloAdapter(private var listener: OnItemClickListener.CicloListener) :
    RecyclerView.Adapter<CicloAdapter.ViewHolder>() {

    private var perfiles = emptyList<Ciclo>()

    fun addItems(list: List<Ciclo>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_ciclo, parent, false)
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
        fun bind(p: Ciclo, position: Int, listener: OnItemClickListener.CicloListener) =
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

                textView1.text = p.nombre
                textView2.text = String.format("Desde: %s",p.desde)
                textView3.text = String.format("Hasta: %s",p.hasta)
                textView4.text = p.nombreEstado
                if (p.estado == 5) {
                    imgEdit.visibility = View.GONE
//                    textView4.setTextColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorRed)
//                    )
                }else{
                    imgEdit.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                }
                // 3	-> Activo    4	-> En Proceso     5	-> Cerrado
            }
    }
}