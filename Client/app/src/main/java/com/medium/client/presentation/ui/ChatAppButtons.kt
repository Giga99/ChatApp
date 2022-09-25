package com.medium.client.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.medium.client.R

@Composable
fun ChatAppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colors.background,
            backgroundColor = MaterialTheme.colors.primary
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.size_16))
    ) {
        Text(text = text)
    }
}
