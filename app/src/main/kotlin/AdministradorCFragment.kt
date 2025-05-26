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
import com.equiposeis.database.Pet
import com.equiposeis.databinding.FragmentAdministradorCitasBinding
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
        val petDao = AppDatabase.getDatabase(context).petDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val existingPets = petDao.getAllPets()

            /*
            if (existingPets.isEmpty()) {
                val nuevaMascota = Pet(
                    petName = "Firulais",
                    petBreed = "Labrador",
                    ownerName = "Carlos",
                    phoneNumber = "3012345678",
                    symptoms = "Cojea de la pata izquierda"
                )
                petDao.insert(nuevaMascota)
            }
            */

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
        imagenMascota.setImageResource(R.drawable.perro_detalle_cita)

        itemView.setOnClickListener {
            findNavController().navigate(R.id.action_administradorCFragment_to_detalleCitaFragment)
        }

        binding.listaCitasContainer.addView(itemView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}