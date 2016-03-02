package com.theIronYard;

import jodd.json.JsonSerializer;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS messages (id IDENTITY, author VARCHAR, text VARCHAR)");
    }
    public static void dropTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE messages");
        conn.close();
    }
    public static void insertMessage(Connection conn, String author, String text) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO messages VALUES (null, ?, ?)");
        stmt.setString(1, author);
        stmt.setString(2, text);
        stmt.execute();
    }
    public static ArrayList<Message> selectMessages(Connection conn) throws SQLException {
        ArrayList<Message> messageList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM messages");
        while (results.next()) {
            int id = results.getInt(1);
            String author = results.getString(2);
            String text = results.getString(3);
            messageList.add(new Message(id, author, text));
        }
        return messageList;
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();


        Spark.get(
                "/get-messages",
                ((request, response) -> {
                    ArrayList<Message> messageList = selectMessages(conn);

                    JsonSerializer serializer = new JsonSerializer();

                    return serializer.serialize(messageList);
                })
        );

        Spark.post(
                "/add-message",
                ((request, response) -> {
                    String author = request.queryParams("author");
                    String text = request.queryParams("text");

                    insertMessage(conn, author, text);

                    return "";
                })
        );


    }
}
