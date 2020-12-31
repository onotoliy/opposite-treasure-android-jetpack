package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO

@Dao
interface CashboxDAO {

    @Query("SELECT * FROM treasure_cashbox")
    fun get(): LiveData<CashboxVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(vo: CashboxVO)
}
