package udemy.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class ExecuteOrderService implements JavaDelegate {

  @Override
  public void execute(DelegateExecution ctx) throws Exception {

    System.out.println("Order executed!");

  }
}