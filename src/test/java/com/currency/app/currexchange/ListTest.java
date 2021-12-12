package com.currency.app.currexchange;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTest {
    @Test
    public void testList(){
        String[] strList = new String[] {"1", "2", "3"};
        List<String> list = new ArrayList<String>(Arrays.asList(strList));
        System.out.println(list.toString());
        list.remove(0);
        System.out.println(list.toString());
    }
}
