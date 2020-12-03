package com.itfperu.appitf.ui.fragments.edition

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.RptGeneral
import com.itfperu.appitf.data.viewModel.ReporteViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_reporte_sub_mes.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReporteSubMesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ReporteViewModel
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reporte_sub_mes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ReporteViewModel::class.java)

        itfViewModel.getRptGeneralMes().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val count: Int = tablaMes.childCount
                for (i in 2 until count) {
                    val child: View = tablaMes.getChildAt(i)
                    if (child is TableRow) {
                        (child as ViewGroup).removeAllViews()
                        (child as ViewGroup).visibility = View.GONE
                    }
                }
                getData(it)
            }
        })
    }

    private fun getData(data: List<RptGeneral>) {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        val textSize = 15
        val smallTextSize = 15
        val rows = data.size

        for (i in 0 until rows) {
            val r: RptGeneral = data[i]

            val tv = TextView(context)
            tv.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
            )
            tv.gravity = Gravity.CENTER
            tv.setPadding(5, 15, 0, 5)
            tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv.text = r.representanteMedico
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())

            val tv2 = TextView(context)
            tv2.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT
            )
            tv2.setPadding(5, 15, 0, 5)
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            tv2.gravity = Gravity.CENTER
            tv2.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv2.setTextColor(Color.parseColor("#000000"))
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv2.text = r.accion

            val tv3 = TextView(context)
            tv3.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT
            )
            tv3.setPadding(5, 15, 0, 5)
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            tv3.gravity = Gravity.CENTER
            tv3.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv3.setTextColor(Color.parseColor("#000000"))
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv3.text = r.cuotaMes.toString()

            val tv4 = TextView(context)
            tv4.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT
            )
            tv4.setPadding(5, 15, 0, 5)
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            tv4.gravity = Gravity.CENTER
            tv4.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv4.setTextColor(Color.parseColor("#000000"))
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv4.text = r.numeroVisita.toString()

            val tv5 = TextView(context)
            tv5.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT
            )
            tv5.setPadding(5, 15, 0, 5)
            tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            tv5.gravity = Gravity.CENTER
            tv5.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv5.setTextColor(Color.parseColor("#000000"))
            tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
            tv5.text = r.porcentajeMes.toString()

            val tr = TableRow(context)
            val trParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            trParams.setMargins(
                leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin
            )
            tr.setPadding(5, 5, 5, 5)
            tr.layoutParams = trParams
            tr.addView(tv)
            tr.addView(tv2)
            tr.addView(tv3)
            tr.addView(tv4)
            tr.addView(tv5)
            tablaMes.addView(tr, trParams)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReporteSubMesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}