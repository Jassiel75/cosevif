package utez.edu.mx.cosevif.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "residents")
public class Resident {
    @Id
    private String id;
    private String name;
    private String surnames;
    private int age;
    private String birthDate;
    private String email;
    private String address;
    private String street;
    private String phone;
    private String password;

    @DBRef
    private House house;  // Relación con la casa

    public Resident() {
    }

    public Resident(String id, String name, String surnames, int age, String birthDate, String email, String address, String street, String phone, String password, House house) {
        this.id = id;
        this.name = name;
        this.surnames = surnames;
        this.age = age;
        this.birthDate = birthDate;
        this.email = email;
        this.address = address;
        this.street = street;
        this.phone = phone;
        this.password = password;
        this.house = house;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}