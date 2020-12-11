package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO

@Dao
interface CashboxDAO: WriteDAO<CashboxVO> {

    @Query("SELECT * FROM treasure_cashbox")
    fun get(): LiveData<CashboxVO?>

    @Query("SELECT * FROM treasure_cashbox")
    override fun getAllLocal(): List<CashboxVO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun replace(vo: CashboxVO)

    @Query("DELETE FROM treasure_cashbox")
    override fun clean()
}
