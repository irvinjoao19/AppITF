package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Combos
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_categoria.view.*

class MenuAdapter(private var listener: OnItemClickListener.ComboListener) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var perfiles = emptyList<Combos>()

    fun addItems(list: List<Combos>) {
        perfiles = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_menu, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(perfiles[position], position, listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: Combos, position: Int, listener: OnItemClickListener.ComboListener) =
            with(itemView) {
//                if (position % 2 == 1) {
//                    card.setCardBackgroundColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorGrey)
//                    )
//                } else {
//                    card.setCardBackgroundColor(
//                        ContextCompat.getColor(itemView.context, R.color.colorWhite)
//                    )
//                }
                textView1.text = p.title
                itemView.setOnClickListener { v -> listener.onItemClick(p, v, adapterPosition) }
            }
    }
}