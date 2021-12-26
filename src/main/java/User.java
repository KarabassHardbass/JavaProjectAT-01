public class User {
    private final String name;
    private final String surname;
    private final int id;
    private VkInfo vkInfo;

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public VkInfo getVkInfo() {
        return vkInfo;
    }

    public void setVkInfo(VkInfo vkInfo) {
        this.vkInfo = vkInfo;
    }

    public void printPersonalData() {
        System.out.println("");
        System.out.println(id + " " + name + " " + surname + " Информация из Вконтакте: " + vkInfo.toString());
    }

    public User(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        vkInfo = new VkInfo();
    }
}
