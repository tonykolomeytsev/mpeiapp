package kekmech.ru.notes.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kekmech.ru.core.Presenter
import kekmech.ru.core.scopes.ActivityScope
import kekmech.ru.notes.NoteFragmentPresenter
import kekmech.ru.notes.model.NoteFragmentModel
import kekmech.ru.notes.model.NoteFragmentModelImpl
import kekmech.ru.notes.view.NoteFragment
import kekmech.ru.notes.view.NoteFragmentView

@Module(subcomponents = [NoteFragmentComponent::class])
abstract class NoteFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(NoteFragment::class)
    abstract fun bindNoteFragmentInjectorFactory(factory: NoteFragmentComponent.Factory): AndroidInjector.Factory<*>

    @ActivityScope
    @Binds
    abstract fun provideNotePresenter(presenter: NoteFragmentPresenter): Presenter<NoteFragmentView>

    @ActivityScope
    @Binds
    abstract fun provideNoteModel(model: NoteFragmentModelImpl): NoteFragmentModel
}