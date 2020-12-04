package com.github.onotoliy.opposite.treasure.di.service

import androidx.lifecycle.LiveData
import androidx.work.impl.utils.LiveDataUtils
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.DepositVO
import com.github.onotoliy.opposite.treasure.di.resource.DepositResource
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

class DepositService @Inject constructor(
    private val dao: DepositDAO,
    private val retrofit: DepositResource
): AbstractService<Deposit>() {

    fun get(pk: String): LiveData<DepositVO> = dao.get(pk)

    fun getAll(offset: Int, numberOfRows: Int): Page<Deposit> {
        val content = dao.getAll(
            getAll(
                table = "treasure_deposit",
                whereCause = "1 = 1",
                whereArgs = arrayOf(),
                limit = "$offset, $numberOfRows"
            )
        )
        val total = dao.count(
            count(
                table = "treasure_deposit",
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
            table = "treasure_deposit",
            whereCause = name?.let { "user_name LIKE ?" } ?: "1 = 1",
            whereArgs = name?.let { arrayOf("%$it%") } ?: arrayOf(),
        )
    ).map { Option(it.uuid, it.name) } ?: listOf()

    override fun replace(dto: Deposit) = dao.replace(dto.toVO())

    override fun sync() = syncPage { offset, numberOfRows ->
        retrofit.sync(version = 0, offset = offset, numberOfRows = numberOfRows).execute()
    }


}

fun DepositVO.toDTO() =
    Deposit(
        person = Option(uuid, name),
        deposit = deposit
    )

fun Deposit.toVO() = DepositVO(
    uuid = uuid,
    name = name,
    deposit = deposit,
)