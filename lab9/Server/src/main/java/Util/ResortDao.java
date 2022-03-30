package Util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.transform.Result;

import entity.LiftRide;
import entity.Year;


public class ResortDao {
  private static BasicDataSource dataSource;
  private ArrayList<Integer> resortsList;

  public ResortDao() {
    dataSource = DBCPDataSource.getDataSource();
    resortsList = new ArrayList<Integer>();
  }

  public int getNumberOfSkiersAtResorts(int resortId, int seasonId, int dayId){
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String selectQueryStatement = "SELECT COUNT(DISTINCT SkierId) FROM LiftRides WHERE ResortId = ? AND" +
            " SeasonId = ? AND DayId = ?";
    int skierNumber = 0;
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);
      preparedStatement.setInt(1, resortId);
      preparedStatement.setInt(2, seasonId);
      preparedStatement.setInt(3, dayId);
      System.out.println(resortId);
      System.out.println(seasonId);
      System.out.println(dayId);
      ResultSet res =  preparedStatement.executeQuery();
      while(res.next()){
        skierNumber = res.getInt(1);
      }
      System.out.println(skierNumber);

      return skierNumber;

    }catch (SQLException e) {
        e.printStackTrace();
      } finally {
        try {
          if (conn != null) {
            conn.close();
          }
          if (preparedStatement != null) {
            preparedStatement.close();
          }
        } catch (SQLException se) {
          se.printStackTrace();
        }
      }
      return 0;
    }


  public ArrayList<Integer> getAllResorts() {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
//    String insertQueryStatement = "INSERT INTO Resort (ResortId, Year) " +
//            "VALUES (?,?)";
    String selectQueryStatement = "SELECT ResortId FROM Resort";

    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);
//      preparedStatement.setInt(1, newYear.getResortId());
//      preparedStatement.setInt(2, newYear.getYear());
      // execute insert SQL statement
      ResultSet res =  preparedStatement.executeQuery();
      //get all resortId then return them back to Servelet to display
      while(res.next()){
        resortsList.add(res.getInt("ResortId"));
      }
      return resortsList;
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    return null;
  }
}