package udemy.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class NotificationService implements JavaDelegate {

  @Override
  public void execute(DelegateExecution ctx) throws Exception {

    System.out.println("Notification : " + ctx.getVariable("message"));

  }
}