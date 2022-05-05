package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig.BASE_URL
import ru.netology.nmedia.databinding.FragmentPostAttachmentBinding
import ru.netology.nmedia.util.AttachmentUrlArg

class PostAttachmentFragment : Fragment() {

    companion object {
        var Bundle.stringArg: String? by AttachmentUrlArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostAttachmentBinding.inflate(inflater, container, false)

        val urlImages = "${BASE_URL}/media/${arguments?.stringArg}"
        Glide.with(binding.attachment)
            .load(urlImages)
            .timeout(10_000)
            .into(binding.attachment)

        return binding.root
    }
}