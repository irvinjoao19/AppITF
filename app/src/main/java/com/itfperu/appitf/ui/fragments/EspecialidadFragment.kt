package com.itfperu.appitf.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Especialidad
import com.itfperu.appitf.data.viewModel.EspecialidadViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.EspecialidadAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_categoria.*
import kotlinx.android.synthetic.main.fragment_especialidad.*
import kotlinx.android.synthetic.main.fragment_especialidad.fab
import kotlinx.android.synthetic.main.fragment_especialidad.progressBar
import kotlinx.android.synthetic.main.fragment_especialidad.recyclerView
import kotlinx.android.synthetic.main.fragment_especialidad.refreshLayout
import kotlinx.android.synthetic.main.fragment_especialidad.textviewMessage
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class EspecialidadFragment : DaggerFragment(), SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: EspecialidadViewModel
    lateinit var adapter: EspecialidadAdapter
    private var usuarioId: Int = 0
    private var tipo: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_especialidad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(EspecialidadViewModel::class.java)

        adapter = EspecialidadAdapter(object : OnItemClickListener.EspecialidadListener {
            override fun onItemClick(f: Especialidad, view: View, position: Int) {
                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra("title", "Modificar Especialidad")
                        .putExtra("tipo", tipo)
                        .putExtra("id", f.especialidadId)
                        .putExtra("usuarioId", usuarioId)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        itfViewModel.setLoading(true)
        itfViewModel.syncEspecialidad()

        refreshLayout.setOnRefreshListener(this)
        fab.setOnClickListener(this)

        itfViewModel.getEspecialidads().observe(viewLifecycleOwner, {
            textviewMessage.text = String.format("Se encontraron %s registros", it.size)
            refreshLayout.isRefreshing = false
            adapter.addItems(it)
        })

        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                adapter.addItems(listOfNotNull())
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            EspecialidadFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    override fun onRefresh() {
        itfViewModel.setLoading(false)
        itfViewModel.syncEspecialidad()
    }

    override fun onClick(v: View) {
        startActivity(
            Intent(context, FormActivity::class.java)
                .putExtra("title", "Nueva Especialidad")
                .putExtra("tipo", tipo)
                .putExtra("id", 0)
                .putExtra("usuarioId", usuarioId)
        )
    }
}