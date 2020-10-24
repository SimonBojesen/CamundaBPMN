import org.camunda.bpm.client.ExternalTaskClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
public class ConsumerFindsBook {
    private final static Logger LOGGER = Logger.getLogger(ConsumerFindsBook.class.getName());

    public static void main(String[] args)
    {
        ExternalTaskClient consumer = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();
        LOGGER.info("Not yet requests");

        // subscribe to an external task topic as specified in the process
        consumer.subscribe("find-books")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here
                    System.out.println("Test of External Service: Pay");
                    // Get a process variable
                    String query = (String) externalTask.getVariable("searchQuery");
                    LOGGER.info("found books with result: " + query);
                    //Map<String, Object> map = new HashMap();
                    //map.put("result", false);
                    List<String> list = new ArrayList<>();
                    list.add("hej");
                    list.add("123");
                    VariableMap map = Variables.putValue("result", true).putValue("list", list);
                    // Complete the task
                    externalTaskService.complete(externalTask, map);
                })
                .open();

        consumer.subscribe("find-details")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here
                    System.out.println("Test of External Service: Pay");
                    // Get a process variable
                    String book = (String) externalTask.getVariable("book");
                    LOGGER.info("showing details of chosen book " + book);
                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
