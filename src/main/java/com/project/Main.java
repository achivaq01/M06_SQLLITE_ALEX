package com.project;

import com.project.Utils.UtilsSQLite;
import com.project.Interfaces.Print;
import com.project.Interfaces.PrintHeader;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /**
     * 
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "database.db";
        ResultSet rs = null;

        File fDatabase = new File(filePath);
        if (!fDatabase.exists()) {
            initDatabase(filePath);
        }

        Connection conn = UtilsSQLite.connect(filePath);
        initTables(filePath);
        insertData(filePath);

        boolean running = true;
        Scanner input = new Scanner(System.in);

        while (running) {
            int option;
            Print action;
            PrintHeader actionHeader;

            System.out.println("=== FOR HONOR ===\n"
                    + "1) Show Tables\n"
                    + "2) Show Faction Characters\n"
                    + "3) Show Faction Highest Damage\n"
                    + "4) Show Faction Highest Defense\n"
                    + "0 to exit.\n");

            option = getInput(input, 0, 4);

            switch (option) {
                case -1:
                    continue;
                case 0:
                    running = false;
                    break;
                case 1:
                    //showTable(input, conn);
                    action = Main::printOption;
                    actionHeader = Main::printHeader;
                    showOption(input, conn, UtilsSQLite.listTables(conn), action, "SELECT * FROM REPLACE;", actionHeader);
                    break;
                case 2:
                    //showFactionCharacters(input, conn);
                    action = Main::printOption;
                    actionHeader = Main::printHeader;
                    showOption(input, conn, listFactions(conn), action,
                            "SELECT * FROM Character "
                            + "WHERE idFaction = (SELECT id FROM Faction WHERE name = \"REPLACE\") ;", actionHeader);
                    break;
                case 3:
                    //showFactionHighestAttack(input, conn);
                    action = Main::printOption;
                    actionHeader = Main::printHeader;
                    showOption(input, conn, listFactions(conn), action,
                            "SELECT * FROM Character "
                            + "WHERE idFaction = "
                            + "(SELECT id FROM Faction WHERE name = \"REPLACE\") "
                            + "AND dmg = (SELECT max(dmg) FROM Character WHERE idFaction = "
                            + "(SELECT id FROM Faction WHERE name = \"REPLACE\"));", actionHeader);
                    break;
                case 4:
                    action = Main::printOption;
                    actionHeader = Main::printHeader;
                    showOption(input, conn, listFactions(conn), action,
                            "SELECT * FROM Character "
                            + "WHERE idFaction = "
                            + "(SELECT id FROM Faction WHERE name = \"REPLACE\") "
                            + "AND defense = "
                            + "(SELECT max(defense) FROM Character WHERE idFaction = "
                            + "(SELECT id FROM Faction WHERE name = \"REPLACE\"));", actionHeader);
                    break;
            }
        }

        UtilsSQLite.disconnect(conn);

    }
    /**
     * 
     * @param filePath 
     */
    static void initDatabase(String filePath) {
        Connection conn = UtilsSQLite.connect(filePath);

        // Desconnectar
        UtilsSQLite.disconnect(conn);
    }
    /**
     * 
     * @param filePath 
     */
    private static void initTables(String filePath) {
        Connection conn = UtilsSQLite.connect(filePath);
        UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Faction;");
        UtilsSQLite.queryUpdate(conn, "DROP TABLE IF EXISTS Character;");

        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Faction ("
                + " id integer PRIMARY KEY AUTOINCREMENT,"
                + " name varchar(15) NOT NULL,"
                + " summary varchar(500) NOT NULL);");

        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Character ("
                + " id integer PRIMARY KEY AUTOINCREMENT,"
                + " name varchar(15) NOT NULL,"
                + " dmg double NOT NULL,"
                + " defense double NOT NULL,"
                + " summary varchar(500) NOT NULL,"
                + " idFaction int NOT NULL);");
        UtilsSQLite.disconnect(conn);
    }
    /**
     * 
     * @param filePath 
     */
    private static void insertData(String filePath) {
        Connection conn = UtilsSQLite.connect(filePath);
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faction (name, summary) VALUES (\"Knights\", \"The Knights\");");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faction (name, summary) VALUES (\"Vikings\", \"The Vikings\");");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faction (name, summary) VALUES (\"Samurai\", \"The Samurai\");");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Conquistador\", 130, 150, \"Conquistador\", 1);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Warden\", 140, 120, \"Warden\", 1);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Lawbringer\", 120, 160, \"Lawbringer\", 1);");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Raider\", 150, 110, \"Raider\", 2);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Berserker\", 130, 110, \"Berserker\", 2);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Shaman\", 120, 120, \"Shaman\", 2);");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Orochi\", 120, 140, \"Orochi\", 3);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Nobushi\", 110, 130, \"Nobushi\", 3);");
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Character (name, dmg, defense, summary, idFaction) VALUES (\"Shinobi\", 100, 150, \"Shinobi\", 3);");
        UtilsSQLite.disconnect(conn);
    }
    /**
     * 
     * @param input
     * @param minOption
     * @param maxOption
     * @return 
     */
    private static int getInput(Scanner input, int minOption, int maxOption) {
        int option;
        if (!input.hasNextInt()) {
            return -1;
        }
        option = input.nextInt();
        if (option > maxOption || option < minOption) {
            return -1;
        }

        return option;
    }
    /**
     * 
     * @param title
     * @param columnCount 
     */
    private static void printHeader(String title, int columnCount) {
        int numberOfEquals = (((columnCount * 30) - title.length())) / 2;

        System.out.println("\n");
        for (int i = 0; i < numberOfEquals; i++) {
            System.out.print("=");
        }
        System.out.print(title);
        for (int i = 0; i < numberOfEquals; i++) {
            System.out.print("=");
        }
        System.out.println("\n");
    }
    /**
     * 
     * @param conn
     * @return
     * @throws SQLException 
     */
    private static ArrayList<String> listFactions(Connection conn) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs;
        rs = UtilsSQLite.querySelect(conn, "SELECT name FROM Faction;");

        while (rs.next()) {
            list.add(rs.getString("name"));
        }

        return list;
    }
    /**
     * 
     * @param input
     * @param conn
     * @param tables
     * @param action
     * @param query
     * @param actionHeader
     * @throws SQLException 
     */
    private static void showOption(Scanner input, Connection conn, ArrayList<String> tables, Print action, String query, PrintHeader actionHeader) throws SQLException {
        boolean running = true;
        while (running) {
            int option;

            for (int i = 0; i < tables.size(); i++) {
                System.out.println((i + 1) + ") " + tables.get(i));
            }
            System.out.println("0 to return");

            option = getInput(input, 0, 3);

            if (option == -1) {
                continue;
            }

            if (option == 0) {
                return;
            }
            query = query.replaceAll("REPLACE", tables.get(option - 1));
            action.execute(query, conn, actionHeader, tables.get(option - 1));
        }
    }
    /**
     * 
     * @param query
     * @param conn
     * @param action
     * @param title
     * @throws SQLException 
     */
    private static void printOption(String query, Connection conn, PrintHeader action, String title) throws SQLException {
        ResultSet rs;
        rs = UtilsSQLite.querySelect(conn, query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] columnNames = new String[columnCount];

        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = rsmd.getColumnName(i);
        }
        action.execute(title, columnCount);
        while (rs.next()) {
            for (int i = 1; i < columnCount; i++) {
                System.out.printf("%-30s |  ", columnNames[i] + ": " + rs.getObject(columnNames[i]));
            }
            System.out.println();
        }
        System.out.println("\n");
    }

}
