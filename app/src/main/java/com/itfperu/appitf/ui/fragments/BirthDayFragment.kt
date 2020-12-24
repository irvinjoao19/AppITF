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
import com.google.gson.Gson
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.*
import com.itfperu.appitf.data.viewModel.BirthDayViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.*
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_birth_day.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class BirthDayFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: BirthDayViewModel
    private var usuarioId: Int = 0
    lateinit var f: Filtro

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
        f = Filtro()
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_birth_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(BirthDayViewModel::class.java)

        val birthDayAdapter = BirthDayAdapter(object : OnItemClickListener.BirthDayListener {
            override fun onItemClick(b: BirthDay, view: View, position: Int) {

            }
        })
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = birthDayAdapter
        f = Filtro(usuarioId, Util.getMonthCurrent(), 0, 0)
        itfViewModel.search.value = Gson().toJson(f)

        itfViewModel.setLoading(true)
        itfViewModel.syncBirthDay(usuarioId, Util.getMonthCurrent())

        itfViewModel.getBirthDays().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            birthDayAdapter.addItems(it)
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
//            closeLoad()
            Util.toastMensaje(requireContext(), it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
//            closeLoad()
            Util.toastMensaje(requireContext(), it)
        })
        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                birthDayAdapter.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun dialogSearch() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_filtro_birth_day, null)

        val editTextMes: TextInputEditText = v.findViewById(R.id.editTextMes)
        val fabSearch: ExtendedFloatingActionButton = v.findViewById(R.id.fabSearch)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        f = Filtro()

        editTextMes.setOnClickListener { spinnerDialog(editTextMes, f) }
        fabSearch.setOnClickListener {
            f = Filtro(usuarioId, f.cicloId, 0, 0)
            itfViewModel.search.value = Gson().toJson(f)
            itfViewModel.setLoading(true)
            itfViewModel.syncBirthDay(usuarioId, f.cicloId)
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
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        textViewTitulo.text = String.format("Mes")

        val comboMesAdapter = ComboMesAdapter(object : OnItemClickListener.MonthListener {
            override fun onItemClick(m: Month, view: View, position: Int) {
                f.cicloId = m.codigo
                input.setText(m.nombre)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = comboMesAdapter
        comboMesAdapter.addItems(Util.getMoths())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            BirthDayFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}