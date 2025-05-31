package com.equiposeis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.equiposeis.database.AppDatabase
import com.equiposeis.database.MyApplication
import com.equiposeis.database.Pet
import com.equiposeis.databinding.FragmentAdministradorCitasBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdministradorCFragment : Fragment(R.layout.fragment_administrador_citas) {

    private var _binding: FragmentAdministradorCitasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministradorCitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext().applicationContext
        val petDao = MyApplication.database.petDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val existingPets = petDao.getAllPets()
            val listaCitas = petDao.getAllPets()

            withContext(Dispatchers.Main) {
                Log.d("ROOM_TEST", "Mascotas cargadas: ${listaCitas.map { it.petName }}")
                binding.listaCitasContainer.removeAllViews()
                listaCitas.forEach { pet ->
                    agregarCitaAVista(pet)
                }
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_administradorCFragment_to_CrearCitasFragment)
        }
    }

    private fun agregarCitaAVista(pet: Pet) {
        val itemView = layoutInflater.inflate(R.layout.item_lista, binding.listaCitasContainer, false)

        val nombreMascota = itemView.findViewById<TextView>(R.id.nombreMascota)
        val sintomaMascota = itemView.findViewById<TextView>(R.id.sintomaMascota)
        val turnoMascota = itemView.findViewById<TextView>(R.id.turnoMascota)
        val imagenMascota = itemView.findViewById<ImageView>(R.id.imagenMascota)

        nombreMascota.text = pet.petName
        sintomaMascota.text = pet.symptoms
        turnoMascota.text = "#${pet.id}"

        // Cargar imagen según la raza usando Dog CEO API
        val breedUrl = buildBreedImageUrl(pet.petBreed)

        lifecycleScope.launch {
            try {
                val response = MyApplication.dogApiService.getRandomImageForBreed(breedUrl)
                if (response.status == "success") {
                    val imageUrl = response.message
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .into(imagenMascota)
                } else {
                    imagenMascota.setImageResource(R.drawable.perro_detalle_cita) // fallback
                }
            } catch (e: Exception) {
                imagenMascota.setImageResource(R.drawable.perro_detalle_cita) // fallback
                e.printStackTrace()
            }
        }

        itemView.setOnClickListener {
            val action = AdministradorCFragmentDirections
                .actionAdministradorCFragmentToEditarCitaFragment(pet.id)
            findNavController().navigate(action)
        }
//            val action = AdministradorCFragmentDirections
//                .actionAdministradorCFragmentToDetalleCitaFragment(pet.id)
//            findNavController().navigate(action)        }

        binding.listaCitasContainer.addView(itemView)
    }

    // Convierte "labrador retriever" → "retriever/labrador" o "labrador" según API
    private fun buildBreedImageUrl(breedInput: String): String {
        val fullBreed = breedInput.split(" ")
        val breed = fullBreed[0]
        val subBreed = if (fullBreed.size > 1) fullBreed[1] else ""

        return if (subBreed.isNotEmpty()) {
            "breed/$breed/$subBreed/images/random"
        } else {
            "breed/$breed/images/random"
        }
    }
}