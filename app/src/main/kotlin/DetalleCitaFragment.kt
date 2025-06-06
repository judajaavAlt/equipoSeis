package com.equiposeis
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.equiposeis.database.MyApplication
import com.equiposeis.database.Pet
import com.equiposeis.databinding.FragmentDetalleCitaBinding
import kotlinx.coroutines.launch

class DetalleCitaFragment : Fragment() {

    private var _binding: FragmentDetalleCitaBinding? = null
    private val binding get() = _binding!!
    private val dogApi = MyApplication.dogApiService
    private val args: DetalleCitaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun cargarimagen(url:String) {
        Glide.with(requireContext())
            .load(url)
            .into(binding.imagePerro)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petDao = MyApplication.database.petDao()
        val petId = args.petId

        lifecycleScope.launch {
            try {
                val perro = petDao.getPetById(petId)
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


                val fullBreed = perro?.petBreed!!.split(" ")
                val breed = fullBreed[0]
                val subBreed = if (fullBreed.size > 1) fullBreed[1] else ""

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

        // Setup the return button
        binding.btnVolverDetalleCita.setOnClickListener {
            findNavController().navigate(R.id.action_detalleCitaFragment_to_administradorCFragment)
        }
        binding.BtnEditarCita.setOnClickListener {
            val action = DetalleCitaFragmentDirections
                .actionDetalleCitaFragmentToEditarCitaFragment(petId)
            findNavController().navigate(action)
        }

        binding.BtnBorrarCita.setOnClickListener {
            lifecycleScope.launch {
                val perro = petDao.getPetById(petId)
                petDao.delete(perro!!.copy())
                viewModel.triggerListRefresh()
            }
            findNavController().navigate(R.id.action_detalleCitaFragment_to_administradorCFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}