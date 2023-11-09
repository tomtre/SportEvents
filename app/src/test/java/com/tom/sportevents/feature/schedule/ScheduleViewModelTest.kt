package com.tom.sportevents.feature.schedule

import com.tom.sportevents.MainDispatcherRule
import com.tom.sportevents.R
import com.tom.sportevents.core.domain.ObserveScheduleUseCase
import com.tom.sportevents.core.model.FormattedScheduleItem
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
class ScheduleViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val observeScheduleUseCase = mockk<ObserveScheduleUseCase>()
    private lateinit var viewModel: ScheduleViewModel

    @Test
    fun `state is initially loading`() = runTest {
        every { observeScheduleUseCase() } returns emptyFlow()
        viewModel = ScheduleViewModel(observeScheduleUseCase)

        viewModel.state.value shouldBeEqualTo ScheduleState(isLoading = true)
    }

    @Test
    fun `error is shown when error comes from domain`() = runTest {
        every { observeScheduleUseCase() } returns flowOf(Result.Error())
        viewModel = ScheduleViewModel(observeScheduleUseCase)

        val values = mutableListOf<ScheduleState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo ScheduleState(isLoading = true)
        values[1] shouldBeEqualTo ScheduleState(isLoading = false, error = R.string.error_network)
        values shouldHaveSize 2

        verify(exactly = 1) { observeScheduleUseCase() }
    }

    @Test
    fun `list is shown when success result comes from domain`() = runTest {
        every { observeScheduleUseCase() } returns flowOf(Result.Success(listOfScheduleItems))
        viewModel = ScheduleViewModel(observeScheduleUseCase)

        val values = mutableListOf<ScheduleState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo ScheduleState(isLoading = true)
        values[1] shouldBeEqualTo ScheduleState(isLoading = false, error = null, items = listOfScheduleItems)
        values shouldHaveSize 2

        verify(exactly = 1) { observeScheduleUseCase() }
    }

    @Test
    fun `error and then list is shown when error and then success result comes from domain`() = runTest {
        val flowResult = MutableSharedFlow<Result<List<FormattedScheduleItem>>>()
        every { observeScheduleUseCase() } returns flowResult
        viewModel = ScheduleViewModel(observeScheduleUseCase)

        val values = mutableListOf<ScheduleState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo ScheduleState(isLoading = true)
        flowResult.emit(Result.Error())
        values[1] shouldBeEqualTo ScheduleState(isLoading = false, error = R.string.error_network)
        flowResult.emit(Result.Success(listOfScheduleItems))
        values[2] shouldBeEqualTo ScheduleState(isLoading = false, error = null, items = listOfScheduleItems)
        values shouldHaveSize 3

        verify(exactly = 1) { observeScheduleUseCase() }
    }

    @Test
    fun `call again domain when refresh request comes from view`() = runTest {
        every { observeScheduleUseCase() } returns flowOf(Result.Success(listOfScheduleItems))
        viewModel = ScheduleViewModel(observeScheduleUseCase)

        val values = mutableListOf<ScheduleState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(values)
        }

        values[0] shouldBeEqualTo ScheduleState(isLoading = true)
        values[1] shouldBeEqualTo ScheduleState(isLoading = false, error = null, items = listOfScheduleItems)
        viewModel.refresh()
        values[2] shouldBeEqualTo ScheduleState(isLoading = true, error = null, items = listOfScheduleItems)
        values[3] shouldBeEqualTo ScheduleState(isLoading = false, error = null, items = listOfScheduleItems)

        values shouldHaveSize 4

        verify(exactly = 2) { observeScheduleUseCase() }
    }

    private fun createScheduleItem(id: String) =
        FormattedScheduleItem(
            id = id,
            title = "title",
            subtitle = "subtitle",
            formattedDate = "yesterday",
            imageUrl = "imageUrl"
        )

    private val listOfScheduleItems = listOf(createScheduleItem("1"), createScheduleItem("2"))
}
