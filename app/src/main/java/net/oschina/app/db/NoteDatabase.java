package net.oschina.app.db;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.bean.NotebookData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDatabase {
    private final DatabaseHelper dbHelper;

    public NoteDatabase(Context context) {
        super();
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * 增
     * 
     * @param data
     */
    public void insert(NotebookData data) {
        String sql = "insert into " + DatabaseHelper.NOTE_TABLE_NAME;

        sql += "(_id, iid, time, date, content, color) values(?, ?, ?, ?, ?, ?)";

        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        sqlite.execSQL(sql, new String[] { data.getId() + "",
                data.getIid() + "", data.getUnixTime() + "", data.getDate(),
                data.getContent(), data.getColor() + "" });
        sqlite.close();
    }

    /**
     * 删
     * 
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.NOTE_TABLE_NAME + " where _id=?");
        sqlite.execSQL(sql, new Integer[] { id });
        sqlite.close();
    }

    /**
     * 改
     * 
     * @param data
     */
    public void update(NotebookData data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("update " + DatabaseHelper.NOTE_TABLE_NAME + " set iid=?, time=?, date=?, content=?, color=? where _id=?");
        sqlite.execSQL(sql,
                new String[] { data.getIid() + "", data.getUnixTime() + "",
                        data.getDate(), data.getContent(),
                        data.getColor() + "", data.getId() + "" });
        sqlite.close();
    }

    public List<NotebookData> query() {
        return query(" ");
    }

    /**
     * 查
     * 
     * @param where
     * @return
     */
    public List<NotebookData> query(String where) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<NotebookData> data = null;
        data = new ArrayList<NotebookData>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.NOTE_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NotebookData notebookData = new NotebookData();
            notebookData.setId(cursor.getInt(0));
            notebookData.setIid(cursor.getInt(1));
            notebookData.setUnixTime(cursor.getString(2));
            notebookData.setDate(cursor.getString(3));
            notebookData.setContent(cursor.getString(4));
            notebookData.setColor(cursor.getInt(5));
            data.add(notebookData);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }

    /**
     * 重置
     * 
     * @param datas
     */
    public void reset(List<NotebookData> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DatabaseHelper.NOTE_TABLE_NAME);
            // 重新添加
            for (NotebookData data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }

    /**
     * 保存一条数据到本地(若已存在则直接覆盖)
     * 
     * @param data
     */
    public void save(NotebookData data) {
        List<NotebookData> datas = query(" where _id=" + data.getId());
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }

    //
    // /**
    // * 合并一条数据到本地(通过更新时间判断仅保留最新)
    // *
    // * @param data
    // * @return 数据是否被合并了
    // */
    // public boolean merge(NotebookData data) {
    // Cursor cursor = sqlite.rawQuery(
    // "select * from " + DatabaseHelper.NOTE_TABLE_NAME
    // + " where _id=" + data.getId(), null);
    // NotebookData localData = new NotebookData();
    // // 本循环其实只执行一次
    // for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
    // localData.setId(cursor.getInt(0));
    // localData.setIid(cursor.getInt(1));
    // localData.setUnixTime(cursor.getString(2));
    // localData.setDate(cursor.getString(3));
    // localData.setContent(cursor.getString(4));
    // localData.setColor(cursor.getInt(5));
    // }
    // // 是否需要合这条数据
    // boolean isMerge = localData.getUnixTime() < data.getUnixTime();
    // if (isMerge) {
    // save(data);
    // }
    // return isMerge;
    // }

    public void destroy() {
        dbHelper.close();
    }
}