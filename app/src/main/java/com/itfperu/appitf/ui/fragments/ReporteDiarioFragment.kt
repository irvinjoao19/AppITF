package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.data.local.model.RptDiario
import com.itfperu.appitf.data.viewModel.ReporteViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboCicloAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_reporte_diario.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReporteDiarioFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        spinnerDialog()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ReporteViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    private var usuarioId: Int = 0
    private var tipo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
            tipo = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reporte_diario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ReporteViewModel::class.java)

        itfViewModel.getRptDiarioCabecera().observe(viewLifecycleOwner, { g ->
            if (g != null) {
                editTextCiclo.setText(g.nombreCiclo)
                Util.getTextStyleHtml(
                    String.format(
                        "<strong>Fecha Inicio Ciclo : </strong>%s", g.fechaInicioCiclo
                    ), textView1
                )
                Util.getTextStyleHtml(
                    String.format(
                        "<strong>Fecha Fin Ciclo : </strong> %s", g.fechaFinCiclo
                    ), textView2
                )
                Util.getTextStyleHtml(
                    String.format(
                        "<strong>Fecha Actual : </strong> %s", g.fechaActual
                    ), textView3
                )
                Util.getTextStyleHtml(
                    String.format(
                        "<strong>Dias Ciclo Mes : </strong> %s", g.diasCicloMes
                    ), textView4
                )
                Util.getTextStyleHtml(
                    String.format(
                        "<strong>Dias a la Fecha : </strong> %s", g.diasFecha
                    ), textView5
                )
            } else {
                textView1.text = String.format("Fecha Inicio Ciclo : %s", "")
                textView2.text = String.format("Fecha Fin Ciclo : %s", "")
                textView3.text = String.format("Fecha Actual : %s", "")
                textView4.text = String.format("Dias Ciclo Mes : %s", "")
                textView5.text = String.format("Dias a la Fecha : %s", "")
            }
        })


        itfViewModel.getRptDiario().observe(viewLifecycleOwner,{
            if (it.isNotEmpty()) {
                val count: Int = tablaDiario.childCount
                for (i in 1 until count) {
                    val child: View = tablaDiario.getChildAt(i)
                    if (child is TableRow) {
                        (child as ViewGroup).removeAllViews()
                        (child as ViewGroup).visibility = View.GONE
                    }
                }
                getData(it)
            }
        })


        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                load()
            } else {
                closeLoad()
            }
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            if (it != null) {
                Util.toastMensaje(requireContext(), it)
            }
        })
        editTextCiclo.setOnClickListener(this)

    }


    private fun spinnerDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )
        textViewTitulo.text = String.format("Ciclo")


        itfViewModel.syncCiclo()
        val cicloAdapter = ComboCicloAdapter(object : OnItemClickListener.CicloListener {
            override fun onItemClick(c: Ciclo, view: View, position: Int) {
                generateReporte(c.cicloId, usuarioId)
                editTextCiclo.setText(c.nombre)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = cicloAdapter
        itfViewModel.getCiclos().observe(this, {
            cicloAdapter.addItems(it)
        })
    }

    private fun generateReporte(cicloId: Int, usuarioId: Int) {
        itfViewModel.setLoading(true)
        if (tipo == 0) {
            itfViewModel.syncRRMMDiario(cicloId, usuarioId)
        } else {
            itfViewModel.syncSUPDiario(cicloId, usuarioId)
        }
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Cargando..")
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun closeLoad() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    private fun getData(data: List<RptDiario>) {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        val textSize = 15
        val smallTextSize = 15
        val rows = data.size

        for (i in 0 until rows) {
            val row: RptDiario = data[i]
            val tv = TextView(context)
            tv.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
            )
            tv.gravity = Gravity.CENTER
            tv.setPadding(5, 15, 0, 5)
            tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
            tv.text = row.representanteMedico
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
            tv2.text = row.cuota

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
            tv3.text = row.frecuencia.toString()

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
            tv4.text = row.cobertura.toString()

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
            tablaDiario.addView(tr, trParams)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            ReporteDiarioFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}