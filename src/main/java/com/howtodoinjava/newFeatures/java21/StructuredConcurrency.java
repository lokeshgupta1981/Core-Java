package com.howtodoinjava.newFeatures.java21;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class StructuredConcurrency {

  public static void main(String[] args) {
    StructuredConcurrency instance = new StructuredConcurrency();
    DemographicData data = instance.getClientDetails(1001L);
    //Response response = instance.getClientById(1001L);
  }

  public DemographicData getClientDetails(Long id) {

    System.out.println("Forking new threads...");

    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<>()) {

      scope.fork(() -> fetchUserDetails(id));
      scope.fork(() -> fetchUserDetailsNew(id));

      scope.join();

      System.out.println("Response is received from a worker...");
      //Any one task has been completed
      return (DemographicData) scope.result();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Response getClientById(Long id) {

    System.out.println("Forking new threads...");

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

      Supplier<AccountDetails> accountDetailsFuture = scope.fork(() -> getAccountDetails(id));
      Supplier<LinkedAccounts> linkedAccountsFuture = scope.fork(() -> fetchLinkedAccounts(id));
      Supplier<DemographicData> userDetailsFuture = scope.fork(() -> fetchUserDetails(id));

      System.out.println("Joining all threads...");
      scope.join();  // Join all subtasks
      scope.throwIfFailed(WebApplicationException::new);  //Handle error when any subtask fails

      System.out.println("Response is received from all workers...");
      //The subtasks have completed by now so process the result
      return new Response(accountDetailsFuture.get(),
          linkedAccountsFuture.get(),
          userDetailsFuture.get());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private DemographicData fetchUserDetails(Long id) throws InterruptedException {
    Thread.sleep(2000L);
    System.out.println("Retrieved DemographicData.");
    return new DemographicData();
  }

  private DemographicData fetchUserDetailsNew(Long id) throws InterruptedException {
    Thread.sleep(1000L);
    System.out.println("Retrieved DemographicData from fetchUserDetailsNew.");
    return new DemographicData();
  }

  private LinkedAccounts fetchLinkedAccounts(Long id) throws InterruptedException {
    Thread.sleep(3000L);
    System.out.println("Retrieved LinkedAccounts.");
    return new LinkedAccounts();
  }

  private AccountDetails getAccountDetails(Long id) throws InterruptedException {
    Thread.sleep(4000L);
    System.out.println("Retrieved AccountDetails.");
    return new AccountDetails();
  }
}

class AccountDetails {

}

class LinkedAccounts {

}

class DemographicData {

}

class Response {

  public Response(AccountDetails accountDetails, LinkedAccounts linkedAccounts,
      DemographicData demographicData) {
  }
}

class WebApplicationException extends RuntimeException {

  public WebApplicationException(Throwable e) {
    super(e);
  }
}
