package com.example.alkewalletm5.data.local.databse

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migraciones {

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Crear la nueva tabla local_users
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS local_users (
                        id INTEGER PRIMARY KEY NOT NULL,
                        nombre TEXT NOT NULL,
                        apellido TEXT NOT NULL,
                        email TEXT NOT NULL,
                        imgPerfil TEXT
                    )
                """)
            }
        }
    }

}