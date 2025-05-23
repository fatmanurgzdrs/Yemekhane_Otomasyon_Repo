import javax.swing.JTable;

public interface IVeriIslemleri extends IEkleme {
    abstract Object Listele();
    void Ara(JTable table);
}
