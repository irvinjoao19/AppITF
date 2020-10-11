package com.itfperu.appitf.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_check_medico.view.*

class CheckMedicoAdapter(private var listener: OnItemClickListener.CheckMedicoListener) :
    PagedListAdapter<Medico, CheckMedicoAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = getItem(position)
        if (s != null) {
            holder.bind(s, listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_check_medico, parent, false)
        return ViewHolder(v!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(s: Medico, listener: OnItemClickListener.CheckMedicoListener) =
            with(itemView) {
                textView1.text = String.format("%s %s", s.nombreMedico, s.apellidoP)
                textView2.text = s.cpmMedico
                textView3.text = s.nombreIdentificador

                checkboxAdd.isChecked = s.isSelected
                checkboxAdd.setOnCheckedChangeListener { _, isChecked ->
                    if (checkboxAdd.isPressed) {
                        listener.onCheckedChanged(s, adapterPosition, isChecked)
                    }
                }
                itemView.setOnClickListener {
                    listener.onCheckedChanged(s, adapterPosition, !s.isSelected)
                }
            }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Medico>() {
            override fun areItemsTheSame(oldItem: Medico, newItem: Medico): Boolean =
                oldItem.medicoId == newItem.medicoId

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Medico, newItem: Medico): Boolean =
                oldItem == newItem
        }
    }
}