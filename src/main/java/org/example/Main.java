package org.example;

import java.sql.SQLException;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever();
        try{
            dbConnection.getDBConnection();
            Team t = dataRetriever.findTeamById(1);
            System.out.println(t.getPlayersGoals());
            System.out.println(t.toString());

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}