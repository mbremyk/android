package com.example.e7;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    static final String TABLE_AUTHOR = "author";
    static final String TABLE_TITLE = "book";
    static final String TABLE_AUTHOR_BOOK = "author_book";
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_TITLE = "title";
    static final String KEY_AUTHOR = "author_id";
    static final String KEY_BOOK = "book_id";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "BooksDb";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE1 = "create table " + TABLE_AUTHOR
            + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_NAME + " text unique not null);";
    static final String DATABASE_CREATE2 = "create table " + TABLE_TITLE
            + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_TITLE + " text unique not null);";
    static final String DATABASE_CREATE3 = "create table " + TABLE_AUTHOR_BOOK
            + " (" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_BOOK + " numeric, "
            + KEY_AUTHOR + " numeric,"
            + "FOREIGN KEY(" + KEY_AUTHOR + ") REFERENCES " + TABLE_AUTHOR + "(" + KEY_ROWID + "), "
            + "FOREIGN KEY(" + KEY_BOOK + ") REFERENCES " + TABLE_TITLE + "(" + KEY_ROWID + ")"
            + ");";
    static final String AUTHOR_BOOK_SELECTION =
            "select " + TABLE_TITLE + "." + KEY_ROWID + ", " + TABLE_TITLE + "." + KEY_TITLE + " from "
                    + TABLE_AUTHOR + ", " + TABLE_TITLE + ", " + TABLE_AUTHOR_BOOK + " where "
                    + TABLE_AUTHOR + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_AUTHOR + " and "
                    + TABLE_TITLE + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_BOOK + " and "
                    + TABLE_AUTHOR + "." + KEY_NAME + "=?";
    static final String BOOK_AUTHOR_SELECTION =
            "select " + TABLE_AUTHOR + "." + KEY_ROWID + ", " + TABLE_AUTHOR + "." + KEY_NAME + " from "
                    + TABLE_AUTHOR + ", " + TABLE_TITLE + ", " + TABLE_AUTHOR_BOOK + " where "
                    + TABLE_AUTHOR + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_AUTHOR + " and "
                    + TABLE_TITLE + "._id=" + TABLE_AUTHOR_BOOK + ".book_id and "
                    + TABLE_TITLE + "." + KEY_TITLE + "=?";
    static final String ALL_AUTHOR_BOOK_SELECTION =
            "select " + TABLE_AUTHOR + "." + KEY_NAME + "," + TABLE_TITLE + "." + KEY_TITLE + " from "
                    + TABLE_AUTHOR + ", " + TABLE_TITLE + ", " + TABLE_AUTHOR_BOOK + " where "
                    + TABLE_AUTHOR + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_AUTHOR + " and "
                    + TABLE_TITLE + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_BOOK + ";";

    public DatabaseManager(Context context) throws Exception {
        super(context,
                /*db name=*/ DATABASE_NAME,
                /*cursorFactory=*/ null,
                /*db version=*/DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("db", "onCreate");
        db.execSQL(DATABASE_CREATE1);
        db.execSQL(DATABASE_CREATE2);
        db.execSQL(DATABASE_CREATE3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        Log.d("db", "onUpdate");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TITLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR);
        // re-create the table
        onCreate(db);
    }

    public void clean() {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 0, 0);
        db.close();
    }

    private long insertAuthor(String name, SQLiteDatabase db) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        return db.insert(TABLE_AUTHOR, null, initialValues);
    }

    private long insertBook(String title, SQLiteDatabase db) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        return db.insert(TABLE_TITLE, null, initialValues);
    }

    private long insertAuthor_Book(long author, long book, SQLiteDatabase db) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_AUTHOR, author);
        initialValues.put(KEY_BOOK, book);
        return db.insert(TABLE_AUTHOR_BOOK, null, initialValues);
    }

    public long insert(String author, String title) {
        long bookIndex = this.getBookIndex(author, title);
        if (bookIndex >= 0) {
            return -1;
        }
        SQLiteDatabase db = null;
        db = this.getWritableDatabase();
        Cursor cursor = null;
        long author_id;
        if (author.isEmpty()) author_id = -1;
        else {
            cursor =
                    db.query(true, TABLE_AUTHOR, new String[]{KEY_ROWID, KEY_NAME}, KEY_NAME + "='" + author + "'", null, null, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                author_id = insertAuthor(author, db);
            } else {
                cursor.moveToFirst();
                author_id = cursor.getLong(0);
            }
        }
        long title_id;
        if (title.isEmpty()) title_id = -1;
        else {
            cursor = db.query(TABLE_TITLE, new String[]{KEY_ROWID, KEY_TITLE}, KEY_TITLE + "='" + title + "'", null, null, null, null, null);

            if (cursor == null || cursor.getCount() == 0) {
                title_id = insertBook(title, db);
            } else {
                cursor.moveToFirst();
                title_id = cursor.getLong(0);
            }
        }
        long id;
        if (author_id >= 0 && title_id >= 0) id = insertAuthor_Book(author_id, title_id, db);
        else id = -1;
        if(cursor != null)cursor.close();
        db.close();
        return id;
    }

    public ArrayList<String> getAllAuthors() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_AUTHOR, new String[]{KEY_ROWID, KEY_NAME}, null, null, null, null, null, null);
        ArrayList<String> res = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                res.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return res;
    }

    public ArrayList<String> getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_TITLE, new String[]{KEY_ROWID, KEY_TITLE}, null, null, null, null, null, null);
        ArrayList<String> res = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                res.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return res;
    }

    public ArrayList<String> getAllBooksAndAuthors() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.rawQuery(ALL_AUTHOR_BOOK_SELECTION, new String[]{});
        ArrayList<String> res = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                res.add(cursor.getString(0) + ": " + cursor.getString(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return res;
    }

    public ArrayList<String> getBooksByAuthor(String author) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> res = new ArrayList<>();
        Cursor cursor =
                db.rawQuery(AUTHOR_BOOK_SELECTION, new String[]{author});
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                res.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return res;
    }

    public ArrayList<String> getAuthorsByBook(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> res = new ArrayList<>();
        Cursor cursor =
                db.rawQuery(BOOK_AUTHOR_SELECTION, new String[]{title});
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                res.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return res;
    }

    public void readDbFromFile(Activity activity, int id) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(activity.getResources().openRawResource(id)))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] params = line.trim().split(",", 2);
                for (int i = 0; i < params.length; ++i) {
                    params[i] = params[i].trim();
                }
                this.insert(params[0], params[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getBookIndex(String author, String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        long res = -1;
        String query = "select " + TABLE_AUTHOR_BOOK + "." + KEY_ROWID + " from "
                + TABLE_AUTHOR + ", " + TABLE_TITLE + ", " + TABLE_AUTHOR_BOOK + " where "
                + TABLE_AUTHOR + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_AUTHOR + " and "
                + TABLE_TITLE + "." + KEY_ROWID + "=" + TABLE_AUTHOR_BOOK + "." + KEY_BOOK + " and "
                + TABLE_AUTHOR + "." + KEY_NAME + "=? and " + TABLE_TITLE + "." + KEY_TITLE + "=?;";
        Cursor cursor = db.rawQuery(query, new String[]{author, title});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            res = cursor.getLong(0);
        }
        cursor.close();
        db.close();
        return res;
    }
}
