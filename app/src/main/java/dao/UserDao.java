package dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2021/12/22.
 */

public class UserDao {
    UserHelper helper;

    public UserDao(Context context)  {
      helper=new UserHelper(context);
    }
    public void insert(User user){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into userTable(user,password) values('"+user.getUser()+"','"+user.getPassword()+"')");
        db.close();
    }
    public List<User> find(User user){
        SQLiteDatabase db=helper.getReadableDatabase();
        List<User>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from userTable where user='"+user.getUser()+"'",null);
        while (cursor.moveToNext()){
            String name=cursor.getString(0);
            String password=cursor.getString(1);
            User user1=new User(name,password);
            list.add(user1);
        }
        cursor.close();
        db.close();
        return list;
    }
    public List<User> query(User user){
        SQLiteDatabase db=helper.getReadableDatabase();
        List<User>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from userTable where user='"+user.getUser()+"' and password='"+user.getPassword()+"'",null);
        while (cursor.moveToNext()){
            String name=cursor.getString(0);
            String password=cursor.getString(1);
            User user1=new User(name,password);
            list.add(user1);
        }
        cursor.close();
        db.close();
        return list;
    }
}
