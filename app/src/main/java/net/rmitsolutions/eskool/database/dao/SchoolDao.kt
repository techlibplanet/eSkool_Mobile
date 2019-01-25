package net.rmitsolutions.eskool.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.eskool.models.SchoolAddress

/**
 * Created by Madhu on 22-Aug-17.
 */
@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchoolAddress(schoolAddress: SchoolAddress)

    @Query("select * from school_address where id=:schoolId")
    fun getSchoolAddress(schoolId: Int): SchoolAddress?

    @Query("delete from school_address")
    fun deleteSchoolAddress()
}