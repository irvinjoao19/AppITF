package com.itfperu.appitf.ui.fragments.edition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.data.local.model.SolMedico
import com.itfperu.appitf.data.viewModel.MedicoViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.MedicoActivity
import com.itfperu.appitf.ui.adapters.MedicoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_sol_medico.*
import kotlinx.android.synthetic.main.fragment_edit_sol_medico.recyclerView
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class EditSolMedicoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabPerson -> startActivity(
                Intent(context, MedicoActivity::class.java)
                    .putExtra("usuarioId", usuarioId)
                    .putExtra("solMedicoId", solMedicoId)
                    .putExtra("medicoId", medicoId)
                    .putExtra("title", "Nuevo Medico")
                    .putExtra("tipoMedico", tipoMedico)
            )
            R.id.fabSave -> formValidate()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: MedicoViewModel
    lateinit var s: SolMedico
    private var usuarioId: Int = 0
    private var solMedicoId: Int = 0
    private var medicoId: Int = 0
    private var tipoMedico: Int = 0 // 1 -> actividades , 2 -> aprobadas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = SolMedico()
        arguments?.let {
            solMedicoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
            tipoMedico = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_sol_medico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(MedicoViewModel::class.java)

        val medicoAdapter =
            MedicoAdapter(object : OnItemClickListener.MedicoListener {
                override fun onItemClick(m: Medico, view: View, position: Int) {
                    when (view.id) {
                        R.id.imgEdit -> startActivity(
                            Intent(context, MedicoActivity::class.java)
                                .putExtra("usuarioId", m.usuarioId)
                                .putExtra("solMedicoId", m.medicoSolId)
                                .putExtra("medicoId", m.medicoId)
                                .putExtra("title", "Editar Medico")
                                .putExtra("tipoMedico", tipoMedico)
                        )
                        R.id.imgDelete -> confirmDelete(m)
                    }
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = medicoAdapter

        itfViewModel.getMedicosById(solMedicoId).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                fabSave.visibility = View.VISIBLE
            } else
                fabSave.visibility = View.GONE

            medicoAdapter.addItems(it)
        })

        itfViewModel.getMedicoId().observe(viewLifecycleOwner, {
            medicoId = if (it == null || it == 0) 1 else it + 1
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            activity!!.finish()
            Util.toastMensaje(context!!, it)
        })

        fabPerson.setOnClickListener(this)
        fabSave.setOnClickListener(this)
    }

    private fun confirmDelete(m: Medico) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage("Deseas eliminar este medico ?")
            .setPositiveButton("SI") { dialog, _ ->
                itfViewModel.deleteMedico(m)
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun formValidate() {
        s.solMedicoId = solMedicoId
        s.estadoSol = 11
        s.estado = 1
        s.usuarioId = usuarioId
        s.fecha = Util.getFecha()
        s.tipo = tipoMedico
        itfViewModel.validateSolMedico(s)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: Int) =
            EditSolMedicoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }
}