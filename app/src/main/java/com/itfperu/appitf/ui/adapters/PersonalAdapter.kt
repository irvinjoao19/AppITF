package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Personal
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_personal.view.*
import java.util.*
import kotlin.collections.ArrayList

class PersonalAdapter(private val listener: OnItemClickListener.PersonalListener) :
    RecyclerView.Adapter<PersonalAdapter.ViewHolder>() {

    private var d = emptyList<Personal>()
    private var dList: ArrayList<Personal> = ArrayList()

    fun addItems(list: List<Personal>) {
        d = list
        dList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_personal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(dList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return dList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Personal, position: Int, listener: OnItemClickListener.PersonalListener) =
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

                textView1.text = m.login
                textView2.text = String.format("%s %s %s", m.nombre, m.apellidoP, m.apellidoM)
                textView3.text = m.email
                textView4.text = m.nombreEstado
                textView5.text = m.nombrePerfil
                if (m.estado == 0) {
                    textView4.setTextColor(
                        ContextCompat.getColor(
                            itemView.context, R.color.colorRed
                        )
                    )
                }
                imgEdit.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
                imgDelete.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
            }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                dList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    dList.addAll(d)
                } else {
                    val filteredList = ArrayList<Personal>()
                    for (s: Personal in d) {
                        if (s.nombre.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            s.login.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            s.apellidoM.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            s.apellidoP.toLowerCase(Locale.getDefault()).contains(keyword) ||
                            s.nroDoc.toLowerCase(Locale.getDefault()).contains(keyword)
                        ) {
                            filteredList.add(s)
                        }
                    }
                    dList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}