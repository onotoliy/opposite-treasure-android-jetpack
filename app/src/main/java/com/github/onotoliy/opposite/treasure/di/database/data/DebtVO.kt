package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.*
import com.github.onotoliy.opposite.data.Debt

@Entity(tableName = "treasure_debt")
@Suppress(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class DebtVO(
    @PrimaryKey
    @ColumnInfo(name = "pk", typeAffinity = ColumnInfo.TEXT, defaultValue = "1")
    var pk: String = "1",
    @Embedded(prefix = "deposit_")
    var deposit: DepositVO = DepositVO(),
    @Embedded(prefix = "event_")
    var event: EventVO = EventVO()
)

fun Debt.toVO() = DebtVO(
    pk = deposit.uuid + "-" + event.uuid,
    deposit = deposit.toVO(),
    event = event.toVO(),
)