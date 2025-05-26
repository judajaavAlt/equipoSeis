package com.equiposeis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.equiposeis.databinding.FragmentEditarCitaBinding

class EditarCitaFragment: Fragment(R.layout.fragment_editar_cita)
{
    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup the return button
        binding.returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_editarCitaFragment_to_administradorCFragment)
        }
        //

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}