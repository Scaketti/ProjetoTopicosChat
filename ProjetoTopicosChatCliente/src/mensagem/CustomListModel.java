package mensagem;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

public class CustomListModel extends AbstractListModel {

    private ArrayList<Cliente> lista = new ArrayList();

    @Override
    public int getSize() {
        return lista.size();
    }

    @Override
    public Object getElementAt(int index) {
        Cliente c = lista.get(index);
        return c.getApelido();
    }

    public void addCliente(Cliente c) {
        lista.add(c);
        this.fireIntervalAdded(this, getSize(), getSize() + 1);
    }

    public void removeCliente(int index0) {
        lista.remove(index0);
        this.fireIntervalRemoved(this, getSize(), getSize() + 1);
    }

    public void removeAll() {
        lista.removeAll(lista);
    }

    public Cliente getCliente(int index) {
        return lista.get(index);
    }

}
