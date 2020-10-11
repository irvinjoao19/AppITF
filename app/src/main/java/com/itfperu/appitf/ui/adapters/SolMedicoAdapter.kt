package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.SolMedico
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_sol_medico.view.*

class SolMedicoAdapter(private var listener: OnItemClickListener.SolMedicoListener) :
    RecyclerView.Adapter<SolMedicoAdapter.ViewHolder>() {

    private var perfiles = emptyList<SolMedico>()

    fun addItems(list: List<SolMedico>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_sol_medico, parent, false)
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
        fun bind(p: SolMedico, position: Int, listener: OnItemClickListener.SolMedicoListener) =
            with(itemView) {
                textView1.text = p.usuario
                textView2.text = p.fecha
                textView4.text = p.usuarioAprobador
                textView5.text = p.descripcionEstado
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}