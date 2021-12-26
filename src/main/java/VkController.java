import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.Fields;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.json.*;

import javax.swing.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.io.IOException;
import java.util.*;

import java.util.Scanner;

public class VkController {
    private Scanner scanner = new Scanner(System.in);
    private int id;
    private String token;
    private Manager manager;
    private boolean managerOpened = false;
    private List<VkInfo> users = new ArrayList<>();
    private DataBaseController dataBaseController;
    private ChartController chartController;

    public VkController(int id, String token, Manager manager) throws ClientException, ApiException, SQLException {
        this.id = id;
        this.token = token;
        this.manager = manager;
        chartController = new ChartController(manager);
        dataBaseController = new DataBaseController(manager);
    }

    public void addUsers() throws ClientException {
        var transportClient = new HttpTransportClient();
        var vkApiClient = new VkApiClient(transportClient);
        var userActor = new UserActor(id, token);
        var fields = new Fields[] { Fields.ABOUT, Fields.CONTACTS, Fields.CITY, Fields.PHOTO_MAX_ORIG, Fields.BDATE, Fields.SEX };
        var content = vkApiClient.groups().getMembers(userActor).groupId("198188261").fields(fields).executeAsString();
        System.out.println(content);
        parseInfo(content);
    }

    public void parseInfo(String data) {
        var jsonObject = new JSONObject(data).getJSONObject("response").getJSONArray("items");
        for (int i = 0; i < jsonObject.length(); i++) {
            users.add(new VkInfo(jsonObject.getJSONObject(i)));
        }
    }

    public void openManager() throws IOException {
        if (!managerOpened) {
            manager.parseFromFile();
            managerOpened = true;
        }
        manager.performCommands();
    }

    public void printUser(int id) {
        System.out.println(users.get(id).toString());
    }

    public void addInfo() {
        var studentsCount = manager.getStudents().size();
        for (var currentUser : users) {
            for (var i = 0; i < studentsCount; i++) {
                if (manager.getStudent(i).getName().equals(currentUser.getName()) && manager.getStudent(i).getSurname().equals(currentUser.getSurname())) {
                    manager.applyVkInfo(i, currentUser);
                    break;
                }
            }
        }
    }

    public void performCommands() throws ClientException, ApiException, IOException, SQLException {
        while (true) {
            System.out.println("Введите команду (help для помощи):");
            String[] input = scanner.nextLine().split(" ");
            if (input[0].equals("Stop")) {
                System.out.println("Выполнение программы принудительно остановлено.");
                break;
            }
            if (input[0].equals("AddUsers")) {
                addUsers();
            }
            if (input[0].equals("OpenManager")) {
                System.out.println("Сейчас используется CSV Parser.");
                openManager();
                System.out.println("Сейчас используется Vk Parser.");
            }
            if (input[0].equals("OpenDataBase")) {
                System.out.println("Сейчас используется база данных.");
                dataBaseController.performCommands();
                System.out.println("Сейчас используется Vk Parser.");
            }
            if (input[0].equals("PrintUser")) {
                printUser(Integer.parseInt(input[1]));
            }
            if (input[0].equals("AddInfo")) {
                addInfo();
            }
            if (input[0].equals("CitiesStats")) {
                chartController.drawCitiesChart();
                System.out.println("График сохранён как cities.png в папке проекта.");
            }
            if (input[0].equals("GendersStats")) {
                chartController.drawGendersChart();
                System.out.println("График сохранён как genders.png в папке проекта.");
            }
            if (input[0].equals("AgesStats")) {
                chartController.drawAgesChart();
                System.out.println("График сохранён как ages.png в папке проекта.");
            }
            if (input[0].equals("ScoresStats")) {
                chartController.drawScoresChart();
                System.out.println("График сохранён как scores.png в папке проекта.");
            }
            if (input[0].equals("help")) {
                System.out.println("Stop - остановить выполнение программы.");
                System.out.println("AddUsers - добавить пользователей группы.");
                System.out.println("OpenManager - открыть CSV Parser.");
                System.out.println("OpenDataBase - открыть базу данных.");
                System.out.println("AddInfo - добавить информацию пользователям.");
                System.out.println("PrintUser <id пользователя в группе> - вывести информацию о пользователе группы.");
                System.out.println("CitiesStats - вывести статистику по городам.");
                System.out.println("GendersStats - вывести статистику по полам.");
                System.out.println("AgesStats - вывести статистику по возрастам.");
                System.out.println("ScoresStats - вывести статистику по выполненным заданиям.");
            }
        }
    }
}
