package models;

import java.util.List;

public class BatoiLogicCustomer  implements java.io.Serializable
{
     private int id;
     private String name;
     private String nickname;
     private String password;
     private String email;
     private Integer phone;
     private List<Integer> addresses_id;
     private List<Integer> orders_id;
     
    public BatoiLogicCustomer() {
    }
    
    public BatoiLogicCustomer(String name, String nickname, String password) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public List<Integer> getAddresses_id() {
        return addresses_id;
    }

    public void setAddresses_id(List<Integer> addresses_id) {
        this.addresses_id = addresses_id;
    }

    public List<Integer> getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(List<Integer> orders_id) {
        this.orders_id = orders_id;
    }

    @Override
    public String toString() {
        return "BatoiLogicCustomer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", addresses_id=" + addresses_id +
                ", orders_id=" + orders_id +
                '}';
    }
}