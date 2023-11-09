Java 17 is required to build.

### Architecture:
I chose a single module approach. It can be easily rewritten to a multi-module architecture plus convention plugins if the app grows.
The app doesn't use pagination as the API does not support it (or at least it isn't documented)

### Tech stack
- MVVM + Single UiState + StateIn (WhileSubscribed) + StateInMerge extension
- Ktor + kotlinX Serialization
- Arrow Either
- Compose + Compose Navigation + Coil
- Hilt/Dagger for DI
- ExoPlayer for video
- Ktlint and detekt

### Event screen
The events screen fetches data once, but the view model is recalculated every one minute or when the user sets a new time-zone or phone time. We show relative dates (“yesterday”, “tomorrow” etc) to show the time of the event, so we need a mechanism to constantly refresh the view model.

### Schedule screen
The schedule screen fetches data every 30 seconds. The view model is recalculated when the user sets a new time-zone or phone time.

### Playback screen
Currently, I'm injecting the Player into the ViewModel. I found it on the Internet. There must be a better approach, but I haven't had time to do any research on how to do it correctly.

### Tests:
Tests are not completed. I've implemented only a few to demonstrate the usage of mocks, fakes and spies, and the general approach to testing suspended functions and flows.
