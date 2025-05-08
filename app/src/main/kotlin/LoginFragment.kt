package com.equiposeis


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.equiposeis.databinding.FragmentLoginBinding
import java.util.concurrent.Executor

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(
            requireActivity()
            ,executor,
            object: BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    findNavController().navigate(R.id.action_loginFragment_to_AdministradorCFragment)
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biometrica")
            .setSubtitle("Escanea tu huella para continuar")
            .setNegativeButtonText("Cancelar")
            .build()

        binding.fingerprintLogin.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}



