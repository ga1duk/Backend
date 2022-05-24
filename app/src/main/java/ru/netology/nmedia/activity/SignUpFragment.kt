package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentSignUpBinding
import ru.netology.nmedia.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
                binding.tfConfirmPassword.error = getString(R.string.error_passwords_must_match)
                return@setOnClickListener
            }
            viewModel.createUser(
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString(),
                binding.etName.text.toString()
            )
            findNavController().navigateUp()
        }

        return binding.root
    }
}