package com.casper.test.data;

import android.content.Context;

import com.casper.test.data.model.Book;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BookSaver {
    public BookSaver(Context context) {
        this.context = context;
    }

    Context context;

    public ArrayList<Book> getBooks() {
        return books;
    }

    ArrayList<Book> books = new ArrayList<Book>();

    public void save() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt", Context.MODE_PRIVATE)
            );
            outputStream.writeObject(books);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Book> load(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt"));
            books = (ArrayList<Book>)inputStream.readObject();
            inputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return books;
    }

}
