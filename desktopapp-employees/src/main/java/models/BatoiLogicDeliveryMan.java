package models;
// Generated 12 feb. 2020 0:21:54 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * BatoiLogicDeliveryMan generated by hbm2java
 */
public class BatoiLogicDeliveryMan implements java.io.Serializable {

    private int id;
    private BatoiLogicLorry batoiLogicLorry;
    private String name;
    private String nickname;
    private String password;
    private String email;
    private Integer phone;
    private Integer createUid;
    private Date createDate;
    private Integer writeUid;
    private Date writeDate;
    private Set batoiLogicLocations = new HashSet(0);
    private Set batoiLogicRoutes = new HashSet(0);

    public BatoiLogicDeliveryMan() {
    }

    public BatoiLogicDeliveryMan(BatoiLogicLorry batoiLogicLorry, String name, String nickname, String password) {
        this.batoiLogicLorry = batoiLogicLorry;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
    }

    public BatoiLogicDeliveryMan(BatoiLogicLorry batoiLogicLorry, String name, String nickname, String password, String email, Integer phone, Date createDate, Date writeDate, Set batoiLogicLocations, Set batoiLogicRoutes) {
        this.batoiLogicLorry = batoiLogicLorry;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.createDate = createDate;
        this.writeDate = writeDate;
        this.batoiLogicLocations = batoiLogicLocations;
        this.batoiLogicRoutes = batoiLogicRoutes;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BatoiLogicLorry getBatoiLogicLorry() {
        return this.batoiLogicLorry;
    }

    public void setBatoiLogicLorry(BatoiLogicLorry batoiLogicLorry) {
        this.batoiLogicLorry = batoiLogicLorry;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return this.phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getCreateUid() {
        return this.createUid;
    }

    public void setCreateUid(Integer createUid) {
        this.createUid = createUid;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getWriteUid() {
        return this.writeUid;
    }

    public void setWriteUid(Integer writeUid) {
        this.writeUid = writeUid;
    }

    public Date getWriteDate() {
        return this.writeDate;
    }

    public void setWriteDate(Date writeDate) {
        this.writeDate = writeDate;
    }

    public Set getBatoiLogicLocations() {
        return this.batoiLogicLocations;
    }

    public void setBatoiLogicLocations(Set batoiLogicLocations) {
        this.batoiLogicLocations = batoiLogicLocations;
    }

    public Set getBatoiLogicRoutes() {
        return this.batoiLogicRoutes;
    }

    public void setBatoiLogicRoutes(Set batoiLogicRoutes) {
        this.batoiLogicRoutes = batoiLogicRoutes;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.batoiLogicLorry.getLicensePlate() + " " + this.batoiLogicLorry.getCurrentLoad() + "/" + this.batoiLogicLorry.getCapacity();
    }
}