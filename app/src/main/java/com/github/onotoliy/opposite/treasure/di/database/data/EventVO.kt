package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.core.HasCreationDate
import com.github.onotoliy.opposite.data.core.HasDeletionDate
import com.github.onotoliy.opposite.data.core.HasName
import com.github.onotoliy.opposite.data.core.HasUUID

@Entity(tableName = "treasure_event")
data class EventVO(
    @PrimaryKey
    @ColumnInfo(name = "uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var uuid: String = "",
    @ColumnInfo(name = "total", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var total: String = "0.0",
    @ColumnInfo(name = "contribution", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var contribution: String = "0.0",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var name: String = "",
    @ColumnInfo(name = "deadline", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var deadline: String = "",
    @ColumnInfo(name = "creation_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var creationDate: String = "",
    @ColumnInfo(name = "deletion_date", typeAffinity = ColumnInfo.TEXT)
    override var deletionDate: String? = null,
    @Embedded(prefix = "author_")
    var author: OptionVO = OptionVO(),
    @ColumnInfo(name = "local", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0")
    var local: Int = 0,
    @ColumnInfo(name = "updated", typeAffinity = ColumnInfo.INTEGER, defaultValue = "0")
    var updated: Int = 0
) : HasUUID, HasName, HasCreationDate, HasDeletionDate

fun Event.toVO(): EventVO = EventVO(
    uuid = uuid,
    total = total,
    contribution = contribution,
    name = name,
    deadline = deadline,
    creationDate = creationDate,
    deletionDate = deletionDate,
    author = author.toVO()
)

fun EventVO.toDTO(): Event = Event(
    uuid = uuid,
    total = total,
    contribution = contribution,
    name = name,
    deadline = deadline,
    creationDate = creationDate,
    deletionDate = deletionDate,
    author = author.toDTO()
)