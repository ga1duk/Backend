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
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.viewmodel.SignInViewModel

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.btnSignIn.setOnClickListener {
            viewModel.updateUser(
                binding.etLogin.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.unknownError) {
                Snackbar.make(
                    binding.root,
                    R.string.error_loading,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (state.emptyFieldsError) {
                Snackbar.make(
                    binding.root,
                    R.string.error_empty_login_or_pass,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (state.networkError) {
                Snackbar.make(
                    binding.root,
                    R.string.error_check_network_connection,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else if (state.loginOrPassError) {
                Snackbar.make(
                    binding.root,
                    R.string.error_login_or_pass_unknown,
                    Snackbar.LENGTH_LONG
                )
                    .show()
            } else {
                findNavController().navigateUp()
                Toast.makeText(requireActivity(), R.string.toast_text_successful_sign_in, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
}