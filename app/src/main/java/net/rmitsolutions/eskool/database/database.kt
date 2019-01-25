package net.rmitsolutions.eskool.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import net.rmitsolutions.eskool.database.dao.CalenderEventDao

import net.rmitsolutions.eskool.database.dao.SchoolDao
import net.rmitsolutions.eskool.database.dao.StudentDao
import net.rmitsolutions.eskool.models.CalenderEventEntity

import net.rmitsolutions.eskool.models.SchoolAddress
import net.rmitsolutions.eskool.utils.Converters

/**
 * Created by Madhu on 22-Aug-17.
 */
@Database(entities = arrayOf(SchoolAddress::class,CalenderEventEntity::class), version = 2)
@TypeConverters(Converters::class)
abstract class eSkoolDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun schoolDao(): SchoolDao
    abstract fun calenderEventDao(): CalenderEventDao


}
