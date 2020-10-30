package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Programacion
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_programacion.view.*
import kotlinx.android.synthetic.main.cardview_programacion.view.card
import kotlinx.android.synthetic.main.cardview_programacion.view.textView1
import kotlinx.android.synthetic.main.cardview_programacion.view.textView2
import kotlinx.android.synthetic.main.cardview_programacion.view.textView3
import kotlinx.android.synthetic.main.cardview_programacion.view.textView4
import kotlinx.android.synthetic.main.cardview_programacion.view.textView5
import kotlinx.android.synthetic.main.cardview_target_altas.view.*

class ProgramacionAdapter(private var listener: OnItemClickListener.ProgramacionListener) :
    RecyclerView.Adapter<ProgramacionAdapter.ViewHolder>() {

    private var perfiles = emptyList<Programacion>()

    fun addItems(list: List<Programacion>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_programacion, parent, false)
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
        fun bind(p: Programacion, listener: OnItemClickListener.ProgramacionListener) =
            with(itemView) {
                if (p.active != 0) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.divider)
                    )
                }
                textView1.text = String.format("Ciclo : %s", p.nombreCiclo)
                textView2.text = String.format(
                    "%s %s",
                    p.cmpMedico, p.nombreMedico,
                )
                textView3.text = String.format("Cat : %s  Esp : %s", p.categoria, p.especialidad)
                if (p.fechaProgramacion != "01/01/1900") {
                    textView4.text = String.format("F.P. : %s",p.fechaProgramacion)
                    textView8.text = String.format("H.P. : %s",p.horaProgramacion)
                }
                if (p.fechaReporteProgramacion != "01/01/1900") {
                    textView5.text = String.format("F.R. : %s",p.fechaReporteProgramacion)
                    textView9.text = String.format("H.R. : %s",p.horaReporteProgramacion)
                }
                textView6.text = String.format("Resultado : %s", p.descripcionResultado)
                textView7.text = p.descripcionEstado
                imgInfo.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}