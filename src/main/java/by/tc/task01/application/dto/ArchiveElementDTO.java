package by.tc.task01.application.dto;

import by.tc.task01.entity.TableElement;
import by.tc.task01.entity.impl.ArchiveElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.Locale;

@XmlRootElement
public class ArchiveElementDTO {
    HashMap<String,Object> values;
    public ArchiveElementDTO(ArchiveElement element){
        values= element.getValues();
    }

    public ArchiveElementDTO(TableElement element){
        values= element.getValues();
    }

    public ArchiveElementDTO(){
        values= new HashMap<>();
    }


    public ArchiveElementDTO(HashMap<String,Object> values){
        this.values= values;
    }

    public void addField(String cr,String val){
        Object obj=val;
        switch (cr){
            case "scholarship":
            case "age":
            case "grade":
                obj=Integer.parseInt(val);
                break;
            case "onScholarship":
                if(val.toLowerCase(Locale.ROOT).matches("(yes)|(no)")){
                    obj=val.toLowerCase(Locale.ROOT).matches("yes");
                }else{throw new IllegalArgumentException();}
                break;
            case "rating":
                obj=Double.parseDouble(val);
                break;
        }
        values.put(cr,obj);

    }

    public ArchiveElement convert(){
        return new ArchiveElement(values);
    }

    public  String toString(String[] criteria){
        StringBuilder res=new StringBuilder();
        for (var ct:criteria ) {
            if (values.containsKey(ct)){
                res.append(ct+" | "+values.get(ct)+" | ");
            }
        }
        return res.toString();
    }

    @Override
    public  String toString(){
        StringBuilder res=new StringBuilder();
        String[] criteria={"name","country","city","street","house","grade","descriptionLink","age","rating","onScholarship"};
        for (var cr: criteria ) {
            if(values.containsKey(cr)){
                res.append(cr+":"+values.get(cr)+"\n");
            }
        }
        if (values.containsKey("onScholarship") && (boolean)values.get("onScholarship")){
            res.append("scholarship|"+values.get("scholarship"));
        }
        return res.toString();
    }

    @XmlElement
    public HashMap<String, Object> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }
}
