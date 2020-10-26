package com.itfperu.appitf.ui.fragments.edition

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Medico
import com.itfperu.appitf.data.local.model.MedicoDireccion
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
import kotlinx.android.synthetic.main.fragment_general.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

class EditSolMedicoFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAprobar -> confirmAprobation(13, "Aprobada", "Aprobar")
            R.id.fabRechazar -> confirmAprobation(12, "Rechazada", "Rechazar")
            R.id.fabObservar -> confirmAprobation(14, "Observado", "Observar")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: MedicoViewModel

    private var usuarioId: Int = 0
    private var solMedicoId: Int = 0
    private var medicoId: Int = 0
    private var tipoMedico: Int = 0 // 1 -> actividades , 2 -> aprobadas
    private var estado: Int = 0
    lateinit var s: SolMedico

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = SolMedico()
        arguments?.let {
            solMedicoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
            tipoMedico = it.getInt(ARG_PARAM3)
            estado = it.getInt(ARG_PARAM4)
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
                                .putExtra("estado", if (tipoMedico == 2) 100 else estado)
                        )
                    }
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = medicoAdapter

        itfViewModel.getMedicosById(solMedicoId).observe(viewLifecycleOwner, {
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
        itfViewModel.getSolMedicoCab(solMedicoId).observe(viewLifecycleOwner, {
            if (it != null) {
                s = it
            }
        })

        if (tipoMedico == 1) {
            fabMenu.visibility = View.GONE
        }

        if (estado == 12 || estado == 13) {
            fabMenu.visibility = View.GONE
        }

        fabAprobar.setOnClickListener(this)
        fabRechazar.setOnClickListener(this)
        fabObservar.setOnClickListener(this)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: Int, param4: Int) =
            EditSolMedicoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                    putInt(ARG_PARAM4, param4)
                }
            }
    }

    private fun confirmAprobation(id: Int, estado: String, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null)
        val layoutTitle: TextView = v.findViewById(R.id.layoutTitle)
        val editTextComentario: TextInputEditText = v.findViewById(R.id.editTextComentario)
        val fabAccept: ExtendedFloatingActionButton = v.findViewById(R.id.fabAccept)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        layoutTitle.text = title

        fabAccept.setOnClickListener {
            s.solMedicoId = solMedicoId
            s.estadoSol = id
            s.descripcionEstado = estado
            s.estado = 1
            s.mensajeSol = editTextComentario.text.toString()
            s.usuarioId = usuarioId
            s.fecha = Util.getFecha()
            s.fechaInicio = Util.getFirstDay()
            s.fechaFinal = Util.getLastaDay()
            s.tipo = tipoMedico
            itfViewModel.validateSolMedico(s)
            dialog.dismiss()
        }
    }
}