package com.garden.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.garden.presentation.common.VoidCallback
import com.garden.presentation.theme.Dimens
import com.google.accompanist.themeadapter.material.MdcTheme

@Composable
fun EmptyListView(
    text: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    callback: VoidCallback? = null
) {
    Box(modifier = modifier.padding(Dimens.PaddingNormal)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingNormal))
            if (callback != null && action != null) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onPrimary
                    ),
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
}

@Preview(showBackground = true)
@Composable
private fun EmptyListWithActionPreview() {
    MdcTheme {
        EmptyListView(
            text = "This is very long preview. With very long information.",
            action = "Clear",
            callback = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyListWithoutActionPreview() {
    MdcTheme {
        EmptyListView(
            text = "This is very long preview. With very long information."
        )
    }
}
