package com.suming.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/29 17:21
 * @类描述 ${TODO}数据库操作类（dao类）
 * 1、定义一个构造方法，利用这个方法去实例化一个 数据库帮助类
 * 2、编写dao类的对应的 增删改查 方法。
 *
 * Cursor是结果集游标，用于对结果集进行随机访问
 * 当前行移动到下一行，如果已经移过了结果集的最后一行，返回结果为false，否则为true
 *
 * 对于像银行转账之类的操作，我们需要使用事务来保证操作的安全性
 */
public class SQliteDao {

    private Context context;
    private SQLiteHelper mSqLiteHelper;

    public static final String COLUMNS_ID = "_id";//主键id
    public static final String COLUMNS_USERID = "user_id";
    public static final String COLUMNS_NAME = "name";
    public static final String COLUMNS_ADDRESS = "address";
    public static final String COLUMNS_TIME = "time";
    private String[] COLUMNS = new String[]{COLUMNS_ID,COLUMNS_USERID, COLUMNS_NAME, COLUMNS_ADDRESS, COLUMNS_TIME};

    /**
     * dao类需要实例化数据库Help类,只有得到帮助类的对象我们才可以实例化 SQLiteDatabase
     *
     * @param context
     */
    public SQliteDao(Context context) {
        this.context = context;
        mSqLiteHelper = new SQLiteHelper(context);
    }

    // 将数据库打开帮帮助类实例化，然后利用这个对象
    // 调用谷歌的api去进行增删改查
    // getWritableDatabase()和getReadableDatabase()方法都可以获取一个用于操作数据库的SQLiteDatabase实例。
    // 但getWritableDatabase() 方法以读写方式打开数据库，一旦数据库的磁盘空间满了，数据库就只能读而不能写，
    // 倘若使用getWritableDatabase()打开数据库就会出错。getReadableDatabase()方法先以读写方式打开数据库
    // ，如果数据库的磁盘空间满了，就会打开失败，当打开失败后会继续尝试以只读方式打开数据库。

    /**
     * 判断数据库是否有数据
     *
     * @return 是否存在数据
     */
    public boolean isDataExit() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        int count = 0;

        try {
            database = mSqLiteHelper.getReadableDatabase();
            cursor = database.query(SQLiteHelper.TABLE_NAME, new String[]{"COUNT(_id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {//用于将游标移动到结果集的第一行
                count = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始
            }

            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }

        return false;
    }



    /**
     * 增加一条数据
     *
     * @param userAddress
     * @return 是否增加成功
     * String sql = "insert into OpenBleLog (user_id, name, address, time) values (10083, '张三', '广东省深圳市', '2018-10-15')"
     */
    public boolean add(OpenBleInfo userAddress) {
        SQLiteDatabase db = null;
        try {
            db = mSqLiteHelper.getWritableDatabase();
            String sql = "insert into " + SQLiteHelper.TABLE_NAME + " (" + COLUMNS_USERID + ", " + COLUMNS_NAME + ", " + COLUMNS_ADDRESS + ", " + COLUMNS_TIME + ") values (" + userAddress.getUser_id() + ", '" + userAddress.getName() + "', '" + userAddress.getAddress() + "', '" + userAddress.getTime() + "')";
            System.out.println(sql);
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 增加的方法，返回的的是一个long值
     * 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
     * getWritableDatabase(); 执行后数据库文件才会生成
     * 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看
     *
     * @param userAddress
     * @return 返回值 显示数据添加在第几行
     *
     * 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
     * 因为自增长,-1为增加失败
     */
    public long addForContentValues(OpenBleInfo userAddress) {
        SQLiteDatabase writableDatabase = mSqLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMNS_USERID, userAddress.getUser_id());
        contentValues.put(COLUMNS_NAME, userAddress.getName());
        contentValues.put(COLUMNS_ADDRESS, userAddress.getAddress());
        contentValues.put(COLUMNS_TIME, userAddress.getTime());

        long insert = writableDatabase.insert(SQLiteHelper.TABLE_NAME, null, contentValues);

        return insert;
    }

    /**
     * 获取数据库中的第一条数据
     *
     * @return 第一条数据
     */
    public OpenBleInfo getFirstData() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mSqLiteHelper.getWritableDatabase();
            cursor = db.query(SQLiteHelper.TABLE_NAME, COLUMNS, null, null, null, null, null, "1");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    OpenBleInfo userAddress = parseDate(cursor);
                    return userAddress;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 删除某一条数据
     *
     * @param name 根据名字删除
     * @return 是否删除成功
     * String sql = "delete from OpenBleLog where name = ?"
     */
    public boolean delete(String name) {
        SQLiteDatabase db = null;
        try {
            db = mSqLiteHelper.getWritableDatabase();
            db.beginTransaction();//开启一个事务
            db.execSQL("delete from " + SQLiteHelper.TABLE_NAME + " where " + COLUMNS_NAME + " = ?", new String[]{name});
            System.out.println("delete from " + SQLiteHelper.TABLE_NAME + " where " + COLUMNS_NAME + " = ?");
            db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
                db.close();
            }
        }
        return false;
    }

    /**
     * 删除的方法，返回值是int
     *
     * @param name
     * @return int
     */
    public int deleteForContentValues(String name) {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();
        int delete = db.delete(SQLiteHelper.TABLE_NAME, COLUMNS_NAME + " = ?", new String[]{name});
        db.close();
        return delete;
    }


    /**
     * 根据名字更改地址
     *
     * @param name
     * @param address
     * @return 更新数据是否成功
     */
    public boolean update(String name, String address) {
        SQLiteDatabase db = null;
        try {
            db = mSqLiteHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMNS_ADDRESS, address);
            db.update(SQLiteHelper.TABLE_NAME, contentValues, COLUMNS_NAME + " = ?", new String[]{name});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 根据名字更新地址
     *
     * @param name
     * @param address
     * @return  -1:更新失败；0：没有这条数据；
     */
    public int updateForContentValues(String name, String address) {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMNS_ADDRESS, address);
        int update = db.update(SQLiteHelper.TABLE_NAME, contentValues, COLUMNS_NAME + " = ?", new String[]{name});
        db.close();
        return update;
    }

    /**
     * 根据字段查询需要的数据
     *
     * @param key  数据库列字段
     * @param values  字段对应实际数据
     * @return 查询到的数据
     */
    public List<OpenBleInfo> check(String key, String values) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = mSqLiteHelper.getWritableDatabase();
            cursor = db.query(SQLiteHelper.TABLE_NAME, COLUMNS, key + " = ?", new String[]{values}, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                List<OpenBleInfo> addressList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    OpenBleInfo userAddress = parseDate(cursor);
                    addressList.add(userAddress);
                }
                return addressList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 获取所有数据
     *
     * @return 所有数据
     * String sql = "select * from OpenBleLog"
     */
    public List<OpenBleInfo> getAllData() {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + SQLiteHelper.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<OpenBleInfo> addressList = new ArrayList<>();
            while (cursor.moveToNext()) {
                OpenBleInfo userAddress = parseDate(cursor);
                addressList.add(userAddress);
            }
            return addressList;
        }

        cursor.close();
        db.close();
        return null;
    }

    /**
     * 获取所有数据
     *
     * @return 所有数据
     */
    public List<OpenBleInfo> getAllDataForTry() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = mSqLiteHelper.getWritableDatabase();
            cursor = db.query(SQLiteHelper.TABLE_NAME, COLUMNS, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                List<OpenBleInfo> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    OpenBleInfo userAddress = parseDate(cursor);
                    list.add(userAddress);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 删除表中所有数据
     *
     * @return 删除是否成功
     * String sql = "delete from OpenBleLog"
     */
    public boolean deleteAll() {
        SQLiteDatabase db = null;
        try {
            db = mSqLiteHelper.getWritableDatabase();
            db.beginTransaction();
            String sql = "delete from " + SQLiteHelper.TABLE_NAME;
            System.out.println(sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 删除所有数据
     *
     * @return -1:删除失败；0：没有数据；
     */
    public int deleteAll2() {
        SQLiteDatabase db = mSqLiteHelper.getWritableDatabase();
        int delete = db.delete(SQLiteHelper.TABLE_NAME, null, null);
        db.close();
        return delete;
    }

    /**
     * 解析数据
     *
     * @param cursor
     */
    private OpenBleInfo parseDate(Cursor cursor) {
        OpenBleInfo userAddress = new OpenBleInfo();
        userAddress.setUser_id(cursor.getLong(cursor.getColumnIndex(COLUMNS_USERID))); //获取具体列的值,第一列的索引从0开始
        userAddress.setName(cursor.getString(cursor.getColumnIndex(COLUMNS_NAME)));
        userAddress.setAddress(cursor.getString(cursor.getColumnIndex(COLUMNS_ADDRESS)));
        userAddress.setTime(cursor.getString(cursor.getColumnIndex(COLUMNS_TIME)));
        return userAddress;
    }
}
