package Models;

import Classes.MetaMusicSong;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by MatthewRowe on 4/21/15.
 */

//http://www.rgagnon.com/javadetails/java-0117.html

public class DatabaseModel {

    private static String protocol = "jdbc:derby:";
    private static String dbName = "dbo.SavedSongs";
    private static final String USER = "username";
    private static final String PASS = "password";
    private static Connection conn;
    private static Statement statement;


//    public static void main(String[] args) {
//        LinkedList<String> a = new LinkedList<String>();
//        a.add("roarer");
//        a.add("boom");
//        MetaMusicSong m1 = new MetaMusicSong("1", "a", a, "roar1");
//        MetaMusicSong m2 = new MetaMusicSong("2", "b", a, "roar2");
//        MetaMusicSong m3 = new MetaMusicSong("3", "c", a, "roar3");
//        MetaMusicSong m4 = new MetaMusicSong("4", "d", a, "roar4");
//        connectToDatabase();
//        createTable();
//        insertMetaMusicSongObjectIntoDatabaseTableMetaMusicSong(m1);
//        insertMetaMusicSongObjectIntoDatabaseTableMetaMusicSong(m2);
//        insertMetaMusicSongObjectIntoDatabaseTableMetaMusicSong(m3);
//        insertMetaMusicSongObjectIntoDatabaseTableMetaMusicSong(m4);
//        selectAllMetaMusicSongObjectsFromDatabase();
//        disconnectFromDatabase();
//    }


    public static void initializeDatabase() {
        connectToDatabase();
        createTable();
    }

    private static void connectToDatabase() {
        try {
            conn = DriverManager.getConnection(protocol + dbName + ";create=true", USER, PASS);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);    //http://stackoverflow.com/questions/8033163/moving-resultset-to-first
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void createTable() {
        try {
            String createTableSQL = "CREATE TABLE MetaMusicSong " +
                    "(metaMusicSong BLOB)";   //Binary Large OBject
            statement.executeUpdate(createTableSQL);
        } catch (SQLException se) {
        }
    }


    public static void insertMetaMusicSongObjectLinkedListIntoDatabaseTableMetaMusicSong(LinkedList<MetaMusicSong> mmsLinkedListToInsert) {


            try {
                PreparedStatement pstmt =
                        conn.prepareStatement
                                ("INSERT INTO MetaMusicSong (metaMusicSong) VALUES(?)");
                for (MetaMusicSong mmsToInsert : mmsLinkedListToInsert) {
                    if (!checkIfMetaMusicSongObjectAlreadyExistsInDatabase(mmsToInsert)) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        oos.writeObject(mmsToInsert);
                        // serialize the employee object into a byte array
                        byte[] metaMusicSongAsBytes = baos.toByteArray();

                        ByteArrayInputStream bais =
                                new ByteArrayInputStream(metaMusicSongAsBytes);
                        // bind our byte array  to the emp column
                        pstmt.setBinaryStream(1, bais, metaMusicSongAsBytes.length);
                        pstmt.executeUpdate();
                    }
                }
                conn.commit();
                pstmt.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
    }

    public static void insertMetaMusicSongObjectIntoDatabaseTableMetaMusicSong(MetaMusicSong mmsToInsert) {
        try {
            if (!checkIfMetaMusicSongObjectAlreadyExistsInDatabase(mmsToInsert)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(mmsToInsert);
                // serialize the employee object into a byte array
                byte[] metaMusicSongAsBytes = baos.toByteArray();
                PreparedStatement pstmt =
                        conn.prepareStatement
                                ("INSERT INTO MetaMusicSong (metaMusicSong) VALUES(?)");
                ByteArrayInputStream bais =
                        new ByteArrayInputStream(metaMusicSongAsBytes);
                // bind our byte array  to the emp column
                pstmt.setBinaryStream(1,bais, metaMusicSongAsBytes.length);
                pstmt.executeUpdate();
                conn.commit();
                pstmt.close();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static LinkedList<MetaMusicSong> selectAllMetaMusicSongObjectsFromDatabase() {
        LinkedList<MetaMusicSong> mmsLinkedList = new LinkedList<MetaMusicSong>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs =
                    stmt.executeQuery("SELECT metaMusicSong FROM MetaMusicSong");
            // loop through the result set
            while (rs.next()) {
                // fetch the serialized object to a byte array
                Blob aBlob = rs.getBlob(1);
                byte[] st = aBlob.getBytes(1, (int) aBlob.length());
                //byte[] st = (byte[])rs.getObject(1);
                //   or  byte[] st = rs.getBytes(1);
                //   or  Blob aBlob = rs.getBlob(1);
                //       byte[] st = aBlob.getBytes(0, (int) aBlob.length());
                ByteArrayInputStream baip =
                        new ByteArrayInputStream(st);
                ObjectInputStream ois =
                        new ObjectInputStream(baip);
                // re-create the object
                MetaMusicSong mmp = (MetaMusicSong)ois.readObject();
                // display the result for demonstration
                mmsLinkedList.add(mmp);
            }
            stmt.close();
            rs.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return mmsLinkedList;
    }

    private static boolean checkIfMetaMusicSongObjectAlreadyExistsInDatabase(MetaMusicSong mmsToCheck) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs =
                    stmt.executeQuery("SELECT metaMusicSong FROM MetaMusicSong");
            // loop through the result set
            while (rs.next()) {
                // fetch the serialized object to a byte array
                Blob aBlob = rs.getBlob(1);
                byte[] st = aBlob.getBytes(1, (int) aBlob.length());
                //byte[] st = (byte[])rs.getObject(1);
                //   or  byte[] st = rs.getBytes(1);
                //   or  Blob aBlob = rs.getBlob(1);
                //       byte[] st = aBlob.getBytes(0, (int) aBlob.length());
                ByteArrayInputStream baip =
                        new ByteArrayInputStream(st);
                ObjectInputStream ois =
                        new ObjectInputStream(baip);
                // re-create the object
                MetaMusicSong mmp = (MetaMusicSong)ois.readObject();
                // display the result for demonstration
                if (mmp.toString().equals(mmsToCheck.toString())) {
                    return true;
                }
            }
            stmt.close();
            rs.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void disconnectFromDatabase() {
        try {
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}



