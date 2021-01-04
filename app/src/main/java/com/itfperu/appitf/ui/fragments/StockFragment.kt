package com.itfperu.appitf.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Ciclo
import com.itfperu.appitf.data.local.model.Filtro
import com.itfperu.appitf.data.viewModel.StockViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.ComboCicloAdapter
import com.itfperu.appitf.ui.adapters.StockAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_stock.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class StockFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: StockViewModel
    private var usuarioId: Int = 0

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.search).setVisible(true).isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> dialogSearch()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(StockViewModel::class.java)

        val stockAdapter = StockAdapter()
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = stockAdapter

        itfViewModel.setLoading(true)
        itfViewModel.syncStockMantenimiento(usuarioId, 0)
        itfViewModel.getStockMantenimientos().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
//            refreshLayout.isRefreshing = false
            stockAdapter.addItems(it)
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(requireContext(), it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            Util.toastMensaje(requireContext(), it)
        })
        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                stockAdapter.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }


    private fun dialogSearch() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_stock, null)

        val editTextCiclo: TextInputEditText = v.findViewById(R.id.editTextCiclo)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val f = Filtro()

        editTextCiclo.setOnClickListener { spinnerDialog(editTextCiclo, f) }
        fabSearch.setOnClickListener {
            if (f.cicloId == 0) {
                itfViewModel.setError("Seleccione Ciclo")
                return@setOnClickListener
            }
            itfViewModel.setLoading(true)
            itfViewModel.syncStockMantenimiento(usuarioId, f.cicloId)

            dialog.dismiss()
        }
    }

    private fun spinnerDialog(input: TextInputEditText, f: Filtro) {
        val builder =
            android.app.AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
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
        val cicloAdapter = ComboCicloAdapter(object : OnItemClickListener.CicloListener {
            override fun onItemClick(c: Ciclo, view: View, position: Int) {
                f.cicloId = c.cicloId
                input.setText(c.nombre)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = cicloAdapter
        itfViewModel.getCiclos().observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                itfViewModel.setError("Datos vacios favor de sincronizar.")
            }
            cicloAdapter.addItems(it)
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            StockFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}