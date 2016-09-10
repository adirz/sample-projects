import java.sql.*;
import java.lang.Math;

class CheckMySavings
{
    private Connection con;
    private Statement stmt;
    private boolean connected = false;
    private boolean inUse = false;
    public void init(String username)
    {
        if(connected) {
            return;
        }
        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://dbcourse/public?user=" + username);
        stmt = con.createStatement();
        connected = true;
    }

    public void close()
    {
        while(!inUse && connected) {}
        connected = false;
        con.close();
        stmt.close();
    }

    private double intrest(double rate, int years, double deposit)
    {
        if(years == 1) {
            return deposit*(1 + rate);
        }
        return deposit + intrest(rate, years - 1, deposit)*(1 + rate);
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public double checkMySavings(int AccountNum, Date openDate)
    {
        while(!connected) {}
        inUse = true;
        double savings = 0;
        String queryStr = "SELECT * FROM Savings WHERE AccountNum = '" + String(AccountNum) + "'";
        ResultSet rs = stmt.executeQuery(queryStr);
        while(rs.next()) {
            try{
                int numOfYears = rs.getShort("NumOfYears");
            }catch (SQLException ex){
                System.out.println("could not find NumOfYears");
                return -1;
            }
            try{
                double deposit = rs.getDouble("Deposit");
            }catch (SQLException ex){
                System.out.println("could not find Deposit");
                return -1;
            }
            try{
                Date deDate = rs.getShort("DepositDate");
            }catch (SQLException ex){
                System.out.println("could not find DepositDate");
                return -1;
            }
            try{
                double intr = rs.getDouble("Interest");
            }catch (SQLException ex){
                System.out.println("could not find Interest");
                return -1;
            }
            int diff = getDiffYears(openDate, deDate);
            if(diff >= numOfYears) {
                savings += intrest(intr, numOfYears, deposit);
            }
            else {
                savings += numOfYears * deposit;
            }
            return savings;
        }
        rs.close();
        inUse = false;
        return 0;
    }
}