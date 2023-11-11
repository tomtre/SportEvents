package com.tom.sportevents.core.domain

import app.cash.turbine.test
import com.tom.sportevents.MainDispatcherRule
import com.tom.sportevents.core.data.repository.ScheduleRepository
import com.tom.sportevents.core.model.FormattedScheduleItem
import com.tom.sportevents.core.model.Result
import com.tom.sportevents.core.model.ScheduleItem
import com.tom.sportevents.fake.TestDateFormatter
import com.tom.sportevents.fake.TestTimeModificationEventSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldHaveSize
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveScheduleUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // fixed time variables
    private val localDateTime = LocalDateTime.of(2020, 5, 3, 10, 32, 5)
    private val zoneId = ZoneId.of("Europe/London")
    private val baseZonedDateTime = ZonedDateTime.of(localDateTime, zoneId)

    private val testDateFormatter = spyk(TestDateFormatter(currentZonedDateTime = baseZonedDateTime))
    private val testTimeModificationEventSource = TestTimeModificationEventSource()

    private val scheduleRepository = mockk<ScheduleRepository>()

    private lateinit var useCase: ObserveScheduleUseCase

    @Before
    fun setup() {
        useCase = ObserveScheduleUseCase(
            scheduleRepository = scheduleRepository,
            timeModificationEventSource = testTimeModificationEventSource,
            dateFormatter = testDateFormatter
        )
    }

    @Test
    fun `emit only those containing tomorrow's date`() = runTest {
        val resultFlow = useCase()

        // emit list of 3 items
        coEvery { scheduleRepository.getSchedule() } returns Result.Success(mixedDates)
        testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime)

        val values = mutableListOf<Result<List<FormattedScheduleItem>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            resultFlow.toList(values)
        }

        values[0] shouldBeInstanceOf Result.Success::class
        (values[0] as Result.Success).data shouldHaveSize 2
        values shouldHaveSize 1

        coVerify(exactly = 1) { scheduleRepository.getSchedule() }
        coVerify(exactly = 1) { testDateFormatter.currentZonedDateTime() }
        coVerify(exactly = 2) { testDateFormatter.formatRelativeDays(any()) }
    }

    @Test
    fun `fetch data every 30 seconds`() = runTest {
        val resultFlow = useCase()

        val instant = baseZonedDateTime.plusDays(1).toInstant()
        coEvery { scheduleRepository.getSchedule() } returns
            Result.Success(listOf(createScheduleItem("1", instant))) andThen
            Result.Success(listOf(createScheduleItem("2", instant))) andThen
            Result.Success(listOf(createScheduleItem("3", instant)))

        testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime)

        val values = mutableListOf<Result<List<FormattedScheduleItem>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            resultFlow.toList(values)
        }

        values shouldHaveSize 1
        advanceTimeBy(30_001)
        values shouldHaveSize 2
        advanceTimeBy(30_001)
        values shouldHaveSize 3

        coVerify(exactly = 3) { scheduleRepository.getSchedule() }
    }

    @Test
    fun `rebuild model when time modification happens`() = runTest {
        val resultFlow = useCase()

        coEvery { scheduleRepository.getSchedule() } returns Result.Success(orderedDates)
        testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime)

        val values = mutableListOf<Result<List<FormattedScheduleItem>>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            resultFlow.toList(values)
        }

        values shouldHaveSize 1
        (values[0] as Result.Success).data[0].id shouldBeEqualTo "1"

        testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime.plusDays(1))
        testTimeModificationEventSource.emitTimeModificationEvent()
        values shouldHaveSize 2
        (values[1] as Result.Success).data[0].id shouldBeEqualTo "2"

        testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime.plusDays(2))
        testTimeModificationEventSource.emitTimeModificationEvent()
        values shouldHaveSize 3
        (values[2] as Result.Success).data[0].id shouldBeEqualTo "3"

        coVerify(exactly = 1) { scheduleRepository.getSchedule() }
        coVerify(exactly = 3) { testDateFormatter.currentZonedDateTime() }
    }

    // Turbine example
    @Test
    fun `rebuild model when time modification happens (Turbine example)`() = runTest {
        val resultFlow = useCase()

        coEvery { scheduleRepository.getSchedule() } returns Result.Success(orderedDates)
        testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime)

        resultFlow.test {
            (awaitItem() as Result.Success).data[0].id shouldBeEqualTo "1"

            testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime.plusDays(1))
            testTimeModificationEventSource.emitTimeModificationEvent()
            (awaitItem() as Result.Success).data[0].id shouldBeEqualTo "2"

            testDateFormatter.setCurrentZonedDateTime(baseZonedDateTime.plusDays(2))
            testTimeModificationEventSource.emitTimeModificationEvent()
            (awaitItem() as Result.Success).data[0].id shouldBeEqualTo "3"
        }

        coVerify(exactly = 1) { scheduleRepository.getSchedule() }
        coVerify(exactly = 3) { testDateFormatter.currentZonedDateTime() }
    }

    private fun createScheduleItem(id: String, instant: Instant) =
        ScheduleItem(
            id = id,
            title = "title",
            subtitle = "subtitle",
            date = instant,
            imageUrl = "imageUrl"
        )

    private val mixedDates =
        listOf(
            createScheduleItem("1", baseZonedDateTime.plusDays(1).toInstant()),
            createScheduleItem("2", baseZonedDateTime.toInstant()),
            createScheduleItem("3", baseZonedDateTime.plusDays(1).plusMinutes(1).toInstant())
        )

    private val orderedDates =
        listOf(
            createScheduleItem("1", baseZonedDateTime.plusDays(1).toInstant()),
            createScheduleItem("2", baseZonedDateTime.plusDays(2).toInstant()),
            createScheduleItem("3", baseZonedDateTime.plusDays(3).toInstant())
        )
}
