package com.equiposeis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.equiposeis.databinding.FragmentAdministradorCitasBinding
import android.widget.TextView
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

class AdministradorCFragment : Fragment(R.layout.fragment_administrador_citas) {

    private var _binding: FragmentAdministradorCitasBinding? = null
    private val binding get() = _binding!!

    data class Cita(
        val id: Int,
        val nombre: String,
        val sintoma: String
    )

    // Datos simulados (más adelante los obtendrás desde Room)

    private val citas = listOf(
        Triple("Luna", "Dolor estomacal", 1),
        Triple("Max", "Vómito y decaimiento", 2),
        Triple("Nala", "Fractura en pata trasera", 3)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministradorCitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Simulación de datos de prueba
        val listaCitas = listOf(
            Cita(1, "Max", "Fiebre y tos"),
            Cita(2, "Luna", "Dolor en la pata"),
            Cita(3, "Rocky", "Vómitos")
        )

        for (cita in listaCitas) {
            agregarCitaAVista(cita)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_administradorCFragment_to_CrearCitasFragment)
        }
    }

    // Función para agregar dinámicamente cada vista
    private fun agregarCitaAVista(cita: Cita) {
        val itemView = layoutInflater.inflate(R.layout.item_lista, binding.listaCitasContainer, false)

        val nombreMascota = itemView.findViewById<TextView>(R.id.nombreMascota)
        val sintomaMascota = itemView.findViewById<TextView>(R.id.sintomaMascota)
        val turnoMascota = itemView.findViewById<TextView>(R.id.turnoMascota)
        val imagenMascota = itemView.findViewById<ImageView>(R.id.imagenMascota)

        nombreMascota.text = cita.nombre
        sintomaMascota.text = cita.sintoma
        turnoMascota.text = "#${cita.id}"
        imagenMascota.setImageResource(R.drawable.perro_detalle_cita) // O el que tengas

        binding.listaCitasContainer.addView(itemView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
