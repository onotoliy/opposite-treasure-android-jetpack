package com.github.onotoliy.opposite.treasure.di.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDatabase(context: Context) : SQLiteOpenHelper(context, "MyDatabase", null, 4) {

    override fun onCreate(db: SQLiteDatabase?) {
        listOf(
            """
                |CREATE TABLE IF NOT EXISTS treasure_deposit (
                |    user_uuid TEXT PRIMARY KEY, 
                |    user_name TEXT, deposit NUMERIC DEFAULT 0
                |)
            """.trimMargin(),
            """
                |CREATE TABLE IF NOT EXISTS treasure_cashbox (
                |    last_update_date TEXT,  
                |    deposit NUMERIC DEFAULT 0
                |)
            """.trimMargin(),
            """
                |CREATE TABLE IF NOT EXISTS treasure_event (
                |    uuid TEXT PRIMARY KEY, 
                |    total NUMERIC DEFAULT 0, 
                |    contribution NUMERIC DEFAULT 0, 
                |    name TEXT NOT NULL, 
                |    deadline TEXT NOT NULL, 
                |    creation_date TEXT NOT NULL, 
                |    author_uuid TEXT NOT NULL, 
                |    author_name TEXT NOT NULL
                |)
            """.trimMargin(),
            """
                |CREATE TABLE IF NOT EXISTS treasure_transaction (
                |    uuid TEXT PRIMARY KEY,
                |    name TEXT NOT NULL,
                |    cash NUMERIC DEFAULT 0,    
                |    creation_date TEXT NOT NULL,  
                |    author_uuid TEXT NOT NULL, 
                |    author_name TEXT NOT NULL,
                |    type TEXT NOT NULL,
                |    event_uuid TEXT, 
                |    event_name TEXT,
                |    person_uuid TEXT, 
                |    person_name TEXT
                |)
            """.trimMargin(),
            """
                |CREATE TABLE IF NOT EXISTS treasure_debt (
                |    user_uuid TEXT, 
                |    user_name TEXT,
                |    event_uuid TEXT, 
                |    event_name TEXT
                |)
            """.trimMargin(),
            """
                |CREATE TABLE IF NOT EXISTS treasure_debt (
                |    deposit_user_uuid TEXT PRIMARY KEY, 
                |    deposit_user_name TEXT, deposit NUMERIC DEFAULT 0,
                |    event_uuid TEXT PRIMARY KEY, 
                |    event_total NUMERIC DEFAULT 0, 
                |    event_contribution NUMERIC DEFAULT 0, 
                |    event_name TEXT NOT NULL, 
                |    event_deadline TEXT NOT NULL, 
                |    event_creation_date TEXT NOT NULL, 
                |    event_author_uuid TEXT NOT NULL, 
                |    event_author_name TEXT NOT NULL
                |)
            """.trimIndent()
        ).forEach {
            db?.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }
}
