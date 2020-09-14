package com.itfperu.appitf.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Moneda
import com.itfperu.appitf.data.local.model.Perfil
import com.itfperu.appitf.data.viewModel.ITFViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.FormActivity
import com.itfperu.appitf.ui.adapters.MonedaAdapter
import com.itfperu.appitf.ui.adapters.PerfilAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_perfil.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MonedaFragment : DaggerFragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: ITFViewModel
    lateinit var monedaAdapter: MonedaAdapter

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
        return inflater.inflate(R.layout.fragment_moneda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(ITFViewModel::class.java)

        monedaAdapter = MonedaAdapter(object : OnItemClickListener.MonedaListener {
            override fun onItemClick(m: Moneda, view: View, position: Int) {
                startActivity(
                    Intent(context, FormActivity::class.java)
                        .putExtra("title", "Modificar Moneda")
                        .putExtra("tipo", 2)
                        .putExtra("id", m.monedaId)
                )
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = monedaAdapter

        itfViewModel.setLoading(true)
        itfViewModel.syncMonedas()

        refreshLayout.setOnRefreshListener(this)

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
        fun newInstance(param1: String, param2: String) =
            MonedaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onRefresh() {
        itfViewModel.setLoading(false)
        itfViewModel.syncMonedas()
    }
}