package kekmech.ru.ui_kit_textfield.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
public fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    trailingIcon: (@Composable TextFieldTrailingIconScope.() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    palette: TextFieldPalette = TextFieldPalette.Default,
    form: TextFieldForm = TextFieldForm.Filled,
) {
    val trailingIconScope = remember(enabled, readOnly, onValueChange) {
        TextFieldTrailingIconScope(
            isClearEnabled = enabled && !readOnly,
            onClear = { onValueChange("") },
        )
    }
    androidx.compose.material3.TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = form.textStyle,
        label = label?.let { { Text(text = it, lineHeight = form.labelLineHeight) } },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = form.textStyle,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        trailingIcon = trailingIcon?.let { { it.invoke(trailingIconScope) } },
        supportingText = supportingText?.let { { Text(text = it, style = form.supportingTextStyle) } },
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        shape = form.shape,
        colors = palette.toTextFieldColors(),
    )
}

@Composable
public fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    trailingIcon: (@Composable TextFieldTrailingIconScope.() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    palette: TextFieldPalette = TextFieldPalette.Default,
    form: TextFieldForm = TextFieldForm.Filled,
) {
    val trailingIconScope = remember(enabled, readOnly, onValueChange) {
        TextFieldTrailingIconScope(
            isClearEnabled = enabled && !readOnly,
            onClear = { onValueChange(TextFieldValue()) },
        )
    }
    androidx.compose.material3.TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = form.textStyle,
        label = label?.let { { Text(text = it, lineHeight = form.labelLineHeight) } },
        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = form.textStyle,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        trailingIcon = trailingIcon?.let { { it.invoke(trailingIconScope) } },
        supportingText = supportingText?.let { { Text(text = it, style = form.supportingTextStyle) } },
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        shape = form.shape,
        colors = palette.toTextFieldColors(),
    )
}

public class TextFieldTrailingIconScope internal constructor(
    private val isClearEnabled: Boolean,
    private val onClear: () -> Unit,
) {

    @Composable
    public fun Icon(
        imageVector: ImageVector,
        contentDescription: String? = null,
    ) {
        androidx.compose.material3.Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }

    @Composable
    public fun ClearIconButton() {
        IconButton(
            onClick = onClear,
            enabled = isClearEnabled,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun TextFieldPalette.toTextFieldColors(): TextFieldColors =
    TextFieldDefaults.colors(
        focusedTextColor = text,
        unfocusedTextColor = text,
        disabledTextColor = disabledContent,
        errorTextColor = text,
        focusedContainerColor = container,
        unfocusedContainerColor = container,
        disabledContainerColor = disabledContainer,
        errorContainerColor = container,
        cursorColor = accent,
        errorCursorColor = error,
        selectionColors = TextSelectionColors(
            handleColor = accent,
            backgroundColor = accent.copy(alpha = 0.4f),
        ),
        focusedIndicatorColor = accent,
        unfocusedIndicatorColor = indicator,
        disabledIndicatorColor = disabledContent,
        errorIndicatorColor = error,
        focusedTrailingIconColor = secondaryContent,
        unfocusedTrailingIconColor = secondaryContent,
        disabledTrailingIconColor = disabledContent,
        errorTrailingIconColor = error,
        focusedLabelColor = accent,
        unfocusedLabelColor = secondaryContent,
        disabledLabelColor = disabledContent,
        errorLabelColor = error,
        focusedPlaceholderColor = secondaryContent,
        unfocusedPlaceholderColor = secondaryContent,
        disabledPlaceholderColor = disabledContent,
        errorPlaceholderColor = secondaryContent,
        focusedSupportingTextColor = secondaryContent,
        unfocusedSupportingTextColor = secondaryContent,
        disabledSupportingTextColor = disabledContent,
        errorSupportingTextColor = error,
    )

@Composable
@Preview
private fun TextFieldPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            var text by remember { mutableStateOf(TextFieldValue(text = "Hello!")) }
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Label",
                supportingText = "Supporting text",
                trailingIcon = { ClearIconButton() },
            )
            TextField(
                value = "",
                onValueChange = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Placeholder",
                singleLine = true,
                trailingIcon = { Icon(imageVector = Icons.Outlined.Search) },
            )
            TextField(
                value = TextFieldValue(text = "Wrong value"),
                onValueChange = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
                label = "Label",
                supportingText = "Error message",
                isError = true,
            )
            TextField(
                value = "Disabled",
                onValueChange = { /* no-op */ },
                modifier = Modifier.fillMaxWidth(),
                label = "Label",
                enabled = false,
            )
        }
    }
}
