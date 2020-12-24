package com.itfperu.appitf.ui.fragments.edition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.NuevaDireccion
import com.itfperu.appitf.data.viewModel.DireccionViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_nuevas_direcciones_a.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditNuevasDireccionesAFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabAprobar -> formNuevaDireccion(28, "Aprobada")
            R.id.fabRechazar -> formNuevaDireccion(27, "Rechazada")
            R.id.fabObservar -> formNuevaDireccion(29, "Observado")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: DireccionViewModel
    lateinit var d: NuevaDireccion
    private var solMedicoId: Int = 0
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d = NuevaDireccion()
        arguments?.let {
            solMedicoId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_nuevas_direcciones_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(DireccionViewModel::class.java)

        itfViewModel.getNuevaDireccionId(solMedicoId).observe(viewLifecycleOwner, {
            if (it != null) {
                d = it
                editTextMedico.setText(it.nombreMedico)
                editTextDepartamento.setText(it.nombreDepartamento)
                editTextProvincia.setText(it.nombreProvincia)
                editTextDistrito.setText(it.nombreDistrito)
                editTextDireccion.setText(it.nombreDireccion)
                editTextInstitucion.setText(it.nombreInstitucion)
                editTextReferencia.setText(it.referencia)
                editTextObservacion.setText(it.observacion)

                if (it.estadoId == 28 || it.estadoId == 27 || it.estadoId == 29) {
                    fabAprobar.visibility = View.GONE
                    fabRechazar.visibility = View.GONE
                    fabObservar.visibility = View.GONE
                }
            }
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            if (it == "Guardado"){
                Util.executeDireccionWork(requireContext(),2)
            }
            Util.toastMensaje(requireContext(), it)
            requireActivity().finish()
        })
        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(requireContext(), it)
        })

        fabAprobar.setOnClickListener(this)
        fabRechazar.setOnClickListener(this)
        fabObservar.setOnClickListener(this)
    }


    private fun formNuevaDireccion(e: Int, descripcion: String) {
        d.usuarioId = usuarioId
        d.estadoId = e
        d.descripcionEstado = descripcion
        d.tipo = 2
        d.active = 1
        d.fechaSolicitud = Util.getFecha()
        d.nombreDireccion = editTextDireccion.text.toString()
        d.nombreInstitucion = editTextInstitucion.text.toString()
        d.referencia = editTextReferencia.text.toString()
        d.observacion = editTextObservacion.text.toString()
        itfViewModel.validateNuevaDireccion(d)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            EditNuevasDireccionesAFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}