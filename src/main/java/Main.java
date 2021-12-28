import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClientException, ApiException, IOException, SQLException {
        Manager manager = new Manager();
        VkController controller = new VkController(
                138281156,
                "0c3eb5d3d6ac150d3f5b314cf4ac6aac80ba7d0aac99c5bd1abecff6ca465e327c460e33e543f1031b535",
                manager);
        controller.performCommands();
    }
}
