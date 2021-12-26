import org.json.*;

public class VkInfo {
    private long vkId = 0;
    private String name = "";
    private String surname = "";
    private String phone = "";
    private String city = "";
    private String gender = "";
    private String birthDate = "";
    private String photoLink = "";

    public long getID() {
        return vkId;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPhone() {
        return phone;
    }
    public String getCity() {
        return city;
    }
    public String getGender() {
        return gender;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getPhotoLink() {
        return photoLink;
    }
    public String toString() {
        return "ID: " + vkId + " Номер телефона: " + phone + " Город: " + city + " Пол: " + gender + " Дата рождения: " + birthDate;
    }
    public VkInfo(JSONObject userValues) {
        if (userValues.has("id"))
            vkId = userValues.getInt("id");
        if (userValues.has("first_name"))
            name = userValues.getString("first_name");
        if (userValues.has("last_name"))
            surname = userValues.getString("last_name");
        if (userValues.has("mobile_phone"))
            phone = userValues.getString("mobile_phone");
        if (userValues.has("city"))
            city = userValues.getJSONObject("city").getString("title");
        if (userValues.has("sex")) {
            var genderValue = userValues.getInt("sex");
            if (genderValue == 1)
                gender = "женский";
            if (genderValue == 2)
                gender = "мужской";
        }
        if (userValues.has("bdate"))
            birthDate = userValues.getString("bdate");
        if (userValues.has("photo_max_orig"))
            photoLink = userValues.getString("photo_max_orig");
    }

    public VkInfo() {

    }
}
