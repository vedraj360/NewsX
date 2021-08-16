package com.vdx.newsx.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vdx.newsx.presentation.ui.theme.Background
import com.vdx.newsx.presentation.ui.theme.CardBackground

@Composable
fun ComposeSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?
) {
    SnackbarHost(
        hostState = snackbarHostState, snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                backgroundColor = CardBackground,
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            Text(
                                text = actionLabel,
                                style = MaterialTheme.typography.body2,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        }
                    }
                }
            ) {
                Text(
                    text = data.message,
                    style = MaterialTheme.typography.body2,
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
        }, modifier = modifier
    )
}