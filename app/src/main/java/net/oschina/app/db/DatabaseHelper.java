package net.oschina.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建便签的数据库
 * 
 * @author kymjs
 * 
 *         update:2014-01-12 updateor: fireant 内容：修改为全应用数据库
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String OSC_DATABASE_NAME = "oschina";

    public static final String NOTE_TABLE_NAME = "osc_Notebook";

    public static final String CREATE_NOTE_TABLE = "create table "
            + NOTE_TABLE_NAME
            + " (_id integer primary key autoincrement, iid integer,"
            + " time varchar(10), date varchar(10), content text, color integer)";

    public static final String NEWS_LIST = "osc_news_list";

    public static final String CREATE_NEWS_LIST_TABLE = "create table "
            + NOTE_TABLE_NAME + "(" + "_id integer primary key autoincrement, "
            + "news_id interger, title varchar(10), " + ")";

    public DatabaseHelper(Context context) {
        super(context, OSC_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
        // db.execSQL(CREATE_NEWS_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}