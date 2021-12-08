package by.tc.task01.entity.impl;

import by.tc.task01.entity.TableElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.validator.UrlValidator;

import java.util.HashMap;

@XmlRootElement
public class ArchiveElement extends TableElement {
    private String name;
    private String country;
    private String city;
    private String street;
    private String house;
    private int age;
    private int grade;
    private double rating;
    private String descriptionLink;
    private boolean onScholarship;
    private int scholarship;

    public ArchiveElement(int primaryKey, String name, String country, String city, String street, String house, int age, int grade, double rating, String descriptionLink, int scholarship) {
        values=new HashMap<>();

        setName(name);
        setPrimaryKey(primaryKey);
        setCountry(country);
        setCity(city);
        setStreet(street);
        setHouse(house);
        setAge(age);
        setGrade(grade);
        setRating(rating);
        setDescriptionLink(descriptionLink);
        setScholarship(scholarship);
    }

    public ArchiveElement(HashMap<String,Object> val){
        this(0,(String) val.get("name"),(String) val.get("country"),(String) val.get("city"),
                (String) val.get("street"),(String) val.get("house"),(int)val.get("age"),(int) val.get("grade"),
                (double)val.get("rating"),(String) val.get("descriptionLink"),
                (boolean)val.get("onScholarship")?(int)val.get("scholarship"):-1);
    }

    public ArchiveElement() {
        values=new HashMap<>();
    }

    @XmlElement
    public String getName() {
        return name;
    }
    @XmlElement
    public String getCountry() {
        return country;
    }
    @XmlElement
    public String getCity() {
        return city;
    }
    @XmlElement
    public String getStreet() {
        return street;
    }
    @XmlElement
    public String getHouse() {
        return house;
    }
    @XmlElement
    public int getAge() {
        return age;
    }
    @XmlElement
    public int getGrade() {
        return grade;
    }
    @XmlElement
    public double getRating() {
        return rating;
    }
    @XmlElement
    public String getDescriptionLink() {
        return descriptionLink;
    }
    @XmlElement
    public boolean isOnScholarship() {
        return onScholarship;
    }
    @XmlElement
    public int getScholarship() {
        return scholarship;
    }

    public void setName(String name) {
        if(name.matches("^([A-Za-z]+\\s*)+$")){
            values.put("name",name);
            this.name = name;
        }
    }


    public void setCountry(String country) {
        if(country.matches("^([A-Za-z]+\\s*)+$")) {
            values.put("country", country);
            this.country = country;
        }
    }

    public void setCity(String city) {
        if(city.matches("^([A-Za-z]+\\s*)+$")) {
            values.put("city", city);
            this.city = city;
        }
    }

    public void setStreet(String street) {
        if(street.matches("^([A-Za-z]+\\s*)+$")) {
            values.put("street", street);
            this.street = street;
        }
    }

    public void setHouse(String house) {
        if(house.matches("^([A-Za-z0-9]+\\s*)+$")) {
            values.put("house", house);
            this.house = house;
        }
    }

    public void setAge(int age) {
        if (age>=6 && age<=120) {
            values.put("age", age);
            this.age = age;
        }
    }

    public void setGrade(int grade) {
        if (grade>=1 && grade<=11) {
            values.put("grade", grade);
            this.grade = grade;
        }
    }

    public void setRating(double rating) {
        if(rating>=0 && rating <=10) {
            values.put("rating", rating);
            this.rating = rating;
        }
    }

    public void setDescriptionLink(String descriptionLink) {
        if(descriptionLink.matches("https://\\w+.\\w+(/\\w+)+")){
            values.put("descriptionLink", descriptionLink);
            this.descriptionLink = descriptionLink;
        }
    }

    public void setScholarship(int scholarship) {
        if(scholarship>=0){
            values.put("scholarship",scholarship);
            this.scholarship = scholarship;
        }
        values.put("onScholarship",scholarship>0);
        this.onScholarship = scholarship>0;
    }

    @Override
    public void update(TableElement element) {
        if(element instanceof ArchiveElement) {
            var el=(ArchiveElement) element;
            setName(el.name);
            setPrimaryKey(el.primaryKey);
            setCountry(el.country);
            setCity(el.city);
            setStreet(el.street);
            setHouse(el.house);
            setAge(el.age);
            setGrade(el.grade);
            setRating(el.rating);
            setDescriptionLink(el.descriptionLink);
            setScholarship(el.scholarship);
        }
    }

    @Override
    public HashMap<String, Object> getValues() {
        return (HashMap<String, Object>)values.clone();
    }

    @Override
    public Object getValue(String criteria) {
        return values.get(criteria);
    }

    @Override
    public boolean hasCriteria(String criteria) {
        return values.containsKey(criteria);
    }

    @Override
    public boolean isCorrect() {
        return containAllCriteria();
    }

    @Override
    protected boolean containAllCriteria() {
        String[] criteria={"name","country","city","street","house","grade","descriptionLink","age","rating","onScholarship"};
        for (var cr: criteria ) {
            if(!values.containsKey(cr)){
                return false;
            }
        }
        return !onScholarship || values.containsKey("scholarship");
    }
}
