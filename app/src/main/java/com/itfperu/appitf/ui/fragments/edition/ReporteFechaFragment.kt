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
import com.ekn.gruzer.gaugelibrary.Range
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.RptGeneral
import com.itfperu.appitf.data.viewModel.ReporteViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_reporte_fecha.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReporteFechaFragment : DaggerFragment() {

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
        return inflater.inflate(R.layout.fragment_reporte_fecha, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ReporteViewModel::class.java)

        val range = Range()
        range.color = Color.parseColor("#ce0000")
        range.from = 0.0
        range.to = 70.0

        val range2 = Range()
        range2.color = Color.parseColor("#E3E500")
        range2.from = 71.0
        range2.to = 100.0

        val range3 = Range()
        range3.color = Color.parseColor("#00b20b")
        range3.from = 101.0
        range3.to = 150.0

        halfGauge1.addRange(range)
        halfGauge1.addRange(range2)
        halfGauge1.addRange(range3)
        halfGauge2.addRange(range)
        halfGauge2.addRange(range2)
        halfGauge2.addRange(range3)

        //set min max and current value
        halfGauge1.minValue = 0.0
        halfGauge1.maxValue = 150.0

        halfGauge2.minValue = 0.0
        halfGauge2.maxValue = 150.0

        itfViewModel.getRptGeneralFecha().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                halfGauge1.value = 0.0
                halfGauge2.value = 0.0

                val count: Int = tabla1.childCount
                for (i in 2 until count) {
                    val child: View = tabla1.getChildAt(i)
                    if (child is TableRow) {
                        (child as ViewGroup).removeAllViews()
                        (child as ViewGroup).visibility = View.GONE
                    }
                }
                val count2: Int = tabla2.childCount
                for (i in 2 until count2) {
                    val child: View = tabla2.getChildAt(i)
                    if (child is TableRow) {
                        (child as ViewGroup).removeAllViews()
                        (child as ViewGroup).visibility = View.GONE
                    }
                }

                for (i in it.indices) {
                    if (i == 0) {
                        val f1: RptGeneral? = it[i]
                        if (f1 != null) {
                            halfGauge1.value = f1.porcentajeFecha.toDouble()
                            getData(f1, tabla1)
                        }
                    } else {
                        val f2: RptGeneral? = it[i]
                        if (f2 != null) {
                            halfGauge2.value = f2.porcentajeFecha.toDouble()
                            getData(f2, tabla2)
                        }
                    }
                }
            }
        })
    }

    private fun getData(row: RptGeneral, table: TableLayout) {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        val textSize = 23
        val smallTextSize = 23
        val tv = TextView(context)
        tv.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
        )
        tv.gravity = Gravity.CENTER
        tv.setPadding(5, 15, 0, 5)
        tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
        tv.text = row.cuotaFecha.toString()
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
        tv2.text = row.numeroVisita.toString()

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
        tv3.text = row.porcentajeFecha.toString()

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
        tv4.text = row.saldoFecha.toString()


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
        table.addView(tr, trParams)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReporteFechaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}