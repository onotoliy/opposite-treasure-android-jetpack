package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.VersionVO

@Dao
interface VersionDAO {
    @Query("SELECT * FROM treasure_version WHERE type = :pk")
    fun get(pk: String): VersionVO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: VersionVO)
}
