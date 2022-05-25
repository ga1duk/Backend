package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.networkError) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .show()
            } else if (state.emptyFieldsError) {
                Snackbar.make(
                    binding.root,
                    R.string.error_empty_text_fields,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else {
                findNavController().navigateUp()
                Toast.makeText(
                    requireActivity(),
                    R.string.toast_text_successful_register,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }
}