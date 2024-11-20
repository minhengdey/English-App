package com.example.apienglishapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.*;
import java.util.Objects;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.apienglishapp.repository")
public class ApiEnglishAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiEnglishAppApplication.class, args);
        transferSQLiteToMySQL();
    }

    public static void transferSQLiteToMySQL() {
        String sqliteDbPath = Objects.requireNonNull(ApiEnglishAppApplication.class.getClassLoader().getResource("db/dict_hh.db")).getPath();

        try (Connection sqliteConn = DriverManager.getConnection("jdbc:sqlite:" + sqliteDbPath)) {
            String mysqlUrl = "jdbc:mysql://localhost:3306/english-app";
            String mysqlUser = "root";
            String mysqlPassword = "minhanh2722004";

            try (Connection mysqlConn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
                Statement sqliteStmt = sqliteConn.createStatement();
                ResultSet tables = sqliteStmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");

                while (tables.next()) {
                    String tableName = tables.getString(1);
                    System.out.println("Đang chuyển bảng: " + tableName);
                    transferTableData(tableName, sqliteConn, mysqlConn);
                }

                System.out.println("Chuyển đổi hoàn tất.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void transferTableData(String tableName, Connection sqliteConn, Connection mysqlConn) throws SQLException {
        Statement sqliteStmt = sqliteConn.createStatement();
        ResultSet columns = sqliteStmt.executeQuery("PRAGMA table_info(" + tableName + ")");

        StringBuilder createTableSql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        while (columns.next()) {
            String columnName = columns.getString("name");
            String columnType = mapSQLiteTypeToMySQL(columns.getString("type"));
            createTableSql.append(columnName).append(" ").append(columnType).append(", ");
        }
        createTableSql.setLength(createTableSql.length() - 2);
        createTableSql.append(");");

        try (Statement mysqlStmt = mysqlConn.createStatement()) {
            mysqlStmt.execute(createTableSql.toString());
        }

        ResultSet rows = sqliteStmt.executeQuery("SELECT * FROM " + tableName);

        ResultSetMetaData metaData = rows.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rows.next()) {
            StringBuilder insertSql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                insertSql.append("?").append(", ");
            }
            insertSql.setLength(insertSql.length() - 2);
            insertSql.append(")");

            try (PreparedStatement mysqlStmt = mysqlConn.prepareStatement(insertSql.toString())) {
                for (int i = 1; i <= columnCount; i++) {
                    mysqlStmt.setObject(i, rows.getObject(i));
                }
                mysqlStmt.executeUpdate();
            }
        }
    }

    private static String mapSQLiteTypeToMySQL(String sqliteType) {
        sqliteType = sqliteType.toLowerCase();
        if (sqliteType.contains("int")) {
            return "INT";
        } else if (sqliteType.contains("text")) {
            return "TEXT";
        } else if (sqliteType.contains("real") || sqliteType.contains("numeric")) {
            return "FLOAT";
        } else if (sqliteType.contains("blob")) {
            return "BLOB";
        } else {
            return "TEXT";
        }
    }
}
