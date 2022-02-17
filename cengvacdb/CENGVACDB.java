package ceng.ceng351.cengvacdb;

import ceng.ceng351.cengvacdb.*;

import java.sql.*;

public class CENGVACDB implements ICENGVACDB {

    private static String user = "e2467447"; // TODO: Your userName
    private static String password = "hGcfDq8KkxIM"; //  TODO: Your password
    private static String host = "144.122.71.57"; // host name
    private static String database = "db2467447"; // TODO: Your database name
    private static int port = 8084; // port

    private static Connection connection = null;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false";

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, this.user, this.password);
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {

        int numOfTables = 0;

        // User(userID:int, userName:varchar(30), age:int, address:varchar(150), password:varchar(30), status:varchar(15))
        String queryCreateUser = "CREATE TABLE IF NOT EXISTS User(" +
                "userID INT NOT NULL, " +
                "userName VARCHAR(30), " +
                "age INT, " +
                "address VARCHAR(150), " +
                "password VARCHAR(30), " +
                "status VARCHAR(15), " +
                "PRIMARY KEY (userID));";

        // Vaccine (code:int, vaccinename:varchar(30), type:varchar(30))
        String queryCreateVaccine = "CREATE TABLE IF NOT EXISTS Vaccine(" +
                "code INT NOT NULL, " +
                "vaccinename VARCHAR(30), " +
                "type VARCHAR(30), " +
                "PRIMARY KEY (code));";

        // Vaccination (code:int, userID:int, dose:int, vacdate:date) References Vaccine (code), User (userID)
        String queryCreateVaccination = "CREATE TABLE IF NOT EXISTS Vaccination(" +
                "code int, " +
                "userID int, " +
                "dose int, " +
                "vacdate date, " +
                "FOREIGN KEY (code) REFERENCES Vaccine(code) " +
                "FOREIGN KEY (userID) REFERENCES User(userID) " +
                "PRIMARY KEY (code, userID));";

        // AllergicSideEffect (effectcode:int, effectname:varchar(50))
        String queryCreateAllergicSideEffect = "CREATE TABLE IF NOT EXISTS AllergicSideEffect(" +
                "effectcode INT, " +
                "effectname VARCHAR(50), " +
                "PRIMARY KEY (effectcode));";

        // Seen (effectcode:int, code:int, userID:int, date:date, degree:varchar(30)) References Allergic- SideEffect (effectcode), Vaccination (code), User (userID)
        String queryCreateSeen = "CREATE TABLE IF NOT EXISTS Seen(" +
                "effectcode INT, " +
                "code INT, " +
                "userID INT, " +
                "date DATE, " +
                "degree VARCHAR(30), " +
                "FOREIGN KEY (effectcode) REFERENCES AllergicSideEffect(effectcode) " +
                "FOREIGN KEY (code) REFERENCES Vaccine(code) " +
                        "ON DELETE CASCADE" +
                "FOREIGN KEY (userID) REFERENCES User(userID) " +
                "PRIMARY KEY (effectcode, code, userID));";

        try
        {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(queryCreateUser);
            numOfTables += 1;

            statement.executeUpdate(queryCreateVaccine);
            numOfTables += 1;

            statement.executeUpdate(queryCreateVaccination);
            numOfTables += 1;

            statement.executeUpdate(queryCreateAllergicSideEffect);
            numOfTables += 1;

            statement.executeUpdate(queryCreateSeen);
            numOfTables += 1;

            statement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numOfTables;
    }

    @Override
    public int dropTables() {
        int numOfTables = 0;

        String queryDropUser = "DROP TABLE IF EXISTS User;";
        String queryDropVaccine = "DROP TABLE IF EXISTS Vaccine;";
        String queryDropVaccination = "DROP TABLE IF EXISTS Vaccination;";
        String queryDropAllergicSideEffect = "DROP TABLE IF EXISTS AllergicSideEffect;";
        String queryDropSeen = "DROP TABLE IF EXISTS Seen;";

        try
        {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(queryDropSeen);
            numOfTables += 1;

            statement.executeUpdate(queryDropAllergicSideEffect);
            numOfTables += 1;

            statement.executeUpdate(queryDropVaccination);
            numOfTables += 1;

            statement.executeUpdate(queryDropVaccine);
            numOfTables += 1;

            statement.executeUpdate(queryDropUser);
            numOfTables += 1;

            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return numOfTables;
    }

    @Override
    public int insertUser(User[] users) {

        int numOfUsers = 0;
        // User(userID:int, userName:varchar(30), age:int, address:varchar(150), password:varchar(30), status:varchar(15))
        String insertUser = "INSERT INTO User(" +
                "userID, " +
                "userName, " +
                "age, " +
                "address, " +
                "password, " +
                "status) VALUES (?, ?, ?, ?, ?, ?);";

        for(int i = 0; i < users.length; i += 1)
        {
            User temp = users[i];

            try
            {
                PreparedStatement mystatement = this.connection.prepareStatement(insertUser);
                mystatement.setInt(1, temp.getUserID());
                mystatement.setString(2, temp.getUserName());
                mystatement.setInt(3, temp.getAge());
                mystatement.setString(4, temp.getAddress());
                mystatement.setString(5, temp.getPassword());
                mystatement.setString(6, temp.getStatus());

                mystatement.executeUpdate();
                mystatement.close();
                numOfUsers += 1;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return numOfUsers;
    }

    @Override
    public int insertAllergicSideEffect(AllergicSideEffect[] sideEffects) {
        // AllergicSideEffect (effectcode:int, effectname:varchar(50))
        int numOfSideEffects = 0;

        String insertSideEffect = "INSERT INTO AllergicSideEffect(" +
                "effectcode, " +
                "effectname) VALUES (?, ?);";

        for (int i=0; i < sideEffects.length; i += 1) {
            AllergicSideEffect temp = sideEffects[i];

            try
            {
                PreparedStatement mystatement = this.connection.prepareStatement(insertSideEffect);
                mystatement.setInt(1, temp.getEffectCode());
                mystatement.setString(2, temp.getEffectName());

                mystatement.executeUpdate();
                mystatement.close();
                numOfSideEffects += 1;
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return numOfSideEffects;
    }

    @Override
    public int insertVaccine(Vaccine[] vaccines) {
        return 0;
    }

    @Override
    public int insertVaccination(Vaccination[] vaccinations) {
        return 0;
    }

    @Override
    public int insertSeen(Seen[] seens) {
        return 0;
    }

    @Override
    public Vaccine[] getVaccinesNotAppliedAnyUser() {
        return new Vaccine[0];
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersforTwoDosesByDate(String vacdate) {
        return new QueryResult.UserIDuserNameAddressResult[0];
    }

    @Override
    public Vaccine[] getTwoRecentVaccinesDoNotContainVac() {
        return new Vaccine[0];
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersAtHasLeastTwoDoseAtMostOneSideEffect() {
        return new QueryResult.UserIDuserNameAddressResult[0];
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersWithAllVaccinesCanCauseGivenSideEffect(String effectname) {
        return new QueryResult.UserIDuserNameAddressResult[0];
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersWithAtLeastTwoDifferentVaccineTypeByGivenInterval(String startdate, String enddate) {
        return new QueryResult.UserIDuserNameAddressResult[0];
    }

    @Override
    public AllergicSideEffect[] getSideEffectsOfUserWhoHaveTwoDosesInLessThanTwentyDays() {
        return new AllergicSideEffect[0];
    }

    @Override
    public double averageNumberofDosesofVaccinatedUserOverSixtyFiveYearsOld() {
        return 0;
    }

    @Override
    public int updateStatusToEligible(String givendate) {
        return 0;
    }

    @Override
    public Vaccine deleteVaccine(String vaccineName) {
        return null;
    }
}
