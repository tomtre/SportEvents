package com.tom.sportevents.feature.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tom.sportevents.core.model.FormattedScheduleItem
import com.tom.sportevents.feature.common.ui.ListItem

@Composable
fun ScheduleListItem(
    scheduleItem: FormattedScheduleItem,
    modifier: Modifier = Modifier
) {
    ListItem(
        title = scheduleItem.title,
        subtitle = scheduleItem.subtitle,
        date = scheduleItem.formattedDate,
        imageUrl = scheduleItem.imageUrl,
        modifier = modifier

    )
}

@Preview
@Composable
private fun PreviewScheduleListItem() {
    val scheduleItem = FormattedScheduleItem(
        id = "1",
        title = "Hamburger vs Magdeburg",
        subtitle = "Champions League",
        formattedDate = "Yesterday",
        imageUrl = "imgUrl",
    )
    ScheduleListItem(scheduleItem = scheduleItem)
}
