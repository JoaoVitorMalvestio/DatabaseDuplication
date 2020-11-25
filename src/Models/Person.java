package Models;

public class Person {

    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String numberAddress;
    private String zipCode;
    private String neighborhood;
    private String city;
    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumberAddress() {
        return numberAddress;
    }

    public void setNumberAddress(String numberAddress) {
        this.numberAddress = numberAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String encodePerson(){
        String string = "";
        if(id != null){
            string += ("id="+id.toString());
        }
        if(name != null){
            System.out.println(string);
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("name="+name);
        }
        if(phone != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("phone="+phone);
        }
        if(address != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("address="+address);
        }
        if(numberAddress != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("numberAddress="+numberAddress);
        }
        if(zipCode != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("zipCode="+zipCode);
        }
        if(neighborhood != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("neighborhood="+neighborhood);
        }
        if(city != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("city="+city);
        }
        if(state != null){
            if(this.needPutSeparator(string)){
                string += "$";
            }
            string += ("state="+state);
        }
        return string;
    }
    private boolean needPutSeparator(String string){
        return string.length() > 0 && string.charAt(string.length()-1) != '$';
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", numberAddress='" + numberAddress + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
