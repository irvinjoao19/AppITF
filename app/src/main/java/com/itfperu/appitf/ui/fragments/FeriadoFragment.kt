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
import com.itfperu.appitf.data.local.model.Feriado
import com.itfperu.appitf.data.viewModel.FeriadoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.FeriadoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_feriado.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class FeriadoFragment : DaggerFragment(), SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: FeriadoViewModel
    lateinit var adapter: FeriadoAdapter
    private var usuarioId: Int = 0
    private var tipo: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feriado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(FeriadoViewModel::class.java)

        adapter = FeriadoAdapter(object : OnItemClickListener.FeriadoListener {
            override fun onItemClick(f: Feriado, view: View, position: Int) {
                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra("title", "Modificar Feriado")
                        .putExtra("tipo", tipo)
                        .putExtra("id", f.feriadoId)
                        .putExtra("usuarioId", usuarioId)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        itfViewModel.setLoading(true)
        itfViewModel.syncFeriado()

        refreshLayout.setOnRefreshListener(this)
        fab.setOnClickListener(this)

        itfViewModel.getFeriados().observe(viewLifecycleOwner, {
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
                textviewMessage.visibility = View.VISIBLE
            }
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            FeriadoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    override fun onRefresh() {
        itfViewModel.setLoading(false)
        itfViewModel.syncFeriado()
    }

    override fun onClick(v: View) {
        startActivity(
            Intent(context, FormActivity::class.java)
                .putExtra("title", "Nuevo Feriado")
                .putExtra("tipo", tipo)
                .putExtra("id", 0)
                .putExtra("usuarioId", usuarioId)
        )
    }
}