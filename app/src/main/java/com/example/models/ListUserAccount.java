package com.example.models;

import java.util.ArrayList;

public class ListUserAccount {
    public static ArrayList<UserAccount> getUserAccounts()
    {
        ArrayList<UserAccount>database = new ArrayList<>();

        database.add(new UserAccount("admin","123","admin","Ngọc Khuyến",true));
        database.add(new UserAccount("user1","1234","employee","Minh Phúc",true));
        database.add(new UserAccount("user2","1235","employee","Mỹ Phương",true));

        return database;
    }
    public static UserAccount login(String username, String password)
    {
        //step1:query database
        ArrayList<UserAccount>database = getUserAccounts();
        //step2:compare to login
        for(UserAccount user : database)
        {
            if(user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password))
            {//login success
                return user;
            }
        }
        return null;//failed
    }
}
