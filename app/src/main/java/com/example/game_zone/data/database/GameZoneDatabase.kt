package com.example.game_zone.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.game_zone.data.dao.ProductoDao
import com.example.game_zone.data.dao.UsuarioDao
import com.example.game_zone.data.entity.UsuarioEntity
import com.example.game_zone.data.entity.ProductoEntity

@Database(
    entities = [UsuarioEntity::class, ProductoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GameZoneDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: GameZoneDatabase? = null

        fun getDatabase(context: Context): GameZoneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameZoneDatabase::class.java,
                    "game_zone_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}