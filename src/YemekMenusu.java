import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class YemekMenusu implements IEkleme, IGuncelleme, ISilme, IVeriIslemleri {

    private String menuID;
    private String tarih;
    private String anaYemek;
    private String yanYemek;
    private String corba;
    private String tatli;

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getAnaYemek() {
        return anaYemek;
    }

    public void setAnaYemek(String anaYemek) {
        this.anaYemek = anaYemek;
    }

    public String getYanYemek() {
        return yanYemek;
    }

    public void setYanYemek(String yanYemek) {
        this.yanYemek = yanYemek;
    }

    public String getCorba() {
        return corba;
    }

    public void setCorba(String corba) {
        this.corba = corba;
    }

    public String getTatli() {
        return tatli;
    }

    public void setTatli(String tatli) {
        this.tatli = tatli;
    }

    @Override
    public void Ekle(){
        try {
            Connection conn;
            conn = DbHelper.getConnection();
            String sql = "INSERT INTO Menuler (tarih, anaYemek, yanYemek, corba, tatli) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, tarih);
                stmt.setString(2, anaYemek);
                stmt.setString(3, yanYemek);
                stmt.setString(4, corba);
                stmt.setString(5, tatli);
                stmt.executeUpdate();
            }
            catch (SQLException ex){
                
            }
        }
        catch (SQLException ex){
            Logger.getLogger(YemekMenusu.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    @Override
    public void Sil() {
        Connection conn = null;
        try {
            conn = DbHelper.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(YemekMenusu.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "DELETE FROM Menuler WHERE menuID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, menuID);  // menuID String olduğu için setString kullanıldı
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(YemekMenusu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Guncelle(){
        Connection conn = null;
        try {
            conn = DbHelper.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(YemekMenusu.class.getName()).log(Level.SEVERE, null, ex);
        }
        String sql = "UPDATE Menuler SET anaYemek=?, yanYemek=?, corba=?, tatli=? WHERE tarih=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, anaYemek);
            stmt.setString(2, yanYemek);
            stmt.setString(3, corba);
            stmt.setString(4, tatli);
            stmt.setString(5, tarih);
            stmt.executeUpdate();
        }
        catch (SQLException ex){
            Logger.getLogger(YemekMenusu.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    @Override
    public Object Listele() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("menuID");
        model.addColumn("tarih");
        model.addColumn("anaYemek");
        model.addColumn("yanYemek");
        model.addColumn("corba");
        model.addColumn("tatli");

        try (Connection conn = DbHelper.getConnection()) {
            String sql = "SELECT * FROM Menuler";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("menuID"),
                    rs.getString("tarih"),
                    rs.getString("anaYemek"),
                    rs.getString("yanYemek"),
                    rs.getString("corba"),
                    rs.getString("tatli")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
        }

        return model;
    }

    /**
     *
     * @param table
     */
    @Override
    public void Ara(JTable table){
        try (Connection conn = DbHelper.getConnection()) {
            String sql = "SELECT * FROM Menuler WHERE "
                    + "(? = '' OR menuID LIKE ?) AND "
                    + "(? = '' OR tarih LIKE ?) AND "
                    + "(? = '' OR anaYemek LIKE ?) AND "
                    + "(? = '' OR yanYemek LIKE ?) AND "
                    + "(? = '' OR corba LIKE ?) AND "
                    + "(? = '' OR tatli LIKE ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, getMenuID());
            stmt.setString(2, "%" + getMenuID() + "%");

            stmt.setString(3, getTarih());
            stmt.setString(4, "%" + getTarih() + "%");

            stmt.setString(5, getAnaYemek());
            stmt.setString(6, "%" + getAnaYemek() + "%");

            stmt.setString(7, getYanYemek());
            stmt.setString(8, "%" + getYanYemek() + "%");

            stmt.setString(9, getCorba());
            stmt.setString(10, "%" + getCorba() + "%");

            stmt.setString(11, getTatli());
            stmt.setString(12, "%" + getTatli() + "%");

            ResultSet rs = stmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("menuID"),
                    rs.getString("tarih"),
                    rs.getString("anaYemek"),
                    rs.getString("yanYemek"),
                    rs.getString("corba"),
                    rs.getString("tatli")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage());
        }
    }
}
