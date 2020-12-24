package com.casper.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BookListFragment extends Fragment {

    private BookListMainActivity.BookAdapter bookAdapter;
    public BookListFragment(BookListMainActivity.BookAdapter bookAdapter) {
        // Required empty public constructor
        this.bookAdapter=bookAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView listViewBooks=view.findViewById(R.id.list_view_books);
        listViewBooks.setAdapter(bookAdapter);
        this.registerForContextMenu(listViewBooks);
        // Inflate the layout for this fragment
        return view;
    }
}