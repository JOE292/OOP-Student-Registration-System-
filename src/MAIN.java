import gui.mainFrame;
import util.DBConnection;
import util.DatabaseInitializer;
import util.Logger;

import java.sql.Connection;

public class MAIN {
    public static void main(String[] args) {
        try {
            // Read config
            String url = util.ConfigReader.get("db.url");
            String user = util.ConfigReader.get("db.user");
            String pass = util.ConfigReader.get("db.pass");

            String dbName = url.substring(url.lastIndexOf("/") + 1);

            DatabaseInitializer.createDatabaseIfNotExists(dbName, user, pass);

            Connection conn = DBConnection.connect();

            DatabaseInitializer.createTables(conn);

            javax.swing.SwingUtilities.invokeLater(() -> {
                new mainFrame(conn).setVisible(true);
            });

        } catch (Exception e) {
            Logger.log("ERROR", "Failed to start application: " + e.getMessage());
        }
    }
}