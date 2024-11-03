package com.softcross.motikoc.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.GoldColor
import com.softcross.motikoc.presentation.theme.Poppins
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.PrimarySurface
import com.softcross.motikoc.presentation.theme.TextColor

@Composable
fun MultipleInputCollector(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    dataList: List<String>,
    taggedWords: List<String>,
    onRemove: (String) -> Unit,
    onAdd: (String) -> Unit
) {

    var word by remember { mutableStateOf("") }
    var isExpand by remember { mutableStateOf(false) }
    var textFieldPositionX by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
    ) {
        SuggestionLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally)
                .shadow(3.dp, shape = RoundedCornerShape(8.dp))
                .background(PrimarySurface, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .padding(vertical = 4.dp)
        ) {
            taggedWords.forEach { word ->
                TaggedWordItem(word = word) {
                    onRemove(word)
                }
            }
            TextFieldInnerPadding(
                value = word,
                onValueChange = {
                    word = it
                    isExpand = true
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = GoldColor,
                    errorCursorColor = GoldColor,
                    selectionColors = TextSelectionColors(
                        handleColor = GoldColor,
                        backgroundColor = GoldColor.copy(alpha = 0.3f),
                    )
                ),
                innerPadding = PaddingValues(8.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (word.isNotEmpty()) {
                            onAdd(word)
                            word = ""
                        }
                    }
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        "Kelime girin",
                        color = TextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusEvent { isExpand = it.isFocused }
                    .then(
                        if (taggedWords.isNotEmpty()) {
                            Modifier.width(100.dp)
                        } else {
                            Modifier.fillMaxWidth()
                        }
                    )
                    .onGloballyPositioned { coordinates ->
                        textFieldPositionX = coordinates.positionInWindow().x / 2
                    }
            )
        }
        HorizontalDivider(
            color = GoldColor,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 22.dp)
        )

        AnimatedVisibility(visible = taggedWords.size < 3) {
            MotikocDefaultErrorField(
                modifier = Modifier.padding(horizontal = 16.dp),
                errorMessage = "En az 3 kelime girmelisiniz!"
            )
        }

        AnimatedVisibility(
            visible = isExpand,
            modifier = Modifier.graphicsLayer { translationX = textFieldPositionX }) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .graphicsLayer {
                        translationY = 50f
                    },
                shape = SuggestionShape(12f),
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 150.dp),
                ) {
                    items(
                        dataList.filter { datas ->
                            !taggedWords.contains(datas) &&
                                    (datas.lowercase()
                                        .contains(word.lowercase()) || datas.lowercase()
                                        .contains("others"))
                        }.sortedByDescending {
                            word.lowercase().firstOrNull() == it.lowercase().first()
                        }
                    ) {
                        ItemsCategory(title = it) { title ->
                            onAdd(title)
                            word = ""
                        }
                    }
                    item {
                        ItemsCategory(title = "Add") {
                            if (!taggedWords.contains(word)) {
                                onAdd(word)
                            }
                            word = ""
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleInputSelector(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    dataList: List<String>,
    taggedWords: List<String>,
    onRemove: (String) -> Unit,
    onAdd: (String) -> Unit
) {

    var word by remember { mutableStateOf("") }
    var isExpand by remember { mutableStateOf(false) }
    var textFieldPositionX by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
    ) {
        SuggestionLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
                .align(Alignment.CenterHorizontally)
                .shadow(3.dp, shape = RoundedCornerShape(8.dp))
                .background(PrimarySurface, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .padding(vertical = 4.dp)
        ) {
            taggedWords.forEach { word ->
                TaggedWordItem(word = word) {
                    onRemove(word)
                }
            }
            TextFieldInnerPadding(
                value = word,
                onValueChange = {
                    word = it
                    isExpand = true
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = GoldColor,
                    errorCursorColor = GoldColor,
                    selectionColors = TextSelectionColors(
                        handleColor = GoldColor,
                        backgroundColor = GoldColor.copy(alpha = 0.3f),
                    )
                ),
                innerPadding = PaddingValues(8.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (word.isNotEmpty()) {
                            onAdd(word)
                            word = ""
                        }
                    }
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        "Kelime girin",
                        color = TextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusEvent { isExpand = it.isFocused }
                    .then(
                        if (taggedWords.isNotEmpty()) {
                            Modifier.width(100.dp)
                        } else {
                            Modifier.fillMaxWidth()
                        }
                    )
                    .onGloballyPositioned { coordinates ->
                        textFieldPositionX = coordinates.positionInWindow().x / 2
                    }
            )
        }
        HorizontalDivider(
            color = GoldColor,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 22.dp)
        )

        AnimatedVisibility(visible = taggedWords.isEmpty()) {
            MotikocDefaultErrorField(
                modifier = Modifier.padding(horizontal = 16.dp),
                errorMessage = "En az 1 kelime girmelisiniz!"
            )
        }

        AnimatedVisibility(
            visible = isExpand,
            modifier = Modifier.graphicsLayer { translationX = textFieldPositionX }) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .graphicsLayer {
                        translationY = 50f
                    },
                shape = SuggestionShape(12f),
            ) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 150.dp),
                ) {
                    items(
                        dataList.filter { datas ->
                            !taggedWords.contains(datas) &&
                                    (datas.lowercase()
                                        .contains(word.lowercase()) || datas.lowercase()
                                        .contains("others"))
                        }.sortedByDescending {
                            word.lowercase().firstOrNull() == it.lowercase().first()
                        }
                    ) {
                        ItemsCategory(title = it) { title ->
                            onAdd(title)
                            word = ""
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ItemsCategory(
    title: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                onSelect(title)
            }
            .fillMaxWidth(0.5f)
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }
}

@Composable
fun TaggedWordItem(
    word: String,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = word,
            fontSize = 16.sp,
            color = TextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = 1.dp
            )
        )
        Icon(
            Icons.Default.Clear,
            tint = TextColor,
            contentDescription = "",
            modifier = Modifier
                .size(28.dp)
                .padding(
                    start = 1.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
                .clickable {
                    onRemove()
                }
        )
    }
}