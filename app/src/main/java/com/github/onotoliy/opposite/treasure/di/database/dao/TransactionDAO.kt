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
    fun get(pk: String): LiveData<TransactionVO>

    @Query("SELECT * FROM treasure_transaction LIMIT :offset, :numberOfRows")
    fun getAll(offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT COUNT(*) FROM treasure_transaction")
    fun count(): LiveData<Long>

    @Query("SELECT * FROM treasure_transaction WHERE person_uuid = :person LIMIT :offset, :numberOfRows")
    fun getByPersonAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT COUNT(*) FROM treasure_transaction WHERE person_uuid = :person")
    fun countByPerson(person: String): LiveData<Long>

    @Query("SELECT * FROM treasure_transaction WHERE event_uuid = :event LIMIT :offset, :numberOfRows")
    fun getByEventAll(event: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>>

    @Query("SELECT COUNT(*) FROM treasure_transaction WHERE person_uuid = :event")
    fun countByEvent(event: String): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun replace(vo: TransactionVO)
}