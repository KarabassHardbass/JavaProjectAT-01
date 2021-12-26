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
                "3318106d6647534c9d3380804ddc937784a1ac4b1ae67ec317f059a7632cef3769872700b25a6f889ce16",
                manager);
        controller.performCommands();
    }
}
