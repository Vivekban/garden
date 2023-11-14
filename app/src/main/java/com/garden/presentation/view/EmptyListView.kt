package com.garden.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.garden.common.VoidCallback
import com.garden.ui.theme.Dimens


@Composable
fun EmptyListView(
    modifier: Modifier,
    text: String,
    action: String? = null,
    callback: VoidCallback? = null
) {
    Box(modifier = modifier.padding(Dimens.PaddingNormal)) {
        Column(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingNormal))
            if (callback != null && action != null) Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
                onClick = callback
            ) {
                Text(
                    color = MaterialTheme.colors.primary,
                    text = action
                )
            }
        }
    }
}