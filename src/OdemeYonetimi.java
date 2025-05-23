
import java.sql.*;

public class OdemeYonetimi implements IEkleme {
    private int odemeID;
    private int kullaniciID;
    private double miktar;
    private Timestamp islemTarihi;
    public OdemeYonetimi(int kullaniciID, double miktar, Timestamp islemTarihi) {
        this.kullaniciID = kullaniciID;
        this.miktar = miktar;
        this.islemTarihi = islemTarihi;
    }
    @Override
    public void Ekle() {
        Connection conn = null;
        PreparedStatement odemeStmt = null;
        PreparedStatement bakiyeGuncelleStmt = null;
        PreparedStatement bakiyeKontrolStmt = null;
        ResultSet rs = null;
        try {
            conn = DbHelper.getConnection();
            conn.setAutoCommit(false);

            String bakiyeSorgu = "SELECT bakiye FROM Bakiye WHERE kullaniciID = ?";
            bakiyeKontrolStmt = conn.prepareStatement(bakiyeSorgu);
            bakiyeKontrolStmt.setInt(1, kullaniciID);
            rs = bakiyeKontrolStmt.executeQuery();

            double mevcutBakiye = 0;
            if (rs.next()) {
                mevcutBakiye = rs.getDouble("bakiye");
            }

            if (mevcutBakiye < miktar) {
                conn.rollback();
                return;
            }

            String odemeSQL = "INSERT INTO Odemeler (kullaniciID, miktar, islemTarihi) VALUES (?, ?, ?)";
            odemeStmt = conn.prepareStatement(odemeSQL);
            odemeStmt.setInt(1, kullaniciID);
            odemeStmt.setDouble(2, miktar);
            odemeStmt.setTimestamp(3, islemTarihi);
            odemeStmt.executeUpdate();

            String guncelleSQL = "UPDATE Bakiye SET bakiye = bakiye - ? WHERE kullaniciID = ?";
            bakiyeGuncelleStmt = conn.prepareStatement(guncelleSQL);
            bakiyeGuncelleStmt.setDouble(1, miktar);
            bakiyeGuncelleStmt.setInt(2, kullaniciID);
            bakiyeGuncelleStmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (odemeStmt != null) {
                    odemeStmt.close();
                }
                if (bakiyeGuncelleStmt != null) {
                    bakiyeGuncelleStmt.close();
                }
                if (bakiyeKontrolStmt != null) {
                    bakiyeKontrolStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }
    public class Bakiye {
        private int bakiyeID;
        private int kullaniciID;
        private double bakiye;
        
        public Bakiye(int kullaniciID, double bakiye) {
            this.kullaniciID = kullaniciID;
            this.bakiye = bakiye;
        }
        public Bakiye(int bakiyeID, int kullaniciID, double bakiye) {
            this.bakiyeID = bakiyeID;
            this.kullaniciID = kullaniciID;
            this.bakiye = bakiye;
        }
        public int getBakiyeID() {
            return bakiyeID;
        }
        public void setBakiyeID(int bakiyeID) {
            this.bakiyeID = bakiyeID;
        }

        public int getKullaniciID() {
            return kullaniciID;
        }

        public void setKullaniciID(int kullaniciID) {
            this.kullaniciID = kullaniciID;
        }

        public double getBakiye() {
            return bakiye;
        }

        public void setBakiye(double bakiye) {
            this.bakiye = bakiye;
        }
    }

}
