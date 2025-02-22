package com.example.movieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Movie::class], version = 8, exportSchema = false) // Increased version
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MovieDatabase::class.java,
                    "movie_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5,
                        MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8) // Added new migration
                    .build()
                    .also { instance = it }
            }
        }

        // Migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies ADD COLUMN release_date TEXT DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN original_title TEXT DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN backdrop_path TEXT DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN original_language TEXT DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN adult INTEGER DEFAULT 0")
                db.execSQL("ALTER TABLE movies ADD COLUMN video INTEGER DEFAULT 0")
                db.execSQL("ALTER TABLE movies ADD COLUMN popularity REAL DEFAULT 0.0")
                db.execSQL("ALTER TABLE movies ADD COLUMN vote_average REAL DEFAULT 0.0")
                db.execSQL("ALTER TABLE movies ADD COLUMN vote_count INTEGER DEFAULT 0")
            }
        }

        // New Migration from version 2 to 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies ADD COLUMN release_date TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN original_title TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN backdrop_path TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN original_language TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE movies ADD COLUMN adult INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE movies ADD COLUMN video INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE movies ADD COLUMN popularity REAL NOT NULL DEFAULT 0.0")
                db.execSQL("ALTER TABLE movies ADD COLUMN vote_average REAL NOT NULL DEFAULT 0.0")
                db.execSQL("ALTER TABLE movies ADD COLUMN vote_count INTEGER NOT NULL DEFAULT 0")
            }
        }
        // Migration from version 3 to 4
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies ADD COLUMN favorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies ADD COLUMN isUpcoming INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies ADD COLUMN isPopular INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies RENAME COLUMN posterPath TO poster_path")
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE movies RENAME COLUMN release_date TO releaseDate")
                db.execSQL("ALTER TABLE movies RENAME COLUMN original_title TO originalTitle")
                db.execSQL("ALTER TABLE movies RENAME COLUMN backdrop_path TO backdropPath")
                db.execSQL("ALTER TABLE movies RENAME COLUMN original_language TO originalLanguage")
                db.execSQL("ALTER TABLE movies RENAME COLUMN vote_average TO voteAverage")
                db.execSQL("ALTER TABLE movies RENAME COLUMN vote_count TO voteCount")
                db.execSQL("ALTER TABLE movies RENAME COLUMN poster_path TO posterPath")

            }
        }


    }
}
