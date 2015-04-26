package Models;

import Classes.MetaMusicSong;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.LinkedList;

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
    private static final String userTable = "userMetaMusicSong";
    private static final String globalTable = "globalMetaMusicSong";

    public static void initializeDatabase() {
        connectToDatabase();
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

    public static void createTableAuthorized() {
        dropTable(userTable);
        createTable(userTable);
        createTable(globalTable);
    }

    public static void createTableGlobal() {
        createTable(globalTable);
    }

                    private static void createTable(String tableName) {
                        try {
                            String createTableSQL = "CREATE TABLE " + tableName + " " +
                                    "(metaMusicSong BLOB)";   //Binary Large OBject
                            statement.executeUpdate(createTableSQL);
                        } catch (SQLException se2) {
                            System.out.println("Couldn't create table");
                        }
                    }


                    private static void dropTable(String tableName) {
                        try {
                            String dropTableSQL = "DROP TABLE " + tableName;
                            statement.executeUpdate(dropTableSQL);
                        } catch (SQLException se1) {
                            System.out.println("Couldn't drop table " + tableName + " (before creating it)");
                        }
                    }



    public static void receiveMetaMusicSongObjectLinkedListIntoDatabase(LinkedList<MetaMusicSong> mmsLinkedListToInsert) {
            insertMetaMusicSongObjectLinkedListIntoDatabaseTableMetaMusicSong(mmsLinkedListToInsert, userTable);
            insertMetaMusicSongObjectLinkedListIntoDatabaseTableMetaMusicSong(mmsLinkedListToInsert, globalTable);

    }


                    private static void insertMetaMusicSongObjectLinkedListIntoDatabaseTableMetaMusicSong(LinkedList<MetaMusicSong> mmsLinkedListToInsert, String tableName) {
                            try {
                                PreparedStatement pstmt =
                                        conn.prepareStatement
                                                ("INSERT INTO " + tableName + "(metaMusicSong) VALUES(?)");
                                for (MetaMusicSong mmsToInsert : mmsLinkedListToInsert) {
                                    if (!checkIfMetaMusicSongObjectAlreadyExistsInDatabase(mmsToInsert, tableName)) {
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

                    private static boolean checkIfMetaMusicSongObjectAlreadyExistsInDatabase(MetaMusicSong mmsToCheck, String tableName) {
                        try {
                            Statement stmt = conn.createStatement();
                            ResultSet rs =
                                    stmt.executeQuery("SELECT metaMusicSong FROM " + tableName);
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

    public static LinkedList<MetaMusicSong> selectAllMetaMusicSongObjectsFromDatabase(boolean isUser) {
        if (isUser) {
            return selectAllMetaMusicSongObjectsFromDatabase(userTable);
        } else {
            return selectAllMetaMusicSongObjectsFromDatabase(globalTable);
        }

    }

                    private static LinkedList<MetaMusicSong> selectAllMetaMusicSongObjectsFromDatabase(String tableName) {
                        LinkedList<MetaMusicSong> mmsLinkedList = new LinkedList<MetaMusicSong>();
                        try {
                            Statement stmt = conn.createStatement();
                            ResultSet rs =
                                    stmt.executeQuery("SELECT metaMusicSong FROM " + tableName);
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



    public static void disconnectFromDatabase() {
        try {
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}



