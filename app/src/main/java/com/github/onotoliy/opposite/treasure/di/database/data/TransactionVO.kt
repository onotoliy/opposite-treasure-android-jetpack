package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.core.HasCreationDate
import com.github.onotoliy.opposite.data.core.HasDeletionDate
import com.github.onotoliy.opposite.data.core.HasName
import com.github.onotoliy.opposite.data.core.HasUUID

@Entity(tableName = "treasure_transaction")
data class TransactionVO(
    @PrimaryKey
    @ColumnInfo(name = "uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var uuid: String = "",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var name: String = "",
    @ColumnInfo(name = "cash", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var cash: String = "0.0",
    @ColumnInfo(name = "transaction_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var transactionDate: String = "",
    @ColumnInfo(name = "creation_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var creationDate: String = "",
    @ColumnInfo(name = "deletion_date", typeAffinity = ColumnInfo.TEXT)
    override var deletionDate: String? = null,
    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.TEXT, defaultValue = "NONE")
    var type: String = "NONE",
    @Embedded(prefix = "author_")
    var author: OptionVO = OptionVO(),
    @Embedded(prefix = "event_")
    var event: OptionVO? = null,
    @Embedded(prefix = "person_")
    var person: OptionVO? = null
) : HasUUID, HasName, HasCreationDate, HasDeletionDate

fun Transaction.toVO(): TransactionVO = TransactionVO(
    uuid = uuid,
    name = name,
    creationDate = creationDate,
    deletionDate = deletionDate,
    author = author.toVO(),
    event = event?.toVO(),
    person = person?.toVO(),
    cash = cash,
    type = type.name,
    transactionDate = transactionDate
)