package com.tom.sportevents.feature.events

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tom.sportevents.core.model.FormattedEventItem
import com.tom.sportevents.feature.common.ui.ListItem

@Composable
fun EventListItem(
    eventItem: FormattedEventItem,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        title = eventItem.title,
        subtitle = eventItem.subtitle,
        date = eventItem.formattedDate,
        imageUrl = eventItem.imageUrl,
        modifier = modifier.clickable { onItemClick(eventItem.videoUrl) }

    )
}

@Preview
@Composable
private fun PreviewEventItem() {
    val eventItem = FormattedEventItem(
        id = "1",
        title = "Hamburger vs Magdeburg",
        subtitle = "Champions League",
        formattedDate = "Yesterday",
        imageUrl = "imgUrl",
        videoUrl = "videoUrl"
    )
    EventListItem(eventItem = eventItem, onItemClick = {})
}
