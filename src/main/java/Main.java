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
                "ТОКЕН Я НЕ ДАМ",
                manager);
        controller.performCommands();
    }
}
