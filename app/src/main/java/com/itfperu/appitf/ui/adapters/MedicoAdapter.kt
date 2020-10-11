package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_medico.view.*

class MedicoAdapter(private val listener: OnItemClickListener.MedicoListener) :
    RecyclerView.Adapter<MedicoAdapter.ViewHolder>() {

    private var menu = emptyList<Medico>()

    fun addItems(list: List<Medico>) {
        menu = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_medico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu[position], listener)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Medico, listener: OnItemClickListener.MedicoListener) = with(itemView) {
            textView1.text = m.nombreMedico
            imgEdit.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
            imgDelete.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}