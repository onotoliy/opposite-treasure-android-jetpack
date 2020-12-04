package com.github.onotoliy.opposite.treasure.di.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import dagger.Module
import dagger.Provides

@Entity(tableName = "treasure_cashbox")
class CashboxVO(
    @PrimaryKey
    @ColumnInfo(name = "pk", typeAffinity = ColumnInfo.TEXT, defaultValue = "1")
    var pk: String = "1",
    @ColumnInfo(name = "deposit", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var deposit: String = "0.0",
    @ColumnInfo(name = "last_update_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var lastUpdateDate: String = ""
)

@Entity(tableName = "treasure_debt")
class DebtVO(
    @PrimaryKey
    @ColumnInfo(name = "pk", typeAffinity = ColumnInfo.TEXT, defaultValue = "1")
    var pk: String = "1",
    @ColumnInfo(name = "event_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var eventUUID: String = "",
    @ColumnInfo(name = "event_total", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var eventTotal: String = "0.0",
    @ColumnInfo(name = "event_contribution", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var eventContribution: String = "0.0",
    @ColumnInfo(name = "event_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var eventName: String = "",
    @ColumnInfo(name = "event_deadline", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var eventDeadline: String = "",
    @ColumnInfo(name = "event_creation_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var eventCreationDate: String = "",
    @ColumnInfo(name = "event_deletion_date", typeAffinity = ColumnInfo.TEXT)
    var eventDeletionDate: String? = null,
    @ColumnInfo(name = "event_author_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var eventAuthorUUID: String = "",
    @ColumnInfo(name = "event_author_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var eventAuthorName: String = "",
    @ColumnInfo(name = "deposit_user_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var depositUserUUID: String = "",
    @ColumnInfo(name = "deposit_user_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var depositUsername: String = "",
    @ColumnInfo(name = "deposit_deposit", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var depositDeposit: String = "0.0"
)

@Entity(tableName = "treasure_deposit")
class DepositVO(
    @PrimaryKey
    @ColumnInfo(name = "user_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "1")
    var uuid: String = "1",
    @ColumnInfo(name = "user_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var name: String = "",
    @ColumnInfo(name = "deposit", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var deposit: String = "0.0"
)

@Entity(tableName = "treasure_event")
class EventVO(
    @PrimaryKey
    @ColumnInfo(name = "uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var uuid: String = "",
    @ColumnInfo(name = "total", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var total: String = "0.0",
    @ColumnInfo(name = "contribution", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var contribution: String = "0.0",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var name: String = "",
    @ColumnInfo(name = "deadline", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var deadline: String = "",
    @ColumnInfo(name = "creation_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var creationDate: String = "",
    @ColumnInfo(name = "deletion_date", typeAffinity = ColumnInfo.TEXT)
    var deletionDate: String? = null,
    @ColumnInfo(name = "author_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var authorUUID: String = "",
    @ColumnInfo(name = "author_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var authorName: String = "",
)

@Entity(tableName = "treasure_transaction")
class TransactionVO(
    @PrimaryKey
    @ColumnInfo(name = "uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var uuid: String = "",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var name: String = "",
    @ColumnInfo(name = "cash", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var cash: String = "0.0",
    @ColumnInfo(name = "transaction_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var transactionDate: String = "",
    @ColumnInfo(name = "creation_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var creationDate: String = "",
    @ColumnInfo(name = "deletion_date", typeAffinity = ColumnInfo.TEXT)
    var deletionDate: String? = null,
    @ColumnInfo(name = "author_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var authorUUID: String = "",
    @ColumnInfo(name = "author_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var authorName: String = "",
    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.TEXT, defaultValue = "NONE")
    var type: String = "NONE",
    @ColumnInfo(name = "event_uuid", typeAffinity = ColumnInfo.TEXT)
    var eventUUID: String? = null,
    @ColumnInfo(name = "event_name", typeAffinity = ColumnInfo.TEXT)
    var eventName: String? = null,
    @ColumnInfo(name = "person_uuid", typeAffinity = ColumnInfo.TEXT)
    var personUUID: String? = null,
    @ColumnInfo(name = "person_name", typeAffinity = ColumnInfo.TEXT)
    var personName: String? = null,
)

@Dao
interface CashboxDAO {

    @Query("SELECT * FROM treasure_cashbox")
    fun get(): LiveData<CashboxVO?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: CashboxVO)

}

@Dao
interface DebtDAO {
    @RawQuery(observedEntities = [DebtVO::class])
    fun getAll(query: SupportSQLiteQuery): LiveData<List<DebtVO>>

    @RawQuery()
    fun count(query: SupportSQLiteQuery): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: DebtVO)
}

@Dao
interface DepositDAO {
    @Query("SELECT * FROM treasure_deposit WHERE user_uuid = :pk")
    fun get(pk: String): LiveData<DepositVO>

    @RawQuery()
    fun getAll(query: SupportSQLiteQuery): List<DepositVO>

    @RawQuery()
    fun count(query: SupportSQLiteQuery): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: DepositVO)
}

@Dao
interface EventDAO {
    @Query("SELECT * FROM treasure_event WHERE uuid = :pk")
    fun get(pk: String): EventVO

    @RawQuery()
    fun getAll(query: SupportSQLiteQuery): List<EventVO>

    @RawQuery()
    fun count(query: SupportSQLiteQuery): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: EventVO)
}

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM treasure_transaction WHERE uuid = :pk")
    fun get(pk: String): TransactionVO

    @Query("SELECT * FROM treasure_transaction LIMIT :offset, :numberOfRows")
    fun getAll(offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT count(*) FROM treasure_transaction")
    fun count(): LiveData<Long>

    @Query("SELECT * FROM treasure_transaction WHERE person_uuid = :person LIMIT :offset, :numberOfRows")
    fun getByPersonAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT count(*) FROM treasure_transaction WHERE person_uuid = :person")
    fun countByPerson(person: String): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: TransactionVO)
}

@Database(
    entities = [CashboxVO::class, DebtVO::class, DepositVO::class, EventVO::class, TransactionVO::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cashboxDAO(): CashboxDAO
    abstract fun debtDAO(): DebtDAO
    abstract fun depositDAO(): DepositDAO
    abstract fun eventDAO(): EventDAO
    abstract fun transactionDAO(): TransactionDAO
}

@Module
class RoomModule(private val demoDatabase: AppDatabase) {

    @Provides
    fun providesCashboxDAO(): CashboxDAO {
        return demoDatabase.cashboxDAO()
    }

    @Provides
    fun providesDebtDAO(): DebtDAO {
        return demoDatabase.debtDAO()
    }
    @Provides
    fun providesDepositDAO(): DepositDAO {
        return demoDatabase.depositDAO()
    }
    @Provides
    fun providesEventDAO(): EventDAO {
        return demoDatabase.eventDAO()
    }
    @Provides
    fun providesTransactionDAO(): TransactionDAO {
        return demoDatabase.transactionDAO()
    }
}
