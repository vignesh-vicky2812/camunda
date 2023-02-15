package udemy.camunda;

import static org.camunda.spin.DataFormats.json;
import static org.camunda.spin.Spin.S;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.spin.SpinList;
import org.camunda.spin.json.SpinJsonNode;

public class GetQuote {
    private final static Logger LOGGER = Logger.getLogger(GetQuote.class.getName());

    public static void main(String[] args) {
        ExternalTaskClient client = ExternalTaskClient.create().baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        // subscribe to an external task topic as specified in the process
        // the default lock duration is 20 seconds, but you can override this
        client.subscribe("get-quote").lockDuration(1000).handler((externalTask, externalTaskService) -> {

            Client restClient = ClientBuilder.newClient();
            WebTarget webTarget = restClient.target("https://api.kraken.com/0/public/");

            String t = webTarget.path("Ticker").queryParam("pair", "xbtusd").request()
                    .accept(MediaType.APPLICATION_JSON).get(String.class);

            SpinJsonNode json = S(t, json());
            SpinList<SpinJsonNode> lastTrade = json.prop("result").prop("XXBTZUSD").prop("c").elements();
            SpinJsonNode lastTradePrice = lastTrade.get(0);
            LOGGER.info("lastTradePrice = " + lastTradePrice.stringValue());

            Map<String, Object> hm = new HashMap<String, Object>();
            hm.put("lastTradePrice", Double.parseDouble(lastTradePrice.stringValue()));

            // Complete the task
            externalTaskService.complete(externalTask, hm);
            
        }).open();
    }
}
