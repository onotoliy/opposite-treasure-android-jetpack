package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.di.database.EventVO

fun EventVO.toDTO() = Event(
    uuid = uuid,
    name = name,
    contribution = contribution,
    total = total,
    deadline = deadline,
    creationDate = creationDate,
    author = Option(authorUUID, authorName),
    deletionDate = deletionDate
)

fun Event.toVO() = EventVO(
    uuid = uuid,
    name = name,
    contribution = contribution,
    total = total,
    deadline = deadline,
    creationDate = creationDate,
    deletionDate = deletionDate,
    authorUUID = author.uuid,
    authorName = author.name,
)