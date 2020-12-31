package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO

@Dao
interface TransactionDAO: WriteDAO<TransactionVO> {
    @Query("SELECT * FROM treasure_transaction WHERE uuid = :pk")
    override fun get(pk: String): LiveData<TransactionVO>

    @Query("SELECT * FROM treasure_transaction WHERE local <> 3 ORDER BY milliseconds DESC LIMIT :offset, :numberOfRows")
    override fun getAll(offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT COUNT(*) FROM treasure_transaction WHERE local <> 3")
    override fun count(): LiveData<Long>

    @Query("SELECT * FROM treasure_transaction WHERE person_uuid = :person AND local <> 3 ORDER BY milliseconds DESC LIMIT :offset, :numberOfRows")
    fun getByPersonAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT COUNT(*) FROM treasure_transaction WHERE person_uuid = :person AND local <> 3")
    fun countByPerson(person: String): LiveData<Long>

    @Query("SELECT * FROM treasure_transaction WHERE event_uuid = :event AND local <> 3 ORDER BY milliseconds DESC LIMIT :offset, :numberOfRows")
    fun getByEventAll(event: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT COUNT(*) FROM treasure_transaction WHERE person_uuid = :event AND local <> 3")
    fun countByEvent(event: String): LiveData<Long>

    @Query("SELECT * FROM treasure_transaction where local in (1, 2)")
    override fun getAllLocal(): List<TransactionVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun replace(vo: TransactionVO)

    @Query("DELETE FROM treasure_transaction where uuid = :uuid")
    fun delete(uuid: String)

    @Query("DELETE FROM treasure_transaction where local = :local")
    fun clean(local: Int)

    @Query("DELETE FROM treasure_transaction")
    override fun clean()
}
