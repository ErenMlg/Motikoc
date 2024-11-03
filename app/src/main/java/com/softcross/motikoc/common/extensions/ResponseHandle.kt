package com.softcross.motikoc.common.extensions

import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.LessonItem


fun <I : Any, O : Any> ResponseState<I>.mapResponse(mapper: I.() -> O): ResponseState<O> {
    return when (this) {
        is ResponseState.Error -> ResponseState.Error(this.exception)
        is ResponseState.Success -> ResponseState.Success(mapper.invoke(this.result))
        ResponseState.Loading -> ResponseState.Loading
    }
}

fun HashMap<*,*>.toLessonItem(): LessonItem {
    return LessonItem(
        lessonEmptyCount = this["lessonEmptyCount"].toString().toInt(),
        lessonCorrectCount = this["lessonCorrectCount"].toString().toInt(),
        lessonWrongCount = this["lessonWrongCount"].toString().toInt()
    )
}