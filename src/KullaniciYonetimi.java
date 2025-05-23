import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class KullaniciYonetimi implements IEkleme, IGuncelleme, ISilme, IVeriIslemleri {

    private String id, tc, ad, soyad, sifre, rol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public void Ekle() {
        try {
            Connection conn = DbHelper.getConnection();
            Statement st = conn.createStatement();
            String sql = "INSERT INTO Kullanicilar (tc, ad, soyad, sifre, rol) VALUES ('" + getTc() + "','" + getAd() + "','" + getSoyad() + "','" + getSifre() + "','" + getRol() + "')";
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void Sil() {
        try {
            Connection conn = DbHelper.getConnection();
            Statement st = conn.createStatement();
            String sql = "DELETE FROM Kullanicilar WHERE id = '" + getId() + "'";
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void Guncelle() {
        try {
            Connection conn = DbHelper.getConnection();
            Statement st = conn.createStatement();
            String sql = "UPDATE Kullanicilar SET tc='" + getTc() + "', ad='" + getAd() + "', soyad='" + getSoyad() + "', sifre='" + getSifre() + "', rol='" + getRol() + "' WHERE id='" + getId() + "'";
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public Object Listele() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id");
        model.addColumn("tc");
        model.addColumn("ad");
        model.addColumn("soyad");
        model.addColumn("sifre");
        model.addColumn("rol");

        try (Connection conn = DbHelper.getConnection()) {
            String sql = "SELECT * FROM Kullanicilar";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id"),
                    rs.getString("tc"),
                    rs.getString("ad"),
                    rs.getString("soyad"),
                    rs.getString("sifre"),
                    rs.getString("rol")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
        }

        return model;
    }
    @Override
    public void Ara(JTable table) {
        try (Connection conn = DbHelper.getConnection()) {
            String sql = "SELECT * FROM Kullanicilar WHERE "
                    + "(? = '' OR id LIKE ?) AND "
                    + "(? = '' OR tc LIKE ?) AND "
                    + "(? = '' OR ad LIKE ?) AND "
                    + "(? = '' OR soyad LIKE ?) AND "
                    + "(? = '' OR sifre LIKE ?) AND "
                    + "(? = '' OR rol LIKE ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, getId() == null ? "" : getId());
            stmt.setString(2, "%" + (getId() == null ? "" : getId()) + "%");
            stmt.setString(3, getTc());
            stmt.setString(4, "%" + getTc() + "%");
            stmt.setString(5, getAd());
            stmt.setString(6, "%" + getAd() + "%");
            stmt.setString(7, getSoyad());
            stmt.setString(8, "%" + getSoyad() + "%");
            stmt.setString(9, getSifre());
            stmt.setString(10, "%" + getSifre() + "%");
            stmt.setString(11, getRol());
            stmt.setString(12, "%" + getRol() + "%");
            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("tc"),
                    rs.getString("ad"),
                    rs.getString("soyad"),
                    rs.getString("sifre"),
                    rs.getString("rol")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
        }
    }
}
