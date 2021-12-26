import java.io.IOException;
import java.util.*;

public class Manager {
    private final int courseId = 0;
    private final Parser parser = new Parser();
    private final Scanner scanner = new Scanner(System.in);
    private List<Student> students = new ArrayList<>();
    private Course course;

    public Course getCourse() {
        return course;
    }

    public void createCourse(String[][] rawData) {
        List<String> themesList = new ArrayList<>();
        for (int i = 2; i < rawData[0].length; i++) {
            themesList.add(rawData[0][i]);
        }
        String[] themes = themesList.toArray(new String[0]);
        List<String[]> tasksList = new ArrayList<>();
        List<String> currentTasks = new ArrayList<>();
        for (int i = 4; i < rawData[1].length + 1; i++) {
            if (i == rawData[1].length) {
                tasksList.add(currentTasks.toArray(new String[0]));
            } else {
                if (rawData[1][i].equals("ДЗ")) {
                    tasksList.add(currentTasks.toArray(new String[0]));
                    currentTasks.clear();
                } else {
                    currentTasks.add(rawData[1][i]);
                }
            }
        }
        String[][] tasks = tasksList.toArray(new String[0][0]);
        int[] themesMaxScores = new int[themes.length];
        int[][] tasksMaxScores = new int[themes.length][];
        int currentIndex = 2;
        for (int i = 0; i < themes.length; i++) {
            themesMaxScores[i] = Integer.parseInt(rawData[2][currentIndex]);
            tasksMaxScores[i] = new int[tasks[i].length];
            currentIndex++;
            for (int j = 0; j < tasks[i].length; j++) {
                tasksMaxScores[i][j] = Integer.parseInt(rawData[2][currentIndex]);;
                currentIndex++;
            }
        }
        course = new Course("Основы программирования на Java", themes, tasks, Integer.parseInt(rawData[2][1]), themesMaxScores, tasksMaxScores);
    }
    public void createStudents(String[][] rawData) {
        for (int i = 3; i < rawData.length; i++) {
            String[] nameSurname = rawData[i][0].split(" ");
            String name = nameSurname[0];
            String surname = "";
            if (nameSurname.length == 2) {
                surname = nameSurname[1];
            }
            if (nameSurname.length == 3) {
                surname = nameSurname[1] + " " + nameSurname[2];
            }
            if (nameSurname.length == 4) {
                surname = nameSurname[1] + " " + nameSurname[2] + " " + nameSurname[3];
            }
            int[] themesScores = new int[course.getThemes().length];
            int[][] tasksScores = new int[course.getThemes().length][];
            int currentIndex = 3;
            for (int k = 0; k < course.getThemes().length; k++) {
                themesScores[k] = Integer.parseInt(rawData[i][currentIndex]);
                tasksScores[k] = new int[course.getTasks()[k].length];
                currentIndex++;
                for (int j = 0; j < course.getTasks()[k].length; j++) {
                    tasksScores[k][j] = Integer.parseInt(rawData[i][currentIndex]);
                    currentIndex++;
                }
            }
            students.add(new Student(courseId, i - 3, surname, name, rawData[i][1], course, Integer.parseInt(rawData[i][2]), themesScores, tasksScores));
        }
    }
    public void parseFromFile() throws IOException {
        System.out.println("Введите название файла:");
        String[][] rawData = parser.getLines(scanner.nextLine());
        createCourse(rawData);
        createStudents(rawData);
    }
    public void printStudents() {
        for (int i = 0; i < students.size(); i++) {
            printStudent(i);
        }
    }
    public void printStudent(int index) {
        Student student = students.get(index);
        student.printPersonalData();
        student.printTotalScore(courseId);
        for (var i = 0; i < course.getThemes().length; i++) {
            student.printThemesScores(courseId, i);
            student.printTasksScores(courseId, i);
        }
    }
    public Student getStudent(int id) {
        for (Student student : students) {
            if (student.getID() == id) {
                return student;
            }
        }
        return null;
    }
    public List<Student> getStudents() {
        return students;
    }
    public void printToFile(String fileName) {
        // W.I.P.
    }
    public void applyVkInfo(int id, VkInfo vkInfo) {
        students.get(id).setVkInfo(vkInfo);
    }
    public void performCommands() {
        System.out.println("Введие help для помощи.");
        while (true) {
            String[] command = scanner.nextLine().split(" ");
            if (command[0].equals("PrintStudents")) {
                printStudents();
            }
            if (command[0].equals("PrintStudent")) {
                printStudent(Integer.parseInt(command[1]));
            }
            if (command[0].equals("Update")) {
                if (Integer.parseInt(command[4]) <= course.getTasksMaxScores()[Integer.parseInt(command[2])][Integer.parseInt(command[3])]) {
                    students.get(Integer.parseInt(command[1])).updateScore(courseId, Integer.parseInt(command[2]), Integer.parseInt(command[3]), Integer.parseInt(command[4]));
                } else {
                    System.out.println("Введена слишком большая величина баллов.");
                }
            }
            if (command[0].equals("Save")) {
                System.out.println("Файл сохранён! (понарошку)");
            }
            if (command[0].equals("help")) {
                System.out.println("PrintStudents - вывести информацию о всех студентах.");
                System.out.println("PrintStudent <id студента> - вывести информацию о студенте.");
                System.out.println("Update <id студента> <номер темы> <номер задания> <кол-во баллов> - изменить число баллов.");
                System.out.println("[WIP] Save <название файла> - сохранить данные в файл.");
                System.out.println("Stop - остановить выполнение программы.");
            }
            if (command[0].equals("Stop")) {
                System.out.println("Работа программы принудительно остановлена.");
                break;
            }
        }
    }
}
