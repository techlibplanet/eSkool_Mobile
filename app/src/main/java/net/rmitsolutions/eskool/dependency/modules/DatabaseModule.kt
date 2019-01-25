package net.rmitsolutions.eskool.dependency.modules

import android.arch.persistence.room.Room
import android.content.Context
import net.rmitsolutions.eskool.database.eSkoolDatabase
import net.rmitsolutions.eskool.dependency.qualifiers.ApplicationContextQualifier
import net.rmitsolutions.eskool.dependency.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by Madhu on 22-Aug-17.
 */
@Module(includes = arrayOf(AppContextModule::class))
class DatabaseModule {
    @Provides
    @ApplicationScope
    fun eSkoolDatabase(@ApplicationContextQualifier context: Context): eSkoolDatabase {
        return Room.databaseBuilder(context, eSkoolDatabase::class.java, "eskool-db")
                .fallbackToDestructiveMigration()
                .build()
    }
}