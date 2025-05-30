package com.equiposeis
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.equiposeis.database.MyApplication
import com.equiposeis.database.Pet
import com.equiposeis.databinding.FragmentDetalleCitaBinding
import kotlinx.coroutines.launch

class DetalleCitaFragment : Fragment() {

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!
    private val dogApi = MyApplication.dogApiService
    private val petDao = MyApplication.database.petDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            try {
                val perro = petDao.getPetById(2)
                val razaPerro = binding.razaPerro
                val sintomaMascota = binding.sintomasPerro
                val turnoMascota = binding.turnoCita
                val nombreMascota = binding.nombrePerro
                val propietario = binding.propietarioPerro
                val telefonoPropietario = binding.telefonoPropietario

                nombreMascota.text = perro?.petName
                sintomaMascota.text = perro?.symptoms
                turnoMascota.text = "#${perro?.id}"
                razaPerro.text = perro?.petBreed
                propietario.text = perro?.ownerName
                telefonoPropietario.text = perro?.phoneNumber


                val PetBreed = perro?.petBreed
                val (breed, subBreed) = PetBreed!!.split(" ")

                val endpoint = if (subBreed.isNotEmpty()) {
                    "breed/$breed/$subBreed/images/random"
                } else {
                    "breed/$breed/images/random"
                }

                val image = dogApi.getRandomImageForBreed(endpoint)
                cargarimagen(image.message)
            } catch (e: Exception) {
                e.printStackTrace()
                // Show error message
            }
        }

        return binding.root
    }

    private fun cargarimagen(url:String) {
        Glide.with(requireContext())
            .load(url)
            .into(binding.imagePerro)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup the return button
        binding.btnVolverDetalleCita.setOnClickListener {
            findNavController().navigate(R.id.action_detalleCitaFragment_to_administradorCFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}