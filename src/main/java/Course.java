public class Course {
    private final String name;
    private final String[] themes;
    private final String[][] tasks;
    private int totalMaxScore;
    private final int[] themesMaxScores;
    private final int[][] tasksMaxScores;

    public String getName() {
        return name;
    }
    public String[] getThemes() {
        return themes;
    }
    public String[][] getTasks() {
        return tasks;
    }
    public int[][] getTasksMaxScores() {
        return tasksMaxScores;
    }

    public Course(String name, String[] themes, String[][] tasks, int totalMaxScore, int[] themesMaxScores, int[][] tasksMaxScores) {
        this.name = name;
        this.themes = themes;
        this.tasks = tasks;
        this.totalMaxScore = totalMaxScore;
        this.themesMaxScores = themesMaxScores;
        this.tasksMaxScores = tasksMaxScores;
    }
}
