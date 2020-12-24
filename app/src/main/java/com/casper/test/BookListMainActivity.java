package com.casper.test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.casper.test.data.BookFragmentAdapter;
import com.casper.test.data.BookSaver;
import com.casper.test.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_ADDNEW = CONTEXT_MENU_DELETE+1;
    public static final int CONTEXT_MENU_UPDATE = CONTEXT_MENU_ADDNEW+1;
    public static final int CONTEXT_MENU_ABOUT = CONTEXT_MENU_UPDATE+1;
    public static final int REQUEST_CODE_EDIT_BOOK = 901;
    public static final int REQUEST_CODE_UPDATE_BOOK = 902;


    BookAdapter bookAdapter;
    BookSaver bookSaver;
    private List<Book> listBooks=new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookSaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        bookSaver=new BookSaver(this);
        listBooks=bookSaver.load();
        if(listBooks.size()==0)
            init();

        bookAdapter = new BookAdapter(BookListMainActivity.this, R.layout.list_view_item_book, getListBooks());

        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new BookListFragment(bookAdapter));
        datas.add(new WebViewFragment());
        datas.add(new MapViewFragment());
        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");
        myPageAdapter.setTitles(titles);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==findViewById(R.id.list_view_books)) {
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(listBooks.get(info.position).getTitle());
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0, CONTEXT_MENU_ADDNEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_UPDATE, 0, "修改");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_EDIT_BOOK:
                if(resultCode==RESULT_OK) {
                    String title = data.getStringExtra("title");
                    int insertPosition = data.getIntExtra("insert_position", 0);
                    getListBooks().add(insertPosition, new Book(title, R.drawable.book_no_name));
                    bookAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_UPDATE_BOOK:
                if(resultCode == RESULT_OK){
                    int insertPosition = data.getIntExtra("insert_position",0);
                    Book bookAtPosition = getListBooks().get(insertPosition);

                    bookAtPosition.setTitle(data.getStringExtra("title"));
                    bookAdapter.notifyDataSetChanged();
                }
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE:
                final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                /*listBooks.remove(info.position);
                bookAdapter.notifyDataSetChanged();
                Toast.makeText(BookListMainActivity.this,"删除成功", Toast.LENGTH_LONG).show();
                 */
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("询问");
                builder.setMessage("你确定要删除这个图书吗？");
                builder.setCancelable(true);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listBooks.remove(info.position);
                        bookAdapter.notifyDataSetChanged();

                        Toast.makeText(BookListMainActivity.this,"删除成功", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
            case CONTEXT_MENU_ADDNEW :
                Intent intent = new Intent(this,EditBookActivity.class);
                intent.putExtra("title","无名书籍");
                intent.putExtra("insert_position",((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position);
                startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK);
                break;
            case CONTEXT_MENU_UPDATE:
                int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                Intent intent2 = new Intent(this,EditBookActivity.class);
                intent2.putExtra("title",listBooks.get(position).getTitle());
                intent2.putExtra("insert_position",position);
                startActivityForResult(intent2, REQUEST_CODE_UPDATE_BOOK);
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(BookListMainActivity.this,"图书列表v1.0,coded by casper", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }


    private void init() {
        getListBooks().add(new Book("软件项目管理案例教程（第4版）",R.drawable.book_2));
        getListBooks().add(new Book("创新工程实践",R.drawable.book_no_name));
        getListBooks().add(new Book("信息安全数学基础（第2版）",R.drawable.book_1));
    }

    public List<Book> getListBooks() {
        return listBooks;
    }

    public void setListBooks(List<Book> listBooks) {
        this.listBooks = listBooks;
    }

    public class BookAdapter extends ArrayAdapter<Book> {

        private int resourceId;

        public BookAdapter(Context context, int resource, List<Book> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Book book = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_book_cover)).setImageResource(book.getCoverResourceId());
            ((TextView) view.findViewById(R.id.text_view_book_title)).setText(book.getTitle());
            return view;
        }
    }
}

