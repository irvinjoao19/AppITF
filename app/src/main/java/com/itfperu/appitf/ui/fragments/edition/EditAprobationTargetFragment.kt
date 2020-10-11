package com.itfperu.appitf.ui.fragments.edition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itfperu.appitf.R
import com.itfperu.appitf.data.local.model.TargetCab
import com.itfperu.appitf.data.viewModel.TargetViewModel
import com.itfperu.appitf.data.viewModel.ViewModelFactory
import com.itfperu.appitf.helper.Util
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_sol_medico.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class EditAprobationTargetFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
//            R.id.fabPerson -> startActivity(
//                Intent(context, TargetActivity::class.java)
//                    .putExtra("usuarioId", usuarioId)
//                    .putExtra("solTargetId", solTargetId)
//                    .putExtra("medicoId", medicoId)
//                    .putExtra("title", "Nuevo Target")
//            )
            R.id.fabSave -> formValidate()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var itfViewModel: TargetViewModel
    lateinit var s: TargetCab
    private var usuarioId: Int = 0
    private var solTargetId: Int = 0
    private var medicoId: Int = 0
    private var tipoTarget: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s = TargetCab()
        arguments?.let {
            solTargetId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
            tipoTarget = it.getString(ARG_PARAM3)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_aprobation_target, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        itfViewModel =
            ViewModelProvider(this, viewModelFactory).get(TargetViewModel::class.java)

//        val medicoAdapter =
//            ComboTargetAdapter(object : OnItemClickListener.TargetListener {
//                override fun onItemClick(m: Target, view: View, position: Int) {
//                    when (view.id) {
//                        R.id.imgEdit -> startActivity(
//                            Intent(context, TargetActivity::class.java)
//                                .putExtra("usuarioId", m.usuarioId)
//                                .putExtra("solTargetId", m.medicoSolId)
//                                .putExtra("medicoId", m.medicoId)
//                                .putExtra("title", "Editar Target")
//                        )
//                        R.id.imgDelete -> confirmDelete(m)
//                    }
//                }
//            })
//
//        recyclerView.itemAnimator = DefaultItemAnimator()
//        recyclerView.layoutManager = LinearLayoutManager(context!!)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = medicoAdapter

//        itfViewModel.getTargetsById(solTargetId).observe(viewLifecycleOwner, {
//            if (it.isNotEmpty()) {
//                fabSave.visibility = View.VISIBLE
//            } else
//                fabSave.visibility = View.GONE
//
//            medicoAdapter.addItems(it)
//        })

//        itfViewModel.getTargetId().observe(viewLifecycleOwner, {
//            medicoId = if (it == null || it == 0) 1 else it + 1
//        })

        itfViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it)
        })
        itfViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
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
//        s.solTargetId = solTargetId
//        s.estadoSol = 11
//        s.usuarioId = usuarioId
        itfViewModel.validateTarget(s)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: String) =
            EditAprobationTargetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}