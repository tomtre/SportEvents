package com.tom.sportevents.feature.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tom.sportevents.R

@OptIn(ExperimentalMaterialApi::class)
@Suppress("ModifierParameterPosition", "ComposableParametersOrdering")
@Composable
fun PullRefreshLazyList(
    isLoading: Boolean,
    @StringRes error: Int?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyColumnContent: LazyListScope.() -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = isLoading, onRefresh = onRefresh)

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.Center
    ) {
        if (error == null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                lazyColumnContent(this)
            }
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = error))
                Button(onClick = onRefresh, modifier = Modifier.padding(top = 10.dp)) {
                    Text(stringResource(id = R.string.refresh))
                }
            }
        }
    }
}
