package org.example.Controller;

import org.example.EntityErShouChe.Bean_detail_diyi;

import java.util.List;

public class Controller_moreTread implements Runnable {

    private List<Bean_detail_diyi> list;

    public Controller_moreTread(List<Bean_detail_diyi> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
