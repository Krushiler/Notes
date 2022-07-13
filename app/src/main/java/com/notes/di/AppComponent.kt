package com.notes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.notes.App
import com.notes.data.NoteDatabase
import com.notes.data.NotesRepository
import com.notes.ui.details.NoteDetailsFragment
import com.notes.ui.details.NoteDetailsViewModel
import com.notes.ui.list.NoteListFragment
import com.notes.ui.list.NoteListViewModel
import dagger.*
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
//        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): AppComponent
    }

    fun getNoteDatabase(): NoteDatabase

    fun inject(noteListFragment: NoteListFragment)
    fun inject(noteDetailsFragment: NoteDetailsFragment)

    fun noteDetailsViewModel(): NoteDetailsViewModel.Factory
    fun noteListViewModel(): NoteListViewModel.Factory
}

@Module(
    includes = [
        AppModule.Binding::class
    ]
)
class AppModule {

    @Provides
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideNoteDatabase(
        context: Context
    ) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java, "database-note.db"
    ).createFromAsset("database-note.db")
        .build()

    @Provides
    @Singleton
    fun provideNotesRepository(
        appScope: CoroutineScope,
        database: NoteDatabase
    ) = NotesRepository(database, appScope)

    @Module
    interface Binding {

        @Binds
        fun bindContext(application: Application): Context

    }
}