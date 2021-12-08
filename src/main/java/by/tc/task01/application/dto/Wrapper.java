package by.tc.task01.application.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collection;
import java.util.List;

@XmlRootElement
public class Wrapper<T> {

    @XmlElementWrapper
    @XmlElement
    public List<T> table;

    public Wrapper(List<T> table){
        this.table=table;
    }
    public Wrapper(){
    }

    public List<T> getTable() {
        return table;
    }

    public void setTable(List<T> table) {
        this.table = table;
    }
}
