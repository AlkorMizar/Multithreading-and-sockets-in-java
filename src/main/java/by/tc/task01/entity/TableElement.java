package by.tc.task01.entity;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
@XmlRootElement
public abstract class TableElement{
    protected  HashMap<String,Object> values;
    protected  int primaryKey;

    @XmlElement
    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  TableElement){
            var o=(TableElement) obj;
            return values.equals(o.values) || primaryKey==o.primaryKey;
        }
        return false;
    }

    public abstract void update(TableElement element);
    public abstract HashMap<String,Object> getValues();
    public abstract Object getValue(String criteria);
    public abstract  boolean hasCriteria(String criteria);
    public abstract boolean isCorrect();
    protected abstract boolean containAllCriteria();

}
