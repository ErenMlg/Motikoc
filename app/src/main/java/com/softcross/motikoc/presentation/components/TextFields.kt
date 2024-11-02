package com.softcross.motikoc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.Poppins
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor

@Composable
fun IconTextField(
    modifier: Modifier = Modifier,
    givenValue: String,
    enabled: Boolean = true,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    regex: String.() -> Boolean = String::isNotEmpty,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorField: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            enabled = enabled,
            value = givenValue,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            onValueChange = onValueChange,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = GoldColor,
                focusedIndicatorColor = GoldColor,
                errorIndicatorColor = GoldColor,
                disabledIndicatorColor = GoldColor,
                disabledContainerColor = PrimarySurface,
                unfocusedContainerColor = PrimarySurface,
                focusedContainerColor = PrimarySurface,
                errorContainerColor = PrimarySurface,
                disabledTextColor = TextColor,
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor,
                cursorColor = GoldColor,
                errorCursorColor = GoldColor,
                selectionColors = TextSelectionColors(
                    handleColor = GoldColor,
                    backgroundColor = GoldColor.copy(alpha = 0.3f),
                ),
            ),
            isError = !givenValue.regex(),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    text = placeHolder,
                    fontFamily = PoppinsLight,
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, shape = RoundedCornerShape(8.dp))
        )
        AnimatedVisibility(
            visible = !givenValue.regex() && givenValue.isNotEmpty(),
            enter = slideInVertically() + expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        ) {
            errorField()
        }
    }
}

@Composable
fun TextFieldWithoutError(
    modifier: Modifier = Modifier,
    givenValue: String,
    placeHolder: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    regex: String.() -> Boolean = String::isNotEmpty,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        value = givenValue,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = GoldColor,
            focusedIndicatorColor = GoldColor,
            errorIndicatorColor = GoldColor,
            unfocusedContainerColor = PrimarySurface,
            focusedContainerColor = PrimarySurface,
            errorContainerColor = PrimarySurface,
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor,
            cursorColor = GoldColor,
            errorCursorColor = GoldColor,
            selectionColors = TextSelectionColors(
                handleColor = GoldColor,
                backgroundColor = GoldColor.copy(alpha = 0.3f),
            ),
        ),
        isError = !givenValue.regex(),
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(
                text = placeHolder,
                fontFamily = PoppinsLight,
                fontSize = 16.sp,
                color = TextColor
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, shape = RoundedCornerShape(8.dp))
    )
}


@Composable
fun IdentifyTextField(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    givenValue: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    errorField: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextFieldInnerPadding(
            value = givenValue,
            onValueChange = onValueChange,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = GoldColor,
                focusedIndicatorColor = GoldColor,
                errorIndicatorColor = GoldColor,
                unfocusedContainerColor = PrimarySurface,
                focusedContainerColor = PrimarySurface,
                errorContainerColor = PrimarySurface,
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor,
                cursorColor = GoldColor,
                errorCursorColor = GoldColor,
                selectionColors = TextSelectionColors(
                    handleColor = GoldColor,
                    backgroundColor = GoldColor.copy(alpha = 0.3f),
                ),
            ),
            innerPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            placeholder = {
                Text(
                    placeHolder,
                    color = TextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        AnimatedVisibility(
            visible = givenValue.length < 20,
            enter = slideInVertically() + expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        ) {
            errorField()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldInnerPadding(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    innerPadding: PaddingValues = PaddingValues(0.dp),
) {
    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier = modifier,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            cursorBrush = SolidColor(GoldColor),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox =
            @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    contentPadding = innerPadding,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = placeholder,
                    label = label,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    shape = shape,
                    singleLine = singleLine,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    colors = colors
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    IdentifyTextField(
        givenValue = "Name",
        placeHolder = "Name",
        onValueChange = {}, focusRequester = FocusRequester(), errorField = {},

    )
}