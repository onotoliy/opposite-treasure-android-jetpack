package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO

@Dao
interface EventDAO: WriteDAO<EventVO> {
    @Query("SELECT * FROM treasure_event WHERE uuid = :pk")
    fun get(pk: String): LiveData<EventVO>

    @Query("SELECT * FROM treasure_event LIMIT :offset, :numberOfRows")
    fun getAll(offset: Int, numberOfRows: Int): LiveData<List<EventVO>>

    @Query("SELECT COUNT(*) FROM treasure_event")
    fun count(): LiveData<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun replace(vo: EventVO)
}

