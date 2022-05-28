package ru.netology.nmedia.activity.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth

class SignInDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.dialog_title_sign_in))
                .setMessage(getString(R.string.sign_in_message_are_you_sure))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.dialog_positive_button_text)) { dialog, id ->
                    AppAuth.getInstance().removeAuth()
                    findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_text)) { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}