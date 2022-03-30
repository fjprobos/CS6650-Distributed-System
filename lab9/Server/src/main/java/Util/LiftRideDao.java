package Util;

import java.sql.*;
import org.apache.commons.dbcp2.*;

import entity.LiftRide;


public class LiftRideDao {
  private static BasicDataSource dataSource;

  public LiftRideDao() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public int getSkiDayVerticalForSkier(int resortId, int seasonId, int dayId, int skierId){
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String selectQueryStatement = "SELECT COUNT(*) FROM LiftRides WHERE ResortId = ? AND" +
            " SeasonId = ? AND DayId = ? AND SkierId = ?";
    int vertical = 0;
    try{
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);
      preparedStatement.setInt(1, resortId);
      preparedStatement.setInt(2, seasonId);
      preparedStatement.setInt(3, dayId);
      preparedStatement.setInt(4, skierId);

      ResultSet res =  preparedStatement.executeQuery();
      while(res.next()){
        vertical += res.getInt("COUNT(*)");
      }
//      while(res.next()){
//        System.out.println(res.getInt());
//        vertical = res.getInt(1);
////        vertical = res.getInt(1);
//      }
      return vertical;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return -1;
  }

  public int getTotalVertical(int skierId, int resortId, int seasonId) {
    int totalVertical = 0;
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String selectQueryStatement;
    if(seasonId != -1){
      selectQueryStatement = "SELECT LiftId FROM LiftRides WHERE ResortId = ? AND" +
              " SkierId = ? AND SeasonId = ?";

    }else{
      selectQueryStatement = "SELECT LiftId FROM LiftRides WHERE ResortId = ? AND" +
              " SkierId = ?";
    }

    try{
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(selectQueryStatement);
      preparedStatement.setInt(1, resortId);
      preparedStatement.setInt(2, skierId);
      if(seasonId != -1){
        preparedStatement.setInt(3, seasonId);
      }
      System.out.println(selectQueryStatement);
      ResultSet res =  preparedStatement.executeQuery();
      while(res.next()){
        totalVertical += res.getInt("LiftId") * 10;
      }
      return totalVertical;

    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }


      return -1;

  }


  public void createLiftRide(LiftRide newLiftRide) {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO LiftRides (SkierId, ResortId, SeasonId, DayId, Time, LiftId) " +
            "VALUES (?,?,?,?,?,?)";

    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newLiftRide.getSkierId());
      preparedStatement.setInt(2, newLiftRide.getResortId());
      preparedStatement.setInt(3, newLiftRide.getSeasonId());
      preparedStatement.setInt(4, newLiftRide.getDayId());
      preparedStatement.setInt(5, newLiftRide.getTime());
      preparedStatement.setInt(6, newLiftRide.getLiftId());

      // execute insert SQL statement
      preparedStatement.executeUpdate();
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
  }
}