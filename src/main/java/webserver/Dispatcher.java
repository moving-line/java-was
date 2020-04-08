package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import controller.MainController;
import controller.resource.FontController;
import controller.resource.JavaScriptController;
import controller.resource.StyleSheetController;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private static Map<String, Controller> controllerPool;

    static {
        controllerPool = new HashMap<>();
        controllerPool.put("/css", new StyleSheetController());
        controllerPool.put("/js", new JavaScriptController());
        controllerPool.put("/fonts", new FontController());
        controllerPool.put("/user/create", new CreateUserController());
        controllerPool.put("/user/list", new ListUserController());
        controllerPool.put("/user/login", new LoginController());
        controllerPool.put("/index", new MainController());
    }

    public static void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        for (String dispatchPath : controllerPool.keySet()) {
            if(path.startsWith(dispatchPath)) {
                Controller controller = controllerPool.get(dispatchPath);
                controller.service(request, response);
            }
        }
    }
}
