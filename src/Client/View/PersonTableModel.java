package Client.View;

import Models.Person;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PersonTableModel  extends AbstractTableModel {
    private List<Person> persons;
    private final String[] colunas = new String[]{ "Codigo", "Nome"};

    public PersonTableModel(){
        this.persons = new ArrayList<>();
    }

    public PersonTableModel(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public int getRowCount() {
        return persons.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int columnIndex){
        return colunas[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }


    public void setValueAt(Person aValue, int rowIndex) {
        Person person = persons.get(rowIndex);

        person.setId(aValue.getId());
        person.setName(aValue.getName());

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Person person = persons.get(rowIndex);

        switch (columnIndex) {
            case 0:
                person.setId(Integer.parseInt(aValue.toString()));
            case 1:
                person.setName(aValue.toString());
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Person selectedPerson = persons.get(rowIndex);
        Object valueObject = null;

        switch(columnIndex){
            case 0: valueObject = selectedPerson.getId(); break;
            case 1: valueObject = selectedPerson.getName(); break;
        }

        return valueObject;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Person getPerson(int index) {
        return persons.get(index);
    }

    public void addPerson(Person person) {
        persons.add(person);

        int lastIndex;
        lastIndex = getRowCount() - 1;

        fireTableRowsInserted(lastIndex, lastIndex);
    }

    public void addFirstPerson(Person person) {
        persons.add(0, person);

        int lastIndex;
        lastIndex = getRowCount() - 1;

        fireTableRowsInserted(lastIndex, lastIndex);
    }

    public void removePerson(int index) {
        persons.remove(index);

        fireTableRowsDeleted(index, index);
    }

    public void limpar() {
        persons.clear();
        fireTableDataChanged();
    }

    public boolean isEmpty() {
        return persons.isEmpty();
    }
}