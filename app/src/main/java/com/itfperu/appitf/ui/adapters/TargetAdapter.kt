package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetM
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_target.view.*

class TargetAdapter() :
    RecyclerView.Adapter<TargetAdapter.ViewHolder>() {

    private var perfiles = emptyList<TargetM>()

    fun addItems(list: List<TargetM>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_target, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(perfiles[position], position)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TargetM, position: Int) =
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
                textView1.text = p.nombreUsuario
                textView2.text = String.format("Cat : %s", p.descripcionCategoria)
                textView3.text = String.format("Esp : %s", p.descripcionEspecialidad)
                textView4.text = String.format("Cont : %s", p.numeroContactos)
                textView5.text = String.format("Estado : %s", p.estado)
            }
    }
}