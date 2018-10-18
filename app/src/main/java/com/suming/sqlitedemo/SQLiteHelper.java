package com.suming.sqlitedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/29 16:45
 * @类描述 ${TODO}数据库Helper类，必须继承自 SQLiteOpenHelper
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "SqliteDemo.db";
    public static final String TABLE_NAME = "OpenBleLog";

    public SQLiteHelper(Context context) {
        /**
         * 第一个参数： 上下文
         * 第二个参数：数据库的名称
         * 第三个参数：null代表的是默认的游标工厂
         * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
         */
        super(context, DB_NAME, null, 1);
    }

    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来.
     * 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须写在程序然后重新安装这个app
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + "(_id integer primary key autoincrement, user_id long not null, name text, address text, time text)";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 当数据库更新的时候调用的方法
     * 这个要显示出来得在上面的super语句里面版本号发生改变时才会 打印  （super(context, DB_NAME, null, 2);）
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     * 在数据库版本每次发生变化时都会把用户手机上的数据库表删除，然后再重新创建。
     * 一般在实际项目中是不能这样做的，正确的做法是在更新数据库表结构时，还要考虑用户存放于数据库中的数据不会丢失。
     *
     * @param sqLiteDatabase
     * @param oldVersion     旧版本
     * @param newVersion     新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //第一次创建不需要做任何操作
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);

       /* if (oldVersion==2 && newVersion==3) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD phone TEXT;");//新版本为2的时候，往表中增加一列,在第三版本就可以使用phone字段了
        }*/
    }
}
