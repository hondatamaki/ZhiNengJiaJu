package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2021/12/22.
 */

public class UserHelper extends SQLiteOpenHelper {
    public UserHelper(Context context) {
        super(context,"db_user3",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table userTable(user text,password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
