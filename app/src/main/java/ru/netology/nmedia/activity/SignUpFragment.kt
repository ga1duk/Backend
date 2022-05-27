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
            viewModel.createUser(
                binding.etName.text.toString(),
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString(),
                binding.etConfirmPassword.text.toString()
            )
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.unknownError) {
                showSnackBar(binding,R.string.error_loading)
            } else if (state.emptyFieldsError) {
                showSnackBar(binding, R.string.error_empty_text_fields)
            } else if (state.networkError) {
                showSnackBar(binding, R.string.error_check_network_connection)
            } else if (state.loginOrPassError) {
                showSnackBar(binding, R.string.error_login_is_occupied)
            } else if (state.passwordsNotMatchError) {
                binding.tfConfirmPassword.error = getString(R.string.error_passwords_must_match)
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

    private fun showSnackBar(binding: FragmentSignUpBinding, message: Int) {
        Snackbar.make(
            binding.root,
            getString(message),
            Snackbar.LENGTH_LONG
        )
            .show()
    }
}