package com.itfperu.appitf.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetInfo
import kotlinx.android.synthetic.main.cardview_target_info.view.*

class TargetInfoAdapter :
    RecyclerView.Adapter<TargetInfoAdapter.ViewHolder>() {

    private var info = emptyList<TargetInfo>()

    fun addItems(list: List<TargetInfo>) {
        info = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_target_info, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(info[position])
    }

    override fun getItemCount(): Int {
        return info.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(p: TargetInfo) =
            with(itemView) {
                textView1.text = p.usuario
                textView2.text = String.format(
                    "%s Contacto%s",
                    p.nroContacto, if (p.nroContacto == 1) "" else "s"
                )
            }
    }
}