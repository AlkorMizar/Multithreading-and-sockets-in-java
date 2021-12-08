package by.tc.task01.dao.impl;

import by.tc.task01.application.dto.ArchiveElementDTO;
import by.tc.task01.entity.TableElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Shell for list class to parse it in and out of xml
 */
@XmlRootElement
public class Table {

    @XmlElementWrapper(name = "elements")
    @XmlElement(name = "element")
    public ConcurrentLinkedQueue<TableElement> table;

    public Table(){
        table =new ConcurrentLinkedQueue<>();
    }

    public Table(ConcurrentLinkedQueue<TableElement> source){
        table =source;
    }

    public Optional<TableElement> get(int i){
        return table.stream().filter(c -> c.getPrimaryKey()==i).findFirst();
    }

    public Optional<TableElement> find(String[] criteria,TableElement elemToCompare){
        return table.stream().filter(c ->filter(criteria,c,elemToCompare)).findFirst();
    }

    private  boolean filter(String[] criteria, TableElement elem, TableElement with){
        for (String criterion : criteria) {
            if (!elem.hasCriteria(criterion) ||
                !elem.getValue(criterion).equals(with.getValue(criterion))) {
                return false;
            }
        }
        return true;
    }


    public boolean add(TableElement tableElement){
        int primK=-1;
        try {
            primK= table
                    .stream()
                    .max(Comparator.comparing(TableElement::getPrimaryKey))
                    .map(TableElement::getPrimaryKey).orElseGet(() -> 5 + table.size() * 2);
            tableElement.setPrimaryKey(primK+1);
            table.add(tableElement);
            return true;
        }catch (Exception e){return false;}


    }

    public boolean remove(TableElement tableElement){
        try {
            table.remove(tableElement);
            return true;
        }catch (Exception e){return false;}

    }

    public boolean update(TableElement oldApp,TableElement newApp){
        try {
            var elem=table.stream().filter(c -> {
                return  c.equals(oldApp);
            }).findFirst();
            if (elem.isPresent()){
                elem.get().update(newApp);
                return true;
            }
        }catch (Exception ignore){}
        return false;
    }

    public List<TableElement> getCopy(){
        return new LinkedList<>(table);
    }
}
