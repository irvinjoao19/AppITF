package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import android.widget.Filter
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class ComboMedicoAdapter(private val listener: OnItemClickListener.MedicoListener) :
    RecyclerView.Adapter<ComboMedicoAdapter.ViewHolder>() {

    private var count = emptyList<Medico>()
    private var countList: ArrayList<Medico> = ArrayList()

    fun addItems(list: List<Medico>) {
        count = list
        countList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countList[position], listener)
    }

    override fun getItemCount(): Int {
        return countList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Medico, listener: OnItemClickListener.MedicoListener) = with(itemView) {
            textViewTitulo.text =
                String.format("%s %s %s", m.apellidoP, m.apellidoM,m.nombreMedico)
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                countList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    countList.addAll(count)
                } else {
                    val filteredList = ArrayList<Medico>()
                    for (m: Medico in count) {
                        if (m.cpmMedico.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.nombreMedico.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.apellidoP.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            m.apellidoM.toLowerCase(Locale.getDefault()).contains(keyword)
                        ) {
                            filteredList.add(m)
                        }
                    }
                    countList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}