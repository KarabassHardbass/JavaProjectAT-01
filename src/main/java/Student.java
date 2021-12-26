import java.util.*;

public class Student extends User {
    private String group;
    private List<Course> courses = new ArrayList<>();
    private List<int[]> themeScores = new ArrayList<>();
    private List<int[][]> scores = new ArrayList<>();
    private List<Integer> totalScore = new ArrayList<>();

    public void updateScore(int courseIndex, int themeIndex, int taskIndex, int score) {
        totalScore.set(courseIndex, totalScore.get(courseIndex) + score - scores.get(courseIndex)[themeIndex][taskIndex]);
        themeScores.get(courseIndex)[themeIndex] += score - scores.get(courseIndex)[themeIndex][taskIndex];
        scores.get(courseIndex)[themeIndex][taskIndex] = score;
    }
    public int[][] getScores(int courseIndex) {
        return scores.get(courseIndex);
    }
    public int[] getScoresByThemeIndex(int courseIndex, int index) {
        return scores.get(courseIndex)[index];
    }
    public int getScoreByTaskIndex(int courseIndex, int themeIndex, int taskIndex) {
        return scores.get(courseIndex)[themeIndex][taskIndex];
    }
    public int getTotalScore(int courseIndex) {
        return totalScore.get(courseIndex);
    }
    public void printTotalScore(int courseIndex) {
        System.out.println("Всего баллов: " + totalScore.get(courseIndex));
    }
    public void printTasksScores(int courseIndex, int index) {
        for (int i = 0; i < scores.get(courseIndex)[index].length; i++) {
            System.out.print(courses.get(courseIndex).getTasks()[index][i]);
            System.out.println(" " + scores.get(courseIndex)[index][i]);
        }
    }
    public void printThemesScores(int courseIndex, int index) {
        System.out.print(courses.get(courseIndex).getThemes()[index]);
        System.out.println(" " + themeScores.get(courseIndex)[index]);
    }
    public Student(int courseIndex, int id, String name, String surname, String group, Course course, int totalScore, int[] themeScores, int[][] scores) {
        super(id, name, surname);
        this.group = group;
        this.courses.add(course);
        this.scores.add(scores);
        this.themeScores.add(themeScores);
        this.totalScore.add(totalScore);
    }
}
