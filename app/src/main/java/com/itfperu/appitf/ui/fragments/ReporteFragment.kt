package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.data.viewModel.ReporteViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboCicloAdapter
import com.itfperu.appitf.ui.adapters.TabLayoutAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_reporte.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReporteFragment : DaggerFragment(), View.OnClickListener {

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
        return inflater.inflate(R.layout.fragment_reporte, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ReporteViewModel::class.java)

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab4))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab5))

        val tabLayoutAdapter =
            TabLayoutAdapter.ReporteForm(
                requireActivity().supportFragmentManager, tabLayout.tabCount, tipo
            )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        itfViewModel.getRptGeneralCabecera().observe(viewLifecycleOwner, { g ->
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
            itfViewModel.syncRRMMGeneral(cicloId, usuarioId)
        } else {
            itfViewModel.syncSUPGeneral(cicloId, usuarioId)
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

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            ReporteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}