package com.sample.alarmplaylist.di

import com.sample.alarmplaylist.data.db.alarm.AlarmDataBase
import com.sample.alarmplaylist.data.db.playlist.PlaylistDataBase
import com.sample.alarmplaylist.data.db.youtube.YoutubeDataBase
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepository
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepositoryImpl
import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepository
import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepositoryImpl
import com.sample.alarmplaylist.data.repository.youtube.YoutubeRepository
import com.sample.alarmplaylist.data.repository.youtube.YoutubeRepositoryImpl
import com.sample.alarmplaylist.domain.alarm.AddAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.DeleteAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.GetAllAlarmsUseCase
import com.sample.alarmplaylist.domain.alarm.GetLastAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.UpdateAlarmUseCase
import com.sample.alarmplaylist.domain.playlist.AddPlaylistUseCase
import com.sample.alarmplaylist.domain.playlist.DeletePlaylistUseCase
import com.sample.alarmplaylist.domain.playlist.GetAllPlaylistsUseCase
import com.sample.alarmplaylist.domain.playlist.UpdatePlaylistUseCase
import com.sample.alarmplaylist.domain.youtube.AddYoutubeUseCase
import com.sample.alarmplaylist.domain.youtube.DeleteYoutubeUseCase
import com.sample.alarmplaylist.domain.youtube.GetSelectedYoutubesUseCase
import com.sample.alarmplaylist.domain.youtube.SearchYoutubeUseCase
import com.sample.alarmplaylist.presentation.play_youtube.YoutubePlayViewModel
import com.sample.alarmplaylist.presentation.add_playlist.AddPlaylistViewModel
import com.sample.alarmplaylist.presentation.playlist.PlaylistViewModel
import com.sample.alarmplaylist.presentation.add_alarm.AddAlarmViewModel
import com.sample.alarmplaylist.presentation.alarm.AlarmViewModel
import com.sample.alarmplaylist.presentation.select_playlist.SelectPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database 인스턴스 제공
    single { AlarmDataBase.getInstance(get()) }
    single { PlaylistDataBase.getInstance(get()) }
    single { YoutubeDataBase.getInstance(get()) }

    // Retrofit 인스턴스 제공
    single { provideSearchYoutubeRetrofit() }
    single { provideSearchYoutubeInterface(get()) }

    // Repository 제공
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
    single<YoutubeRepository> { YoutubeRepositoryImpl(get(), get()) }

    // UseCase들 제공
    // alarm
    factory { DeleteAlarmUseCase(get()) }
    factory { GetAllAlarmsUseCase(get()) }
    factory { UpdateAlarmUseCase(get()) }
    factory { AddAlarmUseCase(get()) }
    factory { GetLastAlarmUseCase(get()) }

    // playlist
    factory { GetAllPlaylistsUseCase(get()) }
    factory { AddPlaylistUseCase(get()) }
    factory { DeletePlaylistUseCase(get()) }
    factory { UpdatePlaylistUseCase(get()) }

    //youtube
    factory { GetSelectedYoutubesUseCase(get()) }
    factory { AddYoutubeUseCase(get()) }
    factory { DeleteYoutubeUseCase(get()) }
    factory { SearchYoutubeUseCase(get()) }

    // ViewModel 제공
    viewModel {
        AlarmViewModel(
            deleteAlarmUseCase = get(),
            getAllAlarmsUseCase = get(),
            updateAlarmUseCase = get(),
        )
    }

    viewModel {
        AddAlarmViewModel(
            addAlarmUseCase = get(),
            updateAlarmUseCase = get(),
            getLastAlarmUseCase = get()
        )
    }

    viewModel {
        SelectPlaylistViewModel(
            getAllPlaylistsUseCase = get()
        )
    }

    viewModel {
        PlaylistViewModel(
            getAllPlaylistsUseCase = get(),
            addPlaylistUseCase = get(),
            deletePlaylistUseCase = get(),
            updatePlaylistUseCase = get(),
            getSelectedYoutubesUseCase = get(),
            deleteYoutubeUseCase = get()
        )
    }

    viewModel {
        AddPlaylistViewModel(
            addYoutubeUseCase = get(),
            searchYoutubeUseCase = get()
        )
    }

    viewModel {
        YoutubePlayViewModel(
            getSelectedYoutubesUseCase = get()
        )
    }
}