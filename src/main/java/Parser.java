import java.io.*;
import java.util.*;

public class Parser {
    private List<String[]> originalData;

    public String[][] getLines(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fileReader);
        List<String[]> lines = new ArrayList<>();
        originalData = new ArrayList<>(lines);
        String line;
        while ((line = reader.readLine()) != null) {
            ArrayList<String> lineFixed = new ArrayList<>(Arrays.asList(line.split(";")));
            lineFixed.removeAll(Arrays.asList(""));
            lines.add(lineFixed.toArray(new String[0]));
        }
        return lines.toArray(new String[0][0]);
    }
    public void saveFile(String fileName, String[][] data) {
        for (int i = 0; i < originalData.size(); i++) {

        }
    }
}
