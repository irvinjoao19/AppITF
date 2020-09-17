package com.itfperu.appitf.ui.fragments.edition

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.Moneda
import com.itfperu.appitf.data.viewModel.MonedaViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_moneda.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditMonedaFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabGenerate -> formMoneda()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: MonedaViewModel
    lateinit var p: Moneda
    private var usuarioId: Int = 0
    private var monedaId: Int = 0
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        p = Moneda()
        arguments?.let {
            monedaId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_moneda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(MonedaViewModel::class.java)

        itfViewModel.getMonedaById(monedaId).observe(viewLifecycleOwner, {
            if (it != null) {
                p = it
                editTextCodigo.setText(it.codigo)
                editTextCodigo.isEnabled = false
                editTextNombre.setText(it.descripcion)
                editTextSimbolo.setText(it.simbolo)
                editTextEstado.setText(it.estado)
            }
        })

        fabGenerate.setOnClickListener(this)

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
        })

        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            closeLoad()
            Util.toastMensaje(context!!, it)
            activity!!.finish()
        })
    }

    private fun formMoneda() {
        p.codigo = editTextCodigo.text.toString()
        p.descripcion = editTextNombre.text.toString()
        p.simbolo = editTextSimbolo.text.toString()
        p.estado = editTextEstado.text.toString()
        p.usuarioId = usuarioId
        load()
        itfViewModel.validateMoneda(p)
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textView)
        textViewTitle.text = String.format("Enviando...")
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun closeLoad() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditMonedaFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}