package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO

@Dao
interface DebtDAO: WriteDAO<DebtVO> {
    @Query("SELECT * FROM treasure_debt WHERE deposit_user_uuid = :person LIMIT :offset, :numberOfRows")
    fun getByPersonAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<DebtVO>>

    @Query("SELECT COUNT(*) FROM treasure_debt WHERE deposit_user_uuid = :person")
    fun countByPerson(person: String): LiveData<Long>

    @Query("SELECT * FROM treasure_debt WHERE event_uuid = :event LIMIT :offset, :numberOfRows")
    fun getByEventAll(event: String, offset: Int, numberOfRows: Int): LiveData<List<DebtVO>>

    @Query("SELECT COUNT(*) FROM treasure_debt WHERE event_uuid = :event")
    fun countByEvent(event: String): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun replace(vo: DebtVO)
}
