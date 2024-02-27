package com.example.lab8.Testeee;

import com.example.lab8.service.criptare.Criptare;

public class TesteCriptare {

    public void test_criptare()
    {
        Criptare criptare = new Criptare("1234");
        System.out.println(criptare.criptare());
    }

}
//