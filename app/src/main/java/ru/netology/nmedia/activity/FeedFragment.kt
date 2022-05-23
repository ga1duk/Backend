package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PostAttachmentFragment.Companion.stringArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        binding.errorGroup.isVisible = false

        binding.btnNewEntries.isVisible = false

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                if (viewModel.checkForUsersAuthentication()) {
                    if (!post.likedByMe) {
                        viewModel.likeById(post.id)
                    } else {
                        viewModel.dislikeById(post.id)
                    }
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onAttachmentClick(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_postAttachmentFragment,
                    Bundle().apply { stringArg = post.attachment?.url })
            }
        })
        binding.list.adapter = adapter

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swipeRefreshLayout.isRefreshing = state.refreshing
            binding.errorGroup.isVisible = state.error
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction("Retry") { viewModel.loadPosts() }
                    .show()
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.emptyText.isVisible = state.empty
        }

        viewModel.isUserAuthorized.observe(viewLifecycleOwner) { state ->
            if (!state)
                Snackbar.make(binding.root, R.string.error_sign_in, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_sign_in) {
                        findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
                    }
                    .show()
        }

        viewModel.newerPostsCount.observe(viewLifecycleOwner) { state ->
            if (state > 0) {
                binding.btnNewEntries.isVisible = true
            }
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.fab.setOnClickListener {
            if (viewModel.checkForUsersAuthentication()) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            }
        }

        binding.btnNewEntries.setOnClickListener {
            viewModel.setNewPostsVisibilityToTrue()
            binding.btnNewEntries.isVisible = false
            binding.list.smoothScrollToPosition(0)
        }

        with(binding.swipeRefreshLayout) {
            setOnRefreshListener {
                viewModel.refreshPosts()
            }
            setColorSchemeResources(
                R.color.colorAccent
            )
            setSize(CircularProgressDrawable.LARGE)
        }

        return binding.root
    }
}
