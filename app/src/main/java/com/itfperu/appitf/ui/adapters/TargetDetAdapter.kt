package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetDet
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_target_det.view.*

class TargetDetAdapter(
    private var tipo: Int,
    private var listener: OnItemClickListener.TargetDetListener
) :
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
        holder.bind(det[position], tipo, listener)
    }

    override fun getItemCount(): Int {
        return det.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TargetDet, tipo: Int, listener: OnItemClickListener.TargetDetListener) =
            with(itemView) {
                textView1.text = String.format("CMP : %s", p.cmp)
                textView2.text = p.nombreMedico
                textView3.text = String.format("Categoria : %s", p.nombreCategoria)
                textView5.text = p.nombreEspecialidad
                editTextCantidad.setText(p.nroContacto.toString())

                if (tipo == 2) {
                    if (p.tipoTarget == "A") {
                        if (p.visitado == 0)
                            imgInfo.visibility = View.GONE
                        else
                            imgInfo.visibility = View.VISIBLE

                        if (p.nrovisita == 1) {
                            imgAprobar.visibility = View.VISIBLE
                            imgRechazar.visibility = View.VISIBLE
                        } else {
                            textView7.visibility = View.VISIBLE
                            textView7.text = p.mensajeNrovisita
                            imgRechazar.visibility = View.VISIBLE
                        }
                    } else {
                        imgAprobar.visibility = View.VISIBLE
                        imgRechazar.visibility = View.VISIBLE

                        editTextCantidad.visibility = View.GONE
                        imgNegative.visibility = View.GONE
                        imgPositive.visibility = View.GONE
                    }
                }

                if (p.estadoTarget == 18 || p.estadoTarget == 17) {
                    textView8.visibility = View.VISIBLE
                    textView8.text = if (p.estadoTarget == 18) "Aprobada" else "Rechazada"
                    imgAprobar.visibility = View.GONE
                    imgRechazar.visibility = View.GONE
                    textView6.text = String.format("Nro Contactos : %s", p.nroContacto)
                    textView6.visibility = View.VISIBLE
                    editTextCantidad.visibility = View.GONE
                    imgNegative.visibility = View.GONE
                    imgPositive.visibility = View.GONE
                }


                editTextCantidad.setOnClickListener { v ->
                    listener.onItemClick(p, v, adapterPosition)
                }
                imgNegative.setOnClickListener { v ->
                    listener.onItemClick(p, v, adapterPosition)
                }
                imgPositive.setOnClickListener { v ->
                    listener.onItemClick(p, v, adapterPosition)
                }
                imgInfo.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                imgAprobar.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
                imgRechazar.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}