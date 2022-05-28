package ru.netology.nmedia.activity.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth

class ExitDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.dialog_title_exit))
                .setMessage(getString(R.string.exit_message_are_you_sure))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.dialog_positive_button_text)) { dialog, id ->
                    AppAuth.getInstance().removeAuth()
                    findNavController().navigateUp()
                }
                .setNegativeButton(getString(R.string.dialog_negative_button_text)) { dialog, id ->
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}