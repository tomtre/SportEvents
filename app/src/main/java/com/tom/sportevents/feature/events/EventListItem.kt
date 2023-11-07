package com.tom.sportevents.feature.events

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tom.sportevents.core.model.EventItem
import com.tom.sportevents.feature.common.ui.ListItem

@Composable
fun EventListItem(
    eventItem: EventItem,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        title = eventItem.title,
        subtitle = eventItem.subtitle,
        date = eventItem.date,
        imageUrl = eventItem.imageUrl,
        modifier = modifier.clickable { onItemClick(eventItem.videoUrl) }

    )
}

@Preview
@Composable
private fun PreviewEventItem() {
    val eventItem = EventItem(
        id = "1",
        title = "Hamburger vs Magdeburg",
        subtitle = "Champions League",
        date = "Yesterday",
        imageUrl = "imgUrl",
        videoUrl = "videoUrl"
    )
    EventListItem(eventItem = eventItem, onItemClick = {})
}
