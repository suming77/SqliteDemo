package com.suming.sqlitedemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 mingyan.su
 * @创建时间 2018/9/29 16:45
 * @类描述 ${TODO}调用
 *
 * 这里主要是基本的增删改查（提供了多种不同的写法）
 * 增删改查的数据是写死的，先增加数据，再点击下面的按钮
 * 具体实现还需要根据实际需求灵活改动
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BleListAdapter mAdapter;
    private List<OpenBleInfo> mList;
    private SQliteDao mDao;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.insertButton).setOnClickListener(this);
        findViewById(R.id.insertButton2).setOnClickListener(this);
        findViewById(R.id.deleteButton).setOnClickListener(this);
        findViewById(R.id.deleteButton2).setOnClickListener(this);
        findViewById(R.id.updateButton).setOnClickListener(this);
        findViewById(R.id.updateButton2).setOnClickListener(this);
        findViewById(R.id.query1Button).setOnClickListener(this);
        findViewById(R.id.deleteAllButton).setOnClickListener(this);
        findViewById(R.id.deleteAllButton2).setOnClickListener(this);
        findViewById(R.id.getAllDataButton).setOnClickListener(this);
        findViewById(R.id.getAllDataButton2).setOnClickListener(this);

        mList = new ArrayList<>();
        mDao = new SQliteDao(this);
        mListView = (ListView) findViewById(R.id.listView);

        if (mDao.getAllData() != null && !mDao.getAllData().isEmpty()) {
            mList.addAll(mDao.getAllData());
        }

        mAdapter = new BleListAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    private void refreshList() {
        // 注意：千万不要直接赋值，如：mList = ordersDao.getAllDate() 此时相当于重新分配了一个内存 原先的内存没改变 所以界面不会有变化
        // Java中的类是地址传递 基本数据才是值传递
        mList.clear();
        if (mDao.getAllData() != null && !mDao.getAllData().isEmpty()) {
            mList.addAll(mDao.getAllData());
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        String msg = "";
        switch (view.getId()) {
            case R.id.insertButton://增加数据
                OpenBleInfo userAddress = add(10083, "张三", "广东省深圳市", "2018-10-15");
                boolean add = mDao.add(userAddress);
                Toast.makeText(this, add ? "增加成功" : "增加失败", Toast.LENGTH_SHORT).show();

                refreshList();
                break;
            case R.id.insertButton2:
                OpenBleInfo userAddress2 = add(10084, "李四", "湖北省武汉市", "2018-8-1");
                long add2 = mDao.addForContentValues(userAddress2);
                Toast.makeText(this, add2 != -1 ? "增加成功" : "增加失败", Toast.LENGTH_SHORT).show();

                refreshList();
                break;

            case R.id.deleteButton://删除数据
                boolean delete = mDao.delete("张三");
                Toast.makeText(this, delete ? "删除成功" : "删除失败", Toast.LENGTH_SHORT).show();

                refreshList();
                break;
            case R.id.deleteButton2:
                int deleteForContentValues = mDao.deleteForContentValues("李四");
                if (deleteForContentValues == -1) {
                    msg = "删除失败";
                } else if (deleteForContentValues == 0) {
                    msg = "删除失败：没有这条数据";
                } else {
                    msg = "删除成功";
                }

                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                refreshList();
                break;

            case R.id.updateButton://更新数据
                boolean update = mDao.update("张三", "湖南省长沙市");
                Toast.makeText(this, update ? "更新成功" : "更新失败", Toast.LENGTH_SHORT).show();

                refreshList();
                break;
            case R.id.updateButton2:
                int updateForContentValues = mDao.updateForContentValues("李四", "江西省南昌市");
                if (updateForContentValues == -1) {
                    msg = "更新失败";
                } else if (updateForContentValues == 0) {
                    msg = "更新失败：没有这条数据";
                } else {
                    msg = "更新成功";
                }

                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                refreshList();
                break;

            case R.id.query1Button://查询数据
                List<OpenBleInfo> check = mDao.check(SQliteDao.COLUMNS_NAME, "张三");
                if (check != null && !check.isEmpty()) {
                    mList.clear();
                    mList.addAll(check);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "查询失败：没有这条数据", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.deleteAllButton://删除所有数据
                boolean deleteAll = mDao.deleteAll();

                Toast.makeText(this, deleteAll ? "删除所有数据成功" : "删除所有数据失败", Toast.LENGTH_SHORT).show();

                refreshList();
                break;

            case R.id.deleteAllButton2:
                int deleteAll2 = mDao.deleteAll2();

                if (deleteAll2 == -1) {
                    msg = "删除所有数据失败";
                } else if (deleteAll2 == 0) {
                    msg = "删除所有失败：没有数据";
                } else {
                    msg = "删除所有数据成功";
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                refreshList();
                break;

            case R.id.getAllDataButton://获取所有数据
                mList.clear();
                List<OpenBleInfo> allDataForTry = mDao.getAllData();
                if (allDataForTry != null && !allDataForTry.isEmpty()) {
                    mList.addAll(allDataForTry);
                }

                mAdapter.notifyDataSetChanged();
                if (mList != null && !mList.isEmpty()) {
                    Toast.makeText(this, "获取所有数据成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "获取所有数据成功：暂无数据", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.getAllDataButton2:
                mList.clear();
                List<OpenBleInfo> allDataForTry2 = mDao.getAllDataForTry();
                if (allDataForTry2 != null && !allDataForTry2.isEmpty()) {
                    mList.addAll(allDataForTry2);
                }

                mAdapter.notifyDataSetChanged();
                if (mList != null && !mList.isEmpty()) {
                    Toast.makeText(this, "获取所有数据成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "获取所有数据成功：暂无数据", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @NonNull
    private OpenBleInfo add(long user_id, String name, String address, String time) {
        OpenBleInfo userAddress = new OpenBleInfo();
        userAddress.setUser_id(user_id);
        userAddress.setName(name);
        userAddress.setAddress(address);
        userAddress.setTime(time);
        return userAddress;
    }
}
