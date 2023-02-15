package udemy.camunda;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class CallNotificationProcess implements JavaDelegate {

  @Override
  public void execute(DelegateExecution ctx) throws Exception {

    Map <String, Object> hm = new HashMap<String, Object>();
    hm.put("message", ctx.getVariable("message"));
    System.out.println("message = " + ctx.getVariable("message"));

    RuntimeService runtimeService = ctx.getProcessEngineServices().getRuntimeService();
    runtimeService.startProcessInstanceByMessage("mess_notify", hm);
    
  }
}