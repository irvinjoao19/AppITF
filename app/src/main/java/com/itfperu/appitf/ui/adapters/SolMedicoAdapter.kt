package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.SolMedico
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_actividad.view.*
import kotlinx.android.synthetic.main.cardview_sol_medico.view.*
import kotlinx.android.synthetic.main.cardview_sol_medico.view.card
import kotlinx.android.synthetic.main.cardview_sol_medico.view.textView1
import kotlinx.android.synthetic.main.cardview_sol_medico.view.textView2
import kotlinx.android.synthetic.main.cardview_sol_medico.view.textView4
import kotlinx.android.synthetic.main.cardview_sol_medico.view.textView5

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
        holder.bind(perfiles[position], listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: SolMedico, listener: OnItemClickListener.SolMedicoListener) =
            with(itemView) {
                if (p.estado != 0) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.divider)
                    )
                }
                textView1.text = p.usuario
//                if (p.identity == 0) {
//                    textView1.setTextColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorRed)
//                    )
//                }

                textView2.text = p.fecha
                textView4.text = p.usuarioAprobador
                textView5.text = p.descripcionEstado
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}