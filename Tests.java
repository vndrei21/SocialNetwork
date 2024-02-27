package com.example.lab8;

import com.example.lab8.domain.RequestStatus;

public class Tests {

    public void test()
    {
        String status = "PENDING";
        String status1 ="ACCEPTED";
        String status2 ="REJECTED";
        System.out.println(RequestStatus.valueOf(status));
        System.out.println(RequestStatus.valueOf(status1));
        System.out.println(RequestStatus.valueOf(status2));



    }

}
