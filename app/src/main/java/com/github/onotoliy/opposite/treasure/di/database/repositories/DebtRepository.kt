package com.github.onotoliy.opposite.treasure.di.database.repositories

import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import javax.inject.Inject

class DebtRepository @Inject constructor(
    version: VersionDAO,
    dao: DebtDAO
) : AbstractRepository<DebtVO, DebtDAO>("debt", version, dao)
