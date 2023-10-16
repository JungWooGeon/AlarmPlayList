package com.sample.alarmplaylist.di

import com.sample.alarmplaylist.presentation.add_alarm.AddAlarmViewModel
import com.sample.alarmplaylist.data.db.alarm.AlarmDataBase
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepository
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepositoryImpl
import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepository
import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepositoryImpl
import com.sample.alarmplaylist.domain.alarm.AddAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.DeleteAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.GetAllAlarmsUseCase
import com.sample.alarmplaylist.domain.alarm.GetLastAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.UpdateAlarmUseCase
import com.sample.alarmplaylist.domain.playlist.GetAllPlaylistsUseCase
import com.sample.alarmplaylist.playlist.playlist_db.PlaylistDataBase
import com.sample.alarmplaylist.presentation.alarm.AlarmViewModel
import com.sample.alarmplaylist.presentation.select_playlist.SelectPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database 인스턴스 제공
    single { AlarmDataBase.getInstance(get()) }
    single { PlaylistDataBase.getInstance(get()) }

    // Repository 제공
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }

    // UseCase들 제공
    factory { DeleteAlarmUseCase(get()) }
    factory { GetAllAlarmsUseCase(get()) }
    factory { UpdateAlarmUseCase(get()) }
    factory { AddAlarmUseCase(get()) }

    factory { GetAllPlaylistsUseCase(get()) }
    factory { GetLastAlarmUseCase(get()) }

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
}