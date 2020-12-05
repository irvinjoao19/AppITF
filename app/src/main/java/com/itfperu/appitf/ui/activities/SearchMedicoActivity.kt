package com.itfperu.appitf.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.data.viewModel.TargetViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.adapters.CheckMedicoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_search_medico.*
import kotlinx.android.synthetic.main.activity_search_medico.toolbar
import javax.inject.Inject

class SearchMedicoActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: TargetViewModel
    private var cabId: Int = 0
    private var tipoTarget: String = "" // A -> ALTAS	B -> BAJAS
    private var usuarioId: Int = 0

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        search(searchView)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_medico)
        val b = intent.extras
        if (b != null) {
            cabId = b.getInt("targetCab")
            usuarioId = b.getInt("usuarioId")
            tipoTarget = b.getString("tipoTarget")!!
            bindUI()
        }
    }

    private fun bindUI() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Médicos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(TargetViewModel::class.java)

        val checkMedicoAdapter =
            CheckMedicoAdapter(object : OnItemClickListener.CheckMedicoListener {
                override fun onItemClick(m: Medico, p: Int, b: Boolean) {
                    m.isSelected = true
                    itfViewModel.updateCheckMedico(m)
                }
            })
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = checkMedicoAdapter
        itfViewModel.getCheckMedicos(tipoTarget, usuarioId)
            .observe(this, Observer(checkMedicoAdapter::submitList))

        itfViewModel.searchMedico.value = ""

        itfViewModel.mensajeError.observe(this, { s ->
            Util.toastMensaje(this, s)
        })
        itfViewModel.mensajeSuccess.observe(this, {
            if (it == "Ok") {
                itfViewModel.saveMedico(cabId, tipoTarget)
                return@observe
            }
            Util.toastMensaje(this, it)
            finish()
        })
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                itfViewModel.searchMedico.value = newText
                return true
            }
        })
    }
}