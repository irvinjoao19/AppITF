package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetDet
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_target_det.view.*

class TargetDetAdapter(private var listener: OnItemClickListener.TargetDetListener) :
    RecyclerView.Adapter<TargetDetAdapter.ViewHolder>() {

    private var det = emptyList<TargetDet>()

    fun addItems(list: List<TargetDet>) {
        det = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_target_det, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(det[position], position, listener)
    }

    override fun getItemCount(): Int {
        return det.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TargetDet, position: Int, listener: OnItemClickListener.TargetDetListener) =
            with(itemView) {
                textView1.text = String.format("CMP : %s", p.cmp)
                textView2.text = p.nombreMedico
                textView3.text = String.format("Categoria : %s", p.nombreCategoria)
                textView5.text = p.nombreEspecialidad

                if (p.tipoTarget == "B") {
                    textView6.visibility = View.GONE
                    editTextCantidad.visibility = View.GONE
                    imageViewNegative.visibility = View.GONE
                    imageViewPositive.visibility = View.GONE
                } else {
                    editTextCantidad.setText(p.nroContacto.toString())
                    editTextCantidad.setOnClickListener { v ->
                        listener.onItemClick(p, v, adapterPosition)
                    }
                    imageViewNegative.setOnClickListener { v ->
                        listener.onItemClick(p, v, adapterPosition)
                    }
                    imageViewPositive.setOnClickListener { v ->
                        listener.onItemClick(p, v, adapterPosition)
                    }
                }
            }
    }
}