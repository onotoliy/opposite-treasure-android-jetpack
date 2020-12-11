package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO

@Dao
interface DepositDAO: WriteDAO<DepositVO> {
    @Query("SELECT * FROM treasure_deposit WHERE user_uuid = :pk")
    fun get(pk: String): LiveData<DepositVO?>

    @Query("SELECT * FROM treasure_deposit LIMIT :offset, :numberOfRows")
    fun getAll(offset: Int, numberOfRows: Int): LiveData<List<DepositVO>>

    @Query("SELECT COUNT(*) FROM treasure_deposit")
    fun count(): LiveData<Long>

    @Query("SELECT * FROM treasure_deposit")
    override fun getAllLocal(): List<DepositVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun replace(vo: DepositVO)

    @Query("DELETE FROM treasure_deposit")
    override fun clean()
}
