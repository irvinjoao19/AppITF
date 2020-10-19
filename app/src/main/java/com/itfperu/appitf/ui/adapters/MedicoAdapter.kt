package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_medico.view.*
import kotlinx.android.synthetic.main.cardview_medico.view.textView1
import kotlinx.android.synthetic.main.cardview_medico.view.textView2
import kotlinx.android.synthetic.main.cardview_medico.view.textView3
import kotlinx.android.synthetic.main.cardview_medico.view.textView5
import kotlinx.android.synthetic.main.cardview_target_det.view.*

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
            textView1.text = String.format("CMP : %s", m.cpmMedico)
            textView2.text = String.format("%s %s %s", m.nombreMedico, m.apellidoP, m.apellidoM)
            textView3.text = String.format("Categoria : %s", m.nombreCategoria)
            textView5.text = m.nombreEspecialidad
            imgEdit.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
//            imgDelete.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}