package com.equiposeis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.equiposeis.databinding.FragmentEditarCitaBinding
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.equiposeis.database.MyApplication
import com.equiposeis.database.Pet
import com.equiposeis.database.PetDao
import kotlinx.coroutines.launch


class EditarCitaFragment : Fragment(R.layout.fragment_editar_cita) {
    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!

    // Función para parsear razas (igual que en CrearCitaFragment)
    private fun parseDogBreeds(breedsMap: Map<String, List<String>>): List<String> {
        val breedsList = mutableListOf<String>()
        for ((breed, subBreeds) in breedsMap) {
            if (subBreeds.isEmpty()) {
                breedsList.add(breed.lowercase())
            } else {
                for (subBreed in subBreeds) {
                    breedsList.add("$breed ${subBreed.lowercase()}")
                }
            }
        }
        return breedsList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBreedAutoComplete()
    }

    private fun setupBreedAutoComplete() {
        val breedAutoComplete = binding.breedAutoComplete

        lifecycleScope.launch {
            try {
                val response = MyApplication.dogApiService.getAllBreeds()
                if (response.status == "success") {
                    val breeds = parseDogBreeds(response.message)
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        breeds
                    )
                    breedAutoComplete.setAdapter(adapter)
                    breedAutoComplete.threshold = 2 // Mostrar sugerencias tras 2 letras
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}