package kekmech.ru.notes.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import kekmech.ru.notes.view.NoteFragment

@Subcomponent
interface NoteFragmentComponent : AndroidInjector<NoteFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<NoteFragment>
}