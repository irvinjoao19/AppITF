package com.itfperu.appitf.ui.fragments.edition

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetCab
import com.itfperu.appitf.data.local.model.TargetDet
import com.itfperu.appitf.data.viewModel.TargetViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import com.itfperu.appitf.ui.activities.SearchMedicoActivity
import com.itfperu.appitf.ui.activities.TargetActivity
import com.itfperu.appitf.ui.adapters.TargetDetAdapter
import com.itfperu.appitf.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_sol_medico.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class EditTargetFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabPerson -> startActivity(
                Intent(context, SearchMedicoActivity::class.java)
                    .putExtra("targetCab", targetId)
                    .putExtra("tipoTarget", tipoTarget)
            )
            R.id.fabSave -> formValidate()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: TargetViewModel
    lateinit var s: TargetCab
    private var usuarioId: Int = 0
    private var targetId: Int = 0 // cabecera
    private var tipoTarget: String = "" // A -> ALTAS	B -> BAJAS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = TargetCab()
        arguments?.let {
            targetId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
            tipoTarget = it.getString(ARG_PARAM3)!!
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
            TargetDetAdapter(object : OnItemClickListener.TargetDetListener {
                override fun onItemClick(t: TargetDet, view: View, position: Int) {
                    when (view.id) {
                        R.id.editTextCantidad -> updateCantidadProducto(t)
                        R.id.imageViewNegative -> {
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
                        R.id.imageViewPositive -> {
                            val sTotal = (t.nroContacto + 1).toString()
                            val nPositive = sTotal.toInt()
                            t.active = 1
                            t.nroContacto = nPositive
                            itfViewModel.insertTargetDet(t, 1)
                        }
                    }
                }
            })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = targetDetAdapter

        itfViewModel.getTargetDetById(targetId).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                fabSave.visibility = View.VISIBLE
            } else
                fabSave.visibility = View.GONE

            targetDetAdapter.addItems(it)
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

    private fun confirmDelete(m: Target) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage("Deseas eliminar este medico ?")
            .setPositiveButton("SI") { dialog, _ ->
//                itfViewModel.deleteTarget(m)
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        dialog.show()
    }

    private fun formValidate() {
        s.targetCabId = targetId
        s.usuarioId = usuarioId
        s.tipoTarget = tipoTarget
        s.fechaSolicitud = Util.getFecha()
        s.estado = 16
        s.active = 1
        itfViewModel.validateTarget(s)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: String) =
            EditTargetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
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
}