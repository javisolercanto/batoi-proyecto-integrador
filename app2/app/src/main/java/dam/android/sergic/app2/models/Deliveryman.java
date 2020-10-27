package dam.android.sergic.app2.models;

import java.util.List;

public class Deliveryman
{
    private int id;
    private String nickname;
    private String name;
    private String password;
    private String email;
    private int phone;

    private List<Integer> route_ids;
    private Lorry myLorry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public List<Integer> getRoute_ids() {
        return route_ids;
    }

    public void setRoute_ids(List<Integer> route_ids) {
        this.route_ids = route_ids;
    }

    public Lorry getMyLorry() {
        return myLorry;
    }

    public void setMyLorry(Lorry myLorry) {
        this.myLorry = myLorry;
    }
}