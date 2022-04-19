 package za.co.wethinkcode.ServerClient.database;

 import java.io.File;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 import java.sql.Statement;

 import net.lemnik.eodsql.QueryTool;
 import za.co.wethinkcode.ServerClient.database.dai.WorldDAI;

 /**
  * The RobotWorldsDB class that connects to the RobotWorlds database and executes all the CRUD operations.
  */
 public class RobotWorldsDB {
     public static final String DISK_DB_URL_PREFIX = "jdbc:sqlite:";
     private static Connection connection;
     private String dbUrl = null;
     private final WorldDAI robotWorldQuery;

     /**
      * RobotWorldsDB constructor that takes a String array parameter.
      *
      * @param args the string array containing arguments
      */
     public RobotWorldsDB(String[] args) {
         processCmdLineArgs(args);
         try {
             System.out.println("Connecting to database '" + args[1] + "'...");

             connection = DriverManager.getConnection(dbUrl);
             System.out.println("\t> connected to '" + dbUrl + "'");
             createTables();
             robotWorldQuery = QueryTool.getQuery(connection, WorldDAI.class);
         } catch (SQLException ex) {
             System.out.println("Connection failed!");
             throw new RuntimeException(ex);
         }
     }

     /**
      * Creates the RobotWorlds database.
      * @throws SQLException An exception that provides information on a database access error or other errors.
      */
     private void createTables() throws SQLException {
         Statement stmt = connection.createStatement();

         String sql = "PRAGMA foreign_keys = ON; "
                + "CREATE TABLE world ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT UNIQUE NOT NULL, "
                + "size INT NOT NULL, "
                + "attributes TEXT NOT NULL );"


         + "CREATE TABLE `obstacle` ( "
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`size` INT NOT NULL, "
                + "`position` TEXT UNIQUE NOT NULL, "
                + "`world_id` INT NOT NULL, "
                + "FOREIGN KEY (`world_id`) REFERENCES `world` (`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE );"

                + "CREATE TABLE `pit` ( "
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`size` INT NOT NULL, "
                + "`position` TEXT UNIQUE NOT NULL, "
                + "`world_id` INT NOT NULL, "
                + "FOREIGN KEY (`world_id`) REFERENCES `world` (`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE );"


                + "CREATE TABLE `mine` ( "
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`size` INT NOT NULL, "
                + "`position` TEXT UNIQUE NOT NULL, "
                + "`world_id` INT NOT NULL, "
                + "FOREIGN KEY (`world_id`) REFERENCES `world` (`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE );"

                + "CREATE TABLE `robot` ( "
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`name` TEXT UNIQUE NOT NULL, "
                + "`position` TEXT UNIQUE NOT NULL, "
                + "`direction` TEXT NOT NULL, "
                + "`world_id` INT NOT NULL, "
                + "FOREIGN KEY (`world_id`) REFERENCES `world` (`id`) "
                + "ON DELETE CASCADE ON UPDATE CASCADE);"

                + "CREATE TABLE `server` ( "
                + "`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "`ip` TEXT UNIQUE NOT NULL, "
                + "`name` TEXT UNIQUE NOT NULL, "
                + "`port` INT NOT NULL); ";

         stmt.execute(sql);
     }

     /**
      * Processes the command line arguments specified in the parameter - string array.
      * @param args
      */
     private void processCmdLineArgs(String[] args) {
         if (args.length == 2 && args[0].equals("-f")) {
             final File dbFile = new File(args[1]);
             String databasePath = System.getProperty("user.dir") + "/" + args[1];
             if (dbFile.exists()) {
                 dbUrl = DISK_DB_URL_PREFIX + databasePath;
             } else {
                 throw new IllegalArgumentException("Database file " + dbFile.getName() + " not found.");
             }
         } else {
             throw new RuntimeException("Expected arguments '-f filename' but didn't find it.");
         }
     }

     /**
      * @return  Returns the database connection
      */
     public static Connection getConnection() {
         return connection;
     }
 }