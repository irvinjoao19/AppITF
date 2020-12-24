package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.BirthDay
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_birth_day.view.*

class BirthDayAdapter(private var listener: OnItemClickListener.BirthDayListener) :
    RecyclerView.Adapter<BirthDayAdapter.ViewHolder>() {

    private var birth = emptyList<BirthDay>()

    fun addItems(list: List<BirthDay>) {
        birth = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_birth_day, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(birth[position], listener)
    }

    override fun getItemCount(): Int {
        return birth.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: BirthDay, listener: OnItemClickListener.BirthDayListener) =
            with(itemView) {
//                if (p.active != 0) {
//                    card.setCardBackgroundColor(
//                        ContextCompat.getColor(itemView.context, R.color.divider)
//                    )
//                }
//                when (p.estado) {
//                    7 -> view.setBackgroundColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorWhiteBlue)
//                    )
//                    8 -> view.setBackgroundColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorRed)
//                    )
//                    9 -> view.setBackgroundColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorGreen)
//                    )
//                }
                textView1.text = String.format("%s - %s", p.cmpMedico, p.nombreMedico)
                textView2.text = String.format("Cat: %s", p.descripcionCategoria)
                textView3.text = String.format("Esp: %s", p.descripcionEspecialidad)
                textView4.text = String.format("Fec. Nacimiento: %s", p.fechaNacimiento)
                textView5.text = String.format("Estado: %s", p.nombreEstado)
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}