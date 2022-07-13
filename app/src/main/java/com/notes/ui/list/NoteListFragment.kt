package com.notes.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.FragmentNoteListBinding
import com.notes.databinding.ListItemNoteBinding
import com.notes.di.DependencyManager
import com.notes.di.provideAssistedViewModel
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.navigateTo
import com.notes.ui.details.NoteDetailsFragment
import com.notes.ui.details.NoteDetailsViewModel

class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {

    private lateinit var viewModel: NoteListViewModel

    private val recyclerViewAdapter = RecyclerViewAdapter(
        onNoteClick = {
            onNoteClick(it)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyManager.getAppComponent().inject(this)
        viewModel = provideAssistedViewModel {
            DependencyManager.getAppComponent().noteListViewModel().create()
        }
    }


    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            navigateTo(NoteDetailsFragment())
        }

        viewModel.notes.observe(
            viewLifecycleOwner,
            {
                if (it != null) {
                    recyclerViewAdapter.setItems(it)
                }
            }
        )
    }

    private fun onNoteClick(noteId: Long) {
        val fragment = NoteDetailsFragment()
        val args = Bundle().apply {
            putLong(NoteDetailsFragment.EXTRAS_NOTE_ID, noteId)
        }
        fragment.arguments = args
        navigateTo(fragment)
    }

    private class RecyclerViewAdapter(
        private val onNoteClick: (noteId: Long) -> Unit
    ) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val items = mutableListOf<NoteListItem>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = ViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            holder.bind(items[position])
            holder.binding.root.setOnClickListener {
                onNoteClick(items[position].id)
            }
        }

        override fun getItemCount() = items.size

        fun setItems(
            items: List<NoteListItem>
        ) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        }

        private class ViewHolder(
            val binding: ListItemNoteBinding
        ) : RecyclerView.ViewHolder(
            binding.root
        ) {

            fun bind(
                note: NoteListItem
            ) {
                binding.titleLabel.text = note.title
                binding.contentLabel.text = note.content
            }

        }

    }

}