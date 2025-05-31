package com.equiposeis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.equiposeis.database.MyApplication
import com.equiposeis.databinding.FragmentEditarCitaBinding
import kotlinx.coroutines.launch

class EditarCitaFragment : Fragment(R.layout.fragment_editar_cita) {
    private var _binding: FragmentEditarCitaBinding? = null
    private val binding get() = _binding!!
    private val args: EditarCitaFragmentArgs by navArgs()

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

        val petDao = MyApplication.database.petDao()
        val petId = args.petId

        lifecycleScope.launch {
            val pet = petDao.getPetById(petId)
            if (pet != null) {
                binding.petsNameInput.setText(pet.petName)
                binding.breedAutoComplete.setText(pet.petBreed)
                binding.ownerNameInput.setText(pet.ownerName)
                binding.phoneInput.setText(pet.phoneNumber)
            } else {
                Toast.makeText(requireContext(), "Mascota no encontrada", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_editarCitaFragment_to_detalleCitaFragment)
        }

        // Autocompletado de razas desde la API
        val breed = binding.breedAutoComplete
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
                    breed.setAdapter(adapter)
                    breed.threshold = 2
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error cargando razas", Toast.LENGTH_SHORT).show()
            }
        }

        // Verificación de formulario
        binding.petsNameInput.addTextChangedListener { checkForm() }
        binding.breedAutoComplete.addTextChangedListener { checkForm() }
        binding.ownerNameInput.addTextChangedListener { checkForm() }
        binding.phoneInput.addTextChangedListener { checkForm() }
    }

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

    private fun checkForm() {
        lifecycleScope.launch {
            try {
                val response = MyApplication.dogApiService.getAllBreeds()
                if (response.status == "success") {
                    val breeds = parseDogBreeds(response.message)
                    val allFieldsFilled = listOf(
                        binding.petsNameInput.text?.toString()?.trim()?.isNotEmpty() == true,
                        binding.breedAutoComplete.text?.toString() in breeds,
                        binding.ownerNameInput.text?.toString()?.trim()?.isNotEmpty() == true,
                        binding.phoneInput.text?.toString()?.trim()?.isNotEmpty() == true,
                    ).all { it }
                    binding.guardarCitaButton.isEnabled = allFieldsFilled
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si falla la validación, desactiva el botón
                binding.guardarCitaButton.isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}