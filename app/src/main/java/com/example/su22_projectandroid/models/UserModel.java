package com.example.su22_projectandroid.models;

public class UserModel {
    String ID,username,phone,email,password, address, image;
    boolean isReceptionist;

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return phone;
    }
    public void setUserphone(String phone) {
        this.phone = phone;
    }

    public String getUseremail() {
        return email;
    }
    public void setUseremail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {this.address = address;}

    public String getImage() {
        return image;
    }
    public void setImage(String image) {this.image = image;}

    public boolean getIsReceptionist() {
        return isReceptionist;
    }
    public void setIsReceptionist(boolean isReceptionist) {this.isReceptionist = isReceptionist;

    }

}
