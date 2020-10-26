package com.itfperu.appitf.ui.fragments.edition

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetCab
import com.itfperu.appitf.data.local.model.TargetDet
import com.itfperu.appitf.data.viewModel.TargetViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.SearchMedicoActivity
import com.itfperu.appitf.ui.adapters.TargetDetAdapter
import com.itfperu.appitf.ui.adapters.TargetInfoAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_target.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"

class EditTargetFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabPerson -> startActivity(
                Intent(context, SearchMedicoActivity::class.java)
                    .putExtra("targetCab", targetId)
                    .putExtra("usuarioId", usuarioId)
                    .putExtra("tipoTarget", tipoTarget)
            )
            R.id.fabSave -> formValidate()
            R.id.fabCancelar -> clearDetalle()
        }
    }

    private fun clearDetalle() {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage("Deseas Cancelar Operación ?")
            .setPositiveButton("Si") { dialog, _ ->
                itfViewModel.deleteTargetDet(targetId)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: TargetViewModel
    lateinit var s: TargetCab
    private var usuarioId: Int = 0
    private var targetId: Int = 0 // cabecera
    private var tipo: Int = 0 // 1 -> altas , 2 -> aprobacion de altas
    private var tipoTarget: String = "" // A -> ALTAS	B -> BAJAS
    private var estado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = TargetCab()
        arguments?.let {
            targetId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
            tipoTarget = it.getString(ARG_PARAM3)!!
            tipo = it.getInt(ARG_PARAM4)
            estado = it.getInt(ARG_PARAM5)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_target, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(TargetViewModel::class.java)

        val targetDetAdapter =
            TargetDetAdapter(tipo, object : OnItemClickListener.TargetDetListener {
                override fun onItemClick(t: TargetDet, view: View, position: Int) {
                    when (view.id) {
                        R.id.editTextCantidad -> updateCantidadProducto(t)
                        R.id.imgNegative -> {
                            val resta = t.nroContacto
                            if (resta != 0) {
                                val rTotal = (resta - 1).toString()
                                val nNegative = rTotal.toInt()
                                if (nNegative == 0) {
                                    t.active = 0
                                }
                                t.nroContacto = nNegative
                                itfViewModel.insertTargetDet(t, 1)
                            }
                        }
                        R.id.imgPositive -> {
                            val sTotal = (t.nroContacto + 1).toString()
                            val nPositive = sTotal.toInt()
                            t.active = 1
                            t.nroContacto = nPositive
                            itfViewModel.insertTargetDet(t, 1)
                        }
                        R.id.imgInfo -> dialogInfo(t)
                        R.id.imgAprobar -> confirmAprobation(t, 18, "Deseas Aprobar ?")
                        R.id.imgRechazar -> confirmAprobation(t, 17, "Deseas Rechazar ?")
                    }
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = targetDetAdapter

        itfViewModel.getTargetDetById(targetId).observe(viewLifecycleOwner, {
            if (estado == 17 || estado == 18) {
                fabMenu.visibility = View.GONE
            } else {
                if (tipo != 2) {
                    if (it.isNotEmpty()) {
                        fabCancelar.visibility = View.VISIBLE
                        fabSave.visibility = View.VISIBLE
                    } else{
                        fabCancelar.visibility = View.GONE
                        fabSave.visibility = View.GONE
                    }
                }
            }
            targetDetAdapter.addItems(it)
        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            activity!!.finish()
            Util.toastMensaje(context!!, it)
        })

        itfViewModel.getTarget(targetId).observe(viewLifecycleOwner, {
            if (it != null) {
                s = it
            }
        })

        if (tipo == 2) {
            fabPerson.visibility = View.GONE
            fabSave.visibility = View.GONE
            fabMenu.visibility = View.GONE
        }

        fabPerson.setOnClickListener(this)
        fabSave.setOnClickListener(this)
        fabCancelar.setOnClickListener(this)
    }

    private fun formValidate() {
        s.targetCabId = targetId
        s.usuarioId = usuarioId
        s.tipoTarget = tipoTarget
        s.tipo = tipo
        s.nombreEstado = "Enviada"
        s.fechaSolicitud = Util.getFecha()
        s.fechaInicio = Util.getFirstDay()
        s.fechaFinal = Util.getLastaDay()
        s.estado = 16
        s.active = 1
        s.usuarioId = usuarioId
        itfViewModel.validateTarget(s)
    }

    companion object {
        @JvmStatic
        fun newInstance(p1: Int, p2: Int, p3: String, p4: Int, p5: Int) =
            EditTargetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, p1)
                    putInt(ARG_PARAM2, p2)
                    putString(ARG_PARAM3, p3)
                    putInt(ARG_PARAM4, p4)
                    putInt(ARG_PARAM5, p5)
                }
            }
    }

    private fun updateCantidadProducto(p: TargetDet) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_count_contacto, null)
        val editTextProducto: EditText = v.findViewById(R.id.editTextProducto)
        val buttonCancelar: MaterialButton = v.findViewById(R.id.buttonCancelar)
        val buttonAceptar: MaterialButton = v.findViewById(R.id.buttonAceptar)
        //editTextProducto.setText(p.cantidad.toString())
        Util.showKeyboard(editTextProducto, context!!)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        buttonAceptar.setOnClickListener {
            if (editTextProducto.text.toString().isNotEmpty()) {
                val nPositive = editTextProducto.text.toString().toInt()
                if (nPositive == 0)
                    p.active = 0
                else
                    p.active = 1

                p.nroContacto = nPositive
                itfViewModel.insertTargetDet(p, 1)
                Util.hideKeyboardFrom(context!!, v)
                dialog.dismiss()
            } else {
                itfViewModel.setError("Digite cantidad")
            }
        }
        buttonCancelar.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun confirmAprobation(t: TargetDet, estado: Int, message: String) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage(message)
            .setPositiveButton("Si") { dialog, _ ->
                t.estadoTarget = estado
                t.active = 1
                itfViewModel.updateEstadoTargetDet(t)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun dialogInfo(t: TargetDet) {
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
        textViewTitulo.text = String.format("Información")
        val targetAdapter = TargetInfoAdapter()
        recyclerView.adapter = targetAdapter

        itfViewModel.getTargetInfo(t.targetDetId).observe(viewLifecycleOwner, {
            targetAdapter.addItems(it)
        })
    }
}