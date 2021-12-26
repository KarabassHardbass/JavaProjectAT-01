import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseController {
    Scanner scanner = new Scanner(System.in);
    Connection connection;
    Statement statement;
    Manager manager;

    public void executeCommand(String command) throws SQLException {
        statement.execute(command);
    }

    public void AddStudents() throws SQLException {
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS students (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	surname text NOT NULL,\n"
                + "	score integer NOT NULL,\n"
                + "	vkId integer NULL\n"
                + ");");
        var tasksNames = manager.getCourse().getTasks();
        var tasksList = new ArrayList<String>();
        var commandLine = "CREATE TABLE IF NOT EXISTS results (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	Всего integer NOT NULL";
        var studentsCount = 0;
        for (var tasks : tasksNames) {
            for (var task : tasks) {
                studentsCount++;
                var taskFixed = task
                        .replace("ДЗ: ", "ДЗ" + studentsCount + "_")
                        .replace(".", "_")
                        .replace(" ", "_");
                commandLine += ",\n	" + taskFixed + " integer NOT NULL";
                tasksList.add(taskFixed);
            }
        }
        commandLine += "\n);";
        statement.execute(commandLine);
        statement.execute("CREATE TABLE IF NOT EXISTS vkInfo (\n"
                + "	vkId integer PRIMARY KEY,\n"
                + "	phone text NOT NULL,\n"
                + "	city text NOT NULL,\n"
                + "	gender text NOT NULL,\n"
                + "	birthDate text NOT NULL,\n"
                + "	photoLink text NOT NULL\n"
                + ");");
        var students = manager.getStudents();
        for (var student : students) {
            var command = "INSERT or REPLACE INTO students(id, name, surname, score, vkId) VALUES(?, ?, ?, ?, ?)";
            var commandRes = "INSERT or REPLACE INTO results(id, Всего, "
                    + String.join(", ", tasksList) + ") VALUES(?, ?"
                    + ", ?".repeat(tasksList.size()) + ")";
            var commandVk = "INSERT or REPLACE INTO vkInfo(vkId, phone, city, gender, birthDate, photoLink) VALUES(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(command);
                 PreparedStatement preparedStatementRes = connection.prepareStatement(commandRes);
                 PreparedStatement preparedStatementVk = connection.prepareStatement(commandVk)) {
                preparedStatement.setInt(1, student.getID());
                preparedStatement.setString(2, student.getName());
                preparedStatement.setString(3, student.getSurname());
                preparedStatement.setInt(4, student.getTotalScore(0));

                var count = 2;
                var studentTasks = student.getScores(0);
                preparedStatementRes.setInt(1, student.getID());
                preparedStatementRes.setInt(2, student.getTotalScore(0));
                for (var i = 0; i < studentTasks.length; i++) {
                    for (var j = 0; j < studentTasks[i].length; j++) {
                        count++;
                        preparedStatementRes.setInt(count, student.getScoreByTaskIndex(0, i, j));
                    }
                }
                preparedStatementRes.executeUpdate();

                if (student.getVkInfo().getID() != 0) {
                    preparedStatement.setLong(5, student.getVkInfo().getID());
                    preparedStatementVk.setLong(1, student.getVkInfo().getID());
                    preparedStatementVk.setString(2, student.getVkInfo().getPhone());
                    preparedStatementVk.setString(3, student.getVkInfo().getCity());
                    preparedStatementVk.setString(4, student.getVkInfo().getGender());
                    preparedStatementVk.setString(5, student.getVkInfo().getBirthDate());
                    preparedStatementVk.setString(6, student.getVkInfo().getPhotoLink());
                    preparedStatementVk.executeUpdate();
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void performCommands() throws SQLException {
        while (true) {
            System.out.println("Введите команду (help для помощи):");
            var command = scanner.nextLine().split(" ");
            if (command[0].equals("AddStudents")) {
                AddStudents();
                System.out.println("students.db сохранена в папке проекта.");
            }
            if (command[0].equals("Execute")) {
                executeCommand(command[1]);
            }
            if (command[0].equals("help")) {
                System.out.println("AddStudents - добавить информацию о студентах.");
                System.out.println("Execute <команда> - выполнить SQL запрос.");
                System.out.println("Stop - завершить выполнение программы.");
            }
            if (command[0].equals("Stop")) {
                System.out.println("Выполнение программы принудительно остановлено.");
                break;
            }
        }
    }

    public DataBaseController(Manager manager) throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:students.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        this.manager = manager;
    }
}
