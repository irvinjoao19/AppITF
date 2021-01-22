package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.ProgramacionDet
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_programacion_det.view.*

class ProgramacionDetAdapter(
    private var estado: Int,
    private var listener: OnItemClickListener.ProgramacionDetListener
) :
    RecyclerView.Adapter<ProgramacionDetAdapter.ViewHolder>() {

    private var perfiles = emptyList<ProgramacionDet>()

    fun addItems(list: List<ProgramacionDet>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_programacion_det, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(perfiles[position],estado, listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: ProgramacionDet,e:Int, listener: OnItemClickListener.ProgramacionDetListener) =
            with(itemView) {
                textView1.text = String.format("Orden : %s", p.ordenProgramacion)
                textView2.text = String.format("Codigo : %s", p.codigoProducto)
                textView3.text = p.descripcionProducto
                textView4.text = String.format("Lote : %s", p.lote)
                textView5.text = String.format("Cantidad : %s", p.cantidad)

                if (p.active == 0) {
                    imgDelete.visibility = View.GONE
                }


                if (e == 24) {
                    imgDelete.visibility = View.GONE
                }


                imgDelete.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}