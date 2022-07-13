package com.notes.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.di.DependencyManager
import com.notes.di.provideAssistedViewModel
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.goBack

class NoteDetailsFragment : ViewBindingFragment<FragmentNoteDetailsBinding>(
    FragmentNoteDetailsBinding::inflate
) {

    private lateinit var viewModel: NoteDetailsViewModel

    companion object {
        const val EXTRAS_NOTE_ID = "noteId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = this.arguments
        val noteId: Long? = args?.getLong(EXTRAS_NOTE_ID)

        DependencyManager.getAppComponent().inject(this)
        viewModel = provideAssistedViewModel {
            DependencyManager.getAppComponent().noteDetailsViewModel().create(noteId = noteId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.apply {
            if (savedInstanceState == null)
                viewModel.note.observe(viewLifecycleOwner) {
                    contentEditText.setText(it.content)
                    titleEditText.setText(it.title)
                }

            deleteNoteButton.setOnClickListener {
                viewModel.deleteNote()
                goBack()
            }
            saveNoteButton.setOnClickListener {
                viewModel.saveNote(titleEditText.text.toString(), contentEditText.text.toString())
                goBack()
            }

            toolbar.setNavigationOnClickListener { goBack() }
        }
    }

}