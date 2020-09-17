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
import com.itfperu.appitf.data.local.model.Moneda
import com.itfperu.appitf.data.viewModel.MonedaViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.MonedaAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_moneda.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

class MonedaFragment : DaggerFragment(), SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: MonedaViewModel
    lateinit var monedaAdapter: MonedaAdapter
    private var usuarioId: Int = 0
    private var tipo: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usuarioId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_moneda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(MonedaViewModel::class.java)

        monedaAdapter = MonedaAdapter(object : OnItemClickListener.MonedaListener {
            override fun onItemClick(m: Moneda, view: View, position: Int) {
                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra("title", "Modificar Moneda")
                        .putExtra("tipo", tipo)
                        .putExtra("id", m.monedaId)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = monedaAdapter

        itfViewModel.setLoading(true)
        itfViewModel.syncMoneda()

        refreshLayout.setOnRefreshListener(this)
        fab.setOnClickListener(this)

        itfViewModel.getMonedas().observe(viewLifecycleOwner, {
            refreshLayout.isRefreshing = false
            monedaAdapter.addItems(it)
        })

        itfViewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                monedaAdapter.addItems(listOfNotNull())
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
            MonedaFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }

    override fun onRefresh() {
        itfViewModel.setLoading(false)
        itfViewModel.syncMoneda()
    }

    override fun onClick(v: View) {
        startActivity(
            Intent(context, FormActivity::class.java)
                .putExtra("title", "Nueva Categoria")
                .putExtra("tipo", tipo)
                .putExtra("id", 0)
                .putExtra("usuarioId", usuarioId)
        )
    }
}