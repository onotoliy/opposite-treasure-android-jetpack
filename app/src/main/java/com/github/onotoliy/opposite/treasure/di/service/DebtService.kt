package com.github.onotoliy.opposite.treasure.di.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.*
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.DebtVO
import com.github.onotoliy.opposite.treasure.di.resource.DebtResource
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

class DebtService @Inject constructor(
    private val dao: DebtDAO,
    private val retrofit: DebtResource
): AbstractService<Debt>()  {

    fun getDebtorAll(event: String, offset: Int, numberOfRows: Int): LiveDataPage<DebtVO> {
        val content = dao.getAll(
            getAll(table = "treasure_debt", whereCause = "event_uuid = ?",
                whereArgs = arrayOf(event),
                limit = "$offset, $numberOfRows"
            )
        )
//        val total = dao.count(
//            count(table = "treasure_debt", whereCause = "event_uuid = ?",
//                whereArgs = arrayOf(event)
//            )
//        )

        return LiveDataPage<DebtVO>(
            total = MutableLiveData<Long>(0L),
            content = content,
            offset = offset,
            numberOfRows = numberOfRows
        )
    }

    fun getDebtAll(deposit: String, offset: Int, numberOfRows: Int): LiveDataPage<DebtVO> {
        val content = dao.getAll(
            getAll(table = "treasure_debt", whereCause = "deposit_user_uuid = ?",
                whereArgs = arrayOf(deposit),
                limit = "$offset, $numberOfRows"
            )
        )
//        val total = dao.count(
//            count(table = "treasure_debt", whereCause = "deposit_user_uuid = ?",
//                whereArgs = arrayOf(deposit)
//            )
//        )

        return LiveDataPage<DebtVO>(
            total = MutableLiveData<Long>(0L),
            content = content,
            offset = offset,
            numberOfRows = numberOfRows
        )
    }

    override fun sync() = syncPage { offset, numberOfRows ->
        retrofit.sync(version = 0, offset = offset, numberOfRows = numberOfRows).execute()
    }

    override fun replace(dto: Debt) = dao.replace(dto.toVO())

}

class LiveDataPage<T>(
    val content: LiveData<List<T>>,
    val total: LiveData<Long>,
    val offset: Int,
    val numberOfRows: Int
)

fun DebtVO.toDTO() = this.run {
    Debt(
        deposit = Deposit(
            person = Option(depositUserUUID, depositUsername),
            deposit = depositDeposit
        ),
        event = Event(
            uuid = eventUUID,
            name = eventName,
            contribution = eventContribution,
            total = eventTotal,
            deadline = eventDeadline,
            creationDate = eventCreationDate,
            author = Option(eventAuthorUUID, eventAuthorName),
            deletionDate = eventDeletionDate
        )
    )
}

fun Debt.toVO() = DebtVO(
    depositUserUUID = deposit.uuid,
    depositUsername = deposit.name,
    depositDeposit = deposit.deposit,
    eventUUID = event.uuid,
    eventName = event.name,
    eventContribution = event.contribution,
    eventTotal = event.total,
    eventDeadline = event.deadline,
    eventCreationDate = event.creationDate,
    eventAuthorUUID = event.author.uuid,
    eventAuthorName = event.author.name,
    eventDeletionDate = event.deletionDate
)