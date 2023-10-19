# AlarmPlayList

<br>

![ic_launcher](https://user-images.githubusercontent.com/61993128/223607324-6076b859-dab4-4c58-8d5e-9160b08392f0.png)

<br><br>

### 💡 일어나! - 튜브 목록 예약 알람 <br>
Android 알람 실행 시 직접 만든 플레이리스트에 있는 음악 목록을 차례대로 재생하는 앱

<br><br>

### 🛠 사용
 - android, android studio
 - kotlin
 - MVVM, Clean Architecture, koin, Room DB, Retrofit2, Glide, AlarmManager, Notification, Coroutine
 - BroadcastReceiver, Service

<br><br>

### 🧩 구조
```
|── di
|   ├── appModule.kt
|   └── ProvideAPI.kt
|
│── presentation
│   └── add_alarm
│   │   ├── AddAlarmActivity.kt
│   │   └── AddAlarmViewModel.kt
│   ├── add_playlist
│   │   ├── AddPlaylistActivity.kt
│   │   ├── AddPlaylistViewModel.kt
│   │   └── SearchAdapter.kt
│   ├── alarm
│   │   ├── AlarmAdapter.kt
│   │   ├── AlarmFragment.kt
│   │   └── AlarmViewModel.kt
│   ├── notification
│   │   ├── AlarmReceiver.kt
│   │   └── AlarmService.kt
│   ├── play_youtube
│   │   ├── YoutubePlayActivity.kt
│   │   └── YoutubePlayViewModel.kt
│   ├── playlist
│   │   ├── PlaylistAdapter.kt
│   │   ├── PlaylistFragment.kt
│   │   ├── PlaylistViewModel.kt
│   │   └── RenamePlaylistDialog.kt
│   ├── select_playlist
│   │   ├── SelectPlaylistAdapter.kt
│   │   ├── SelectPlaylistDialog.kt
│   │   └── SelectPlaylistViewModel.kt
│   ├── shared_adapters
│   │   └── MusicListAdapter.kt
│   └── MainActivity.kt
│
├── domain
│   ├── alarm
│   │   ├── AddAlarmUseCase.kt
│   │   ├── DeleteAlarmUseCase.kt
│   │   ├── GetAllAlarmsUseCase.kt
│   │   ├── GetLastAlarmUseCase.kt
│   │   └── updateAlarmUseCase.kt
│   ├── playlist
│   │   ├── AddPlaylistUseCase.kt
│   │   ├── DeletePlaylistUseCase.kt
│   │   ├── GetAllPlaylistUseCase.kt
│   │   └── UpdatePlaylistUseCase.kt
│   └── youtube
│       ├── AddYoutubeUseCase.kt
│       ├── DeleteYoutubeUseCase.kt
│       ├── GetSelectedYoutubes.kt
│       └── SearchYoutubeUseCase.kt
│
├── data
│   ├── db
│   │   ├── alarm
│   │   │   ├── AlarmDao.kt
│   │   │   └── AlarmDataBase.kt
│   │   ├── playlist
│   │   │   ├── PlaylistDao.kt
│   │   │   └── PlaylistDataBase.kt
│   │   └── youtube
│   │       ├── PlaylistDao.kt
│   │       └── PlaylistDataBase.kt
│   ├── entity
│   │   ├── Alarm.kt
│   │   ├── Playlist.kt
│   │   └── Youtube.kt
│   ├── network
│   │   ├── SearchYoutubeInterface
│   │   └── SearchYoutubeResponse.kt
│   └── repository
│       ├── alarm
│       │   ├── AlarmRepository
│       │   └── AlarmRepositoryImpl.kt
│       ├── playlist
│       │   ├── PlaylistRepository
│       │   └── PlaylistRepositoryImpl.kt
│       └── youtube
│           ├── YoutubeRepository
│           └── YoutubeRepositoryImpl.kt
│   
├── AlarmApplication.kt
└── Constants.kt
```

<br><br>

### ⭐️ 기능
 - 알람
   - 알람 기능
   - 알람 설정 시 알람음을 플레이리스트로 설정
   
 - 플레이리스트
   - 플레이리스트 만들기
   - 플레이리스트 이름 변경 및 삭제
   - 플레이리스트를 선택하여 유튜브 음악 추가 및 삭제
   - 유튜브 비디오 검색 및 실행

<br><br>

### 📷 화면

<br>

<img src="https://user-images.githubusercontent.com/61993128/224250377-a5722fe6-cd39-4d05-bc84-026847514a27.png" width="315" height="560"/> <img src="https://user-images.githubusercontent.com/61993128/224250390-d64e670f-f0e2-4eb5-9b83-b98646210f6b.png" width="315" height="560" />

<img src="https://user-images.githubusercontent.com/61993128/224250394-88d9be46-cc35-4bc7-82e3-2820bb67c3a8.png" width="315" height="560" /> <img src="https://user-images.githubusercontent.com/61993128/224250399-939d92fd-2358-426a-81f1-b415a5731c83.png" width="315" height="560" />

<img src="https://user-images.githubusercontent.com/61993128/224250410-469a041f-9e1a-4d8e-93aa-9fb581f54418.png" width="315" height="560" />


<br><br>

### 🔥 Google Play
https://play.google.com/store/apps/details?id=com.sample.alarmplaylist&hl=ko
