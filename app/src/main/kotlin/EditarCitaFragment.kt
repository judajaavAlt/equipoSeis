package com.equiposeis

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
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

        // Cargar datos de la mascota
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

        // Botón de regreso
        binding.returnButton.setOnClickListener {
            val action = EditarCitaFragmentDirections
                .actionEditarCitaFragmentToDetalleCitaFragment(args.petId)
            findNavController().navigate(action)
        }

        // Autocompletado de razas
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

        // Configurar el botón de guardar
        binding.guardarCitaButton.setOnClickListener {
            guardarCambios()
        }

        // Verificación de formulario en tiempo real
        binding.petsNameInput.addTextChangedListener { checkForm() }
        binding.breedAutoComplete.addTextChangedListener { checkForm() }
        binding.ownerNameInput.addTextChangedListener { checkForm() }
        binding.phoneInput.addTextChangedListener { checkForm() }

        // Inicialmente deshabilitar el botón
        updateButtonState(false)
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
                        binding.phoneInput.text?.toString()?.trim()?.isNotEmpty() == true
                    ).all { it }

                    updateButtonState(allFieldsFilled)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                updateButtonState(false)
            }
        }
    }

    private fun updateButtonState(isEnabled: Boolean) {
        binding.guardarCitaButton.isEnabled = isEnabled
        if (isEnabled) {
            binding.guardarCitaButton.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            binding.guardarCitaButton.setTypeface(null, Typeface.BOLD)
        } else {
            binding.guardarCitaButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.guardarCitaButton.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun guardarCambios() {
        lifecycleScope.launch {
            try {
                val petId = args.petId
                val pet = MyApplication.database.petDao().getPetById(petId)

                if (pet != null) {
                    val updatedPet = pet.copy(
                        petName = binding.petsNameInput.text.toString(),
                        petBreed = binding.breedAutoComplete.text.toString(),
                        ownerName = binding.ownerNameInput.text.toString(),
                        phoneNumber = binding.phoneInput.text.toString()
                    )

                    MyApplication.database.petDao().update(updatedPet)
                    Toast.makeText(requireContext(), "Cita actualizada correctamente", Toast.LENGTH_SHORT).show()

                    val action = EditarCitaFragmentDirections
                        .actionEditarCitaFragmentToDetalleCitaFragment(petId)
                    findNavController().navigate(action)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}