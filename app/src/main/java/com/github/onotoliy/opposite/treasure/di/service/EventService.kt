package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.EventVO
import com.github.onotoliy.opposite.treasure.di.resource.EventResource
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

class EventService @Inject constructor(
    private val dao: EventDAO,
    private val retrofit: EventResource
) : AbstractService<Event>() {

    fun get(pk: String): Event = dao.get(pk).toDTO() ?: Event()

    fun getAll(offset: Int, numberOfRows: Int): Page<Event> {
        val content = dao.getAll(
            getAll(
                table = "treasure_event",
                whereCause = "1 = 1",
                whereArgs = arrayOf(),
                limit = "$offset, $numberOfRows"
            )
        )
        val total = dao.count(
            count(
                table = "treasure_event",
                whereCause = "1 = 1",
                whereArgs = arrayOf()
            )
        )

        return Page(
            meta = Meta(
                total = total.toInt() ?: 0,
                paging = Paging(offset, numberOfRows)
            ),
            context = content.map { it.toDTO() } ?: listOf()
        )
    }

    fun getAllOption(name: String?): List<Option> = dao.getAll(
        getAll(
            table = "treasure_event",
            whereCause = name?.let { "name LIKE ?" } ?: "1 = 1",
            whereArgs = name?.let { arrayOf("%$it%") } ?: arrayOf(),
        )
    ).map { Option(it.uuid, it.name) } ?: listOf()

    override fun sync() = syncPage { offset, numberOfRows ->
        retrofit.sync(version = 0, offset = offset, numberOfRows = numberOfRows).execute()
    }

    override fun replace(dto: Event) = dao.replace(dto.toVO())

    private fun EventVO.toDTO() = Event(
        uuid = uuid,
        name = name,
        contribution = contribution,
        total = total,
        deadline = deadline,
        creationDate = creationDate,
        author = Option(authorUUID, authorName),
        deletionDate = deletionDate
    )

    private fun Event.toVO() = EventVO(
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
}