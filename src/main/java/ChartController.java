import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class ChartController {
    private final Manager manager;

    public void drawCitiesChart() throws IOException {
        var chartData = new DefaultPieDataset();
        var neededStudents = manager.getStudents();
        var cities = new HashMap<String, Integer>();
        for (var student : neededStudents) {
            var city = student.getVkInfo().getCity();
            if (city.equals(""))
                continue;
            if (!cities.containsKey(city))
                cities.put(city, 1);
            else
                cities.put(city, cities.get(city) + 1);
        }
        for (var city : cities.keySet()) {
            chartData.setValue(city, cities.get(city));
        }
        var chart = ChartFactory.createPieChart("Статистика по городам", chartData, false, true, false);
        ChartUtils.saveChartAsPNG(new File("cities.png"), chart, 512, 512);
    }

    public void drawGendersChart() throws IOException {
        var chartData = new DefaultPieDataset();
        var neededStudents = manager.getStudents();
        var genders = new HashMap<String, Integer>();
        for (var student : neededStudents) {
            var gender = student.getVkInfo().getGender();
            if (gender.equals(""))
                continue;
            if (!genders.containsKey(gender))
                genders.put(gender, 1);
            else
                genders.put(gender, genders.get(gender) + 1);
        }
        for (var gender : genders.keySet()) {
            chartData.setValue(gender, genders.get(gender));
        }
        var chart = ChartFactory.createPieChart("Статистика по полу", chartData, false, true, false);
        ChartUtils.saveChartAsPNG(new File("genders.png"), chart, 512, 512);
    }

    public void drawAgesChart() throws IOException {
        var chartData = new DefaultPieDataset();
        var neededStudents = manager.getStudents();
        var ages = new HashMap<Integer, Integer>();
        for (var student : neededStudents) {
            if (student.getVkInfo().getBirthDate().equals("") || student.getVkInfo().getBirthDate().split("\\.").length < 3)
                continue;
            var age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(student.getVkInfo().getBirthDate().split("\\.")[2]);
            if (!ages.containsKey(age))
                ages.put(age, 1);
            else
                ages.put(age, ages.get(age) + 1);
        }
        for (var age : ages.keySet()) {
            chartData.setValue(age, ages.get(age));
        }
        var chart = ChartFactory.createPieChart("Статистика по возрасту", chartData, false, true, false);
        ChartUtils.saveChartAsPNG(new File("ages.png"), chart, 512, 512);
    }

    public void drawScoresChart() throws IOException {
        var chartData = new DefaultCategoryDataset();
        var neededStudents = manager.getStudents();
        var tasksTitles = manager.getCourse().getTasks();
        var tasksMaxScores = manager.getCourse().getTasksMaxScores();
        var tasksScoresBoys = new int[tasksTitles.length][];
        var tasksScoresGirls = new int[tasksTitles.length][];
        var tasksScoresNone = new int[tasksTitles.length][];
        var boysCount = 0;
        var girlsCount = 0;
        var noneCount = 0;
        var allCount = 0;
        for (var student : neededStudents) {
            var currentSet = tasksScoresNone;
            if (student.getVkInfo().getGender() == "мужской") {
                boysCount++;
                currentSet = tasksScoresBoys;
            } else if (student.getVkInfo().getGender() == "женский") {
                girlsCount++;
                currentSet = tasksScoresGirls;
            } else {
                noneCount++;
            }
            for (var i = 0; i < tasksMaxScores.length; i++) {
                if (currentSet[i] == null) {
                    currentSet[i] = new int[tasksMaxScores[i].length];
                }
                for (var j = 0; j < tasksMaxScores[i].length; j++) {
                    currentSet[i][j] = currentSet[i][j] + student.getScoreByTaskIndex(0, i, j);
                }
            }
        }
        var count = 0;
        for (var i = 0; i < tasksMaxScores.length; i++) {
            for (var j = 0; j < tasksMaxScores[i].length; j++) {
                count++;
                chartData.addValue((float)tasksScoresGirls[i][j] / tasksMaxScores[i][j]
                        / girlsCount * 100, "Девочки", Integer.toString(count));
                chartData.addValue((float)tasksScoresBoys[i][j] / tasksMaxScores[i][j]
                        / boysCount * 100, "Мальчики", Integer.toString(count));
                chartData.addValue((float)tasksScoresNone[i][j] / tasksMaxScores[i][j]
                        / noneCount * 100, "Не указано", Integer.toString(count));
            }
        }
        var chart = ChartFactory.createBarChart("Статистика по баллам", "Задания", "Прогресс (в процентах)", chartData);
        ChartUtils.saveChartAsPNG(new File("scores.png"), chart, 4096, 512);
    }

    public ChartController(Manager manager) {
        this.manager = manager;
    }
}
