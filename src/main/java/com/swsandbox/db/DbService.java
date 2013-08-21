package com.swsandbox.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jgreenwald
 * Date: 8/20/13
 * Time: 10:27 PM
 */
public class DbService
{
    public static void main(String[] args)
    {
        System.out.println(execute("select * from transaction_type"));
    }

    public static List<Map<String, Object>> execute(String query)
    {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection("jdbc:oracle:thin:@auspepdevdb02.aus.biowareonline.int:1521:bwapepd2", "webtest", "webtest");
            stmt = conn.createStatement();
            stmt.setFetchSize(50);
            stmt.setMaxRows(50);
            rs = stmt.executeQuery(query);

            List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();


            while (rs.next())
            {
                Map<String, Object> row = new HashMap<String, Object>();
                int columnCount = rs.getMetaData().getColumnCount();

                for (int i = 1; i < columnCount + 1; i++)
                {
                    Object o = rs.getObject(i);
                    row.put(rs.getMetaData().getColumnName(i), o);
                }
                results.add(row);
            }
            return results;
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex)
        {
            List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("ERROR", ex.getMessage());
            results.add(row);
            return results;
        }
        finally
        {
            try
            {
                rs.close();
            }
            catch (Exception ex)
            {
            }
            try
            {
                stmt.close();
            }
            catch (Exception ex)
            {
            }
            try
            {
                conn.close();
            }
            catch (Exception ex)
            {
            }
        }
    }
}
