package net.rmitsolutions.eskool.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.eskool.models.CalenderEventEntity

/**
 * Created by SC368081 on 2/8/2018.
 */
@Dao
interface CalenderEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCalenderEvent(calenderEvent:CalenderEventEntity)

    @Query("select * from calender_events where eventDate between :from and :to  ")
    fun getAllCalederEvents(from:Long,to:Long):List<CalenderEventEntity>

    @Query("delete from calender_events where eventDate between :from and :to ")
    fun deleteEvents(from:Long,to:Long)
}