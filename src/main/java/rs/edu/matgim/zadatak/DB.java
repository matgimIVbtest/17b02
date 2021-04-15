package rs.edu.matgim.zadatak;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DB {

    String connectionString = "jdbc:sqlite:src\\main\\java\\KompanijaZaPrevoz.db";

    public void printFirma() {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT * FROM Firma");
            while (rs.next()) {
                int IdFil = rs.getInt("IdFir");
                String Naziv = rs.getString("Naziv");
                String Adresa = rs.getString("Adresa");
                String Tel1 = rs.getString("Tel1");
                String Tel2 = rs.getString("Tel2");

                System.out.println(String.format("%d\t%s\t%s\t%s\t%s", IdFil, Naziv, Adresa, Tel1, Tel2));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }
    public void printRelVrednostPosiljki() {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT * FROM Posiljka");
            while (rs.next()) {
                int IdPos = rs.getInt("IDPos");
                float T = rs.getFloat("Tezina");
                int V = rs.getInt("Vrednost");
                String Mo = rs.getString("MestoOd");
                String Md = rs.getString("MestoDo");
                int IdFir = rs.getInt("IdFir");
                float RelVrednost = V/T;

                System.out.println(String.format("%d\t%f\t%s\t%s\t%d",IdPos,RelVrednost,Mo,Md,IdFir));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }
    
    
    
    
    public int zadatak(String Imeiprezime,String Kategorija)
    {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {
            conn.setAutoCommit(false);
        int idz=napravizaposlenog(conn, Imeiprezime);
        
        napravivozaca(conn,Kategorija,idz);
        
         
         ResultSet rs = s.executeQuery("Select IDPut FROM Putovanje WHERE Status='N' EXCEPT SELECT IDPut FROM Vozi");
         if(rs.next())
         { 
             int idp=rs.getInt(1);
             PreparedStatement s2=conn.prepareStatement("INSERT INTO Vozi (IDZap,IDPut) VALUES (?,?)");
             s2.setInt(1,idz);
             s2.setInt(2, idp);
             s2.execute();
             System.out.println("Uspesna realizacija");
                conn.commit();
                conn.setAutoCommit(true);
             return idp;
         }
         
         else
         {
             ResultSet rs1 = s.executeQuery("SELECT min(IDPut) FROM Putovanje WHERE Status='N'");
             if(rs1.next())
             { int idp=rs1.getInt(1);
             PreparedStatement s3=conn.prepareStatement("INSERT INTO Vozi (IDZap,IDPut) VALUES (?,?)");
             s3.setInt(1,idz);
             s3.setInt(2, idp);
             s3.execute();
             System.out.println("Uspesna realizacija");
                conn.commit();
                conn.setAutoCommit(true);
            return idp;}
             
              System.out.println("Uspesna realizacija");
                conn.commit();
                conn.setAutoCommit(true);
            return -1;

         }
     
        
    }   catch (SQLException ex) {
            System.out.println("Dogodila se grska");
            System.out.println(ex);
        }
            return 0; 
        }
    
}
    
     int napravizaposlenog(Connection conn,String Imeiprezime) throws SQLException {

        try (Statement s = conn.createStatement()) {

            PreparedStatement s1=conn.prepareStatement("INSERT INTO Zaposlen (IDZap,ImePrezime,Staz) VALUES (?,?,?)");
        ResultSet rs = s.executeQuery("SELECT Max(IDZap)+1 FROM Zaposlen");
        rs.next();
        int IDz=rs.getInt(1);
        s1.setInt(1,IDz);
        s1.setString(2,Imeiprezime);
        s1.setInt(3, 0);
        s1.execute();

            return IDz;
        }

        
        void napravivozaca(Connection conn,String Kategorija,int IDz) throws SQLException {

        try (Statement s = conn.createStatement()) {

            PreparedStatement s1=conn.prepareStatement("INSERT INTO Vozac (Kategorija,IDZap) VALUES (?,?)");
         s1.setInt(2, IDz);
         s1.setString(1, Kategorija);
          s1.execute();
        }
        
}
}