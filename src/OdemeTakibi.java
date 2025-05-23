import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class OdemeTakibi implements IEkleme, IGuncelleme, ISilme{

    private String odemeID;
    private String kullaniciID;
    private String miktar;
    private String islemTarihi;

    public String getOdemeID() { return odemeID; }
    public void setOdemeID(String odemeID) { this.odemeID = odemeID; }

    public String getKullaniciID() { return kullaniciID; }
    public void setKullaniciID(String kullaniciID) { this.kullaniciID = kullaniciID; }

    public String getMiktar() { return miktar; }
    public void setMiktar(String miktar) { this.miktar = miktar; }

    public String getIslemTarihi() { return islemTarihi; }
    public void setIslemTarihi(String islemTarihi) { this.islemTarihi = islemTarihi; }


    public void Ekle() {
        String sql = "INSERT INTO Odemeler (kullaniciID, miktar, islemTarihi) VALUES (?, ?, ?)";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, getKullaniciID());
            stmt.setString(2, getMiktar());
            stmt.setString(3, getIslemTarihi());

            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ekleme hatası: " + e.getMessage());
        }
    }

    public void Sil() {
        String sql = "DELETE FROM Odemeler WHERE odemeID = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, getOdemeID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Silme hatası: " + e.getMessage());
        }
    }

    public void Guncelle() {
        String sql = "UPDATE Odemeler SET kullaniciID = ?, miktar = ?, islemTarihi = ? WHERE odemeID = ?";
        try (Connection conn = DbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, getKullaniciID());
            stmt.setString(2, getMiktar());
            stmt.setString(3, getIslemTarihi());
            stmt.setString(4, getOdemeID());

            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Güncelleme hatası: " + e.getMessage());
        }
    }

    public Object Listele() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("odemeID");
        model.addColumn("kullaniciID");
        model.addColumn("miktar");
        model.addColumn("islemTarihi");

        try (Connection conn = DbHelper.getConnection()) {
            String sql = "SELECT * FROM Odemeler";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("odemeID"),
                    rs.getString("kullaniciID"),
                    rs.getString("miktar"),
                    rs.getString("islemTarihi")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Listeleme hatası: " + e.getMessage());
        }

        return model;
    }

    public Object Ara() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("odemeID");
        model.addColumn("kullaniciID");
        model.addColumn("miktar");
        model.addColumn("islemTarihi");

        String sql = "SELECT * FROM Odemeler WHERE "
            + "(? = '' OR odemeID LIKE ?) AND "
            + "(? = '' OR kullaniciID LIKE ?) AND "
            + "(? = '' OR miktar LIKE ?) AND "
            + "(? = '' OR islemTarihi LIKE ?)";

        try (Connection conn = DbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, getOdemeID());
            stmt.setString(2, "%" + getOdemeID() + "%");

            stmt.setString(3, getKullaniciID());
            stmt.setString(4, "%" + getKullaniciID() + "%");

            stmt.setString(5, getMiktar());
            stmt.setString(6, "%" + getMiktar() + "%");

            stmt.setString(7, getIslemTarihi());
            stmt.setString(8, "%" + getIslemTarihi() + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("odemeID"),
                    rs.getString("kullaniciID"),
                    rs.getString("miktar"),
                    rs.getString("islemTarihi")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Arama hatası: " + e.getMessage());
        }

        return model;
    }
}
