package com.tom.sportevents.feature.events

import com.tom.sportevents.MainDispatcherRule
import com.tom.sportevents.R
import com.tom.sportevents.core.domain.ObserveEventsUseCase
import com.tom.sportevents.core.model.FormattedEventItem
import com.tom.sportevents.core.model.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeEventsUseCase = mockk<ObserveEventsUseCase>()
    private lateinit var viewModel: EventsViewModel

    @Test
    fun `state is initially loading`() = runTest {
        every { observeEventsUseCase() } returns emptyFlow()
        viewModel = EventsViewModel(observeEventsUseCase)

        viewModel.state.value shouldBeEqualTo EventsState(isLoading = true)
    }

    @Test
    fun `error is shown when error comes from domain`() = runTest {
        every { observeEventsUseCase() } returns flowOf(Result.Error())
        viewModel = EventsViewModel(observeEventsUseCase)

        val values = mutableListOf<EventsState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo EventsState(isLoading = true)
        values[1] shouldBeEqualTo EventsState(isLoading = false, error = R.string.error_network)
        values shouldHaveSize 2

        verify(exactly = 1) { observeEventsUseCase() }
    }

    @Test
    fun `list is shown when success result comes from domain`() = runTest {
        every { observeEventsUseCase() } returns flowOf(Result.Success(listOfEvents))
        viewModel = EventsViewModel(observeEventsUseCase)

        val values = mutableListOf<EventsState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo EventsState(isLoading = true)
        values[1] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents)
        values shouldHaveSize 2

        verify(exactly = 1) { observeEventsUseCase() }
    }

    @Test
    fun `error and then list is shown when error and then success result comes from domain`() = runTest {
        val flowResult = MutableSharedFlow<Result<List<FormattedEventItem>>>()
        every { observeEventsUseCase() } returns flowResult
        viewModel = EventsViewModel(observeEventsUseCase)

        val values = mutableListOf<EventsState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo EventsState(isLoading = true)
        flowResult.emit(Result.Error())
        values[1] shouldBeEqualTo EventsState(isLoading = false, error = R.string.error_network)
        flowResult.emit(Result.Success(listOfEvents))
        values[2] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents)
        values shouldHaveSize 3

        verify(exactly = 1) { observeEventsUseCase() }
    }

    @Test
    fun `call again domain when refresh request comes from view`() = runTest {
        every { observeEventsUseCase() } returns flowOf(Result.Success(listOfEvents))
        viewModel = EventsViewModel(observeEventsUseCase)

        val values = mutableListOf<EventsState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo EventsState(isLoading = true)
        values[1] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents)
        viewModel.refresh()
        values[2] shouldBeEqualTo EventsState(isLoading = true, error = null, items = listOfEvents)
        values[3] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents)

        values shouldHaveSize 4

        verify(exactly = 2) { observeEventsUseCase() }
    }

    @Test
    fun `navigate to playback and then clear navigation when requests come from view`() = runTest {
        val urlArg = "urlArg"
        every { observeEventsUseCase() } returns flowOf(Result.Success(listOfEvents))
        viewModel = EventsViewModel(observeEventsUseCase)

        val values = mutableListOf<EventsState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo EventsState(isLoading = true)
        values[1] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents)
        viewModel.onEventItemClicked(urlArg)
        values[2] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents, navigateToPlayback = urlArg)
        viewModel.clearNavigation()
        values[3] shouldBeEqualTo EventsState(isLoading = false, error = null, items = listOfEvents, navigateToPlayback = null)

        values shouldHaveSize 4

        verify(exactly = 1) { observeEventsUseCase() }
    }

    private fun createEventItem(id: String) =
        FormattedEventItem(
            id = id,
            title = "title",
            subtitle = "subtitle",
            formattedDate = "yesterday",
            imageUrl = "imageUrl",
            videoUrl = "videoUrl"
        )

    private val listOfEvents = listOf(createEventItem("1"), createEventItem("2"))
}
