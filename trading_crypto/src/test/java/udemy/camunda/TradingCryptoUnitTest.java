package udemy.camunda;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;


/**
 * Test case starting an in-memory database-backed Process Engine.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradingCryptoUnitTest {

  private static final String PROCESS_DEFINITION_KEY = "trading_crypto";

  static {
    LogFactory.useSlf4jLogging(); // MyBatis
  }

  @Rule
  //@ClassRule
  //public static ProcessEngineRule processEngine = new StandaloneInMemoryTestConfiguration().rule();
  public ProcessEngineRule processEngine = TestCoverageProcessEngineRuleBuilder.create().build();
  
  @Before
  public void setup() {
    init(processEngine.getProcessEngine());
  }

  @Deployment(resources = {"trading_crypto.bpmn"})
  @Test
  public void testHappyPath() {
  
    //autoMock("trading_crypto.bpmn");
    Mocks.register("executeOrderService", new ExecuteOrderService());
    Mocks.register("callNotificationProcess", new CallNotificationProcess());
    Mocks.register("notificationService", new NotificationService());

    Map<String, Object> map = new HashMap<>();
    map.put("interestingTradeLimit", "10000");

    ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, map);

    assertThat(processInstance).isStarted();

    assertThat(processInstance).isWaitingAt("get_quote");

    complete(externalTask("get_quote"), withVariables("lastTradePrice", 9000));

    assertThat(processInstance).isWaitingAt("create_order");

    complete(task("create_order"), withVariables("orderAmount", 5000));

    assertThat(processInstance).hasPassed("execute_order");
    
    assertThat(processInstance).hasPassed("execute_order_notif");

    assertThat(processInstance).isEnded();

  }
}