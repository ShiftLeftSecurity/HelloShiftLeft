package io.shiftleft.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import io.shiftleft.model.Patient;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

import io.shiftleft.model.Address;
import io.shiftleft.model.Customer;
import io.shiftleft.model.Account;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class DataBuilder {

  public List<Customer> createCustomers() {

    try {
      // Simulate file access by creating temp file
      // create a temp file
      File temp = File.createTempFile("tempfile", ".tmp");
      // write it
      BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
      bw.write("This is the temporary file content");
      bw.close();
      System.out.println(" File Write Successful ");
    } catch (IOException e) {

      e.printStackTrace();

    }

    try {

      String output = new ProcessExecutor().command("java", "-version")
          .redirectOutput(Slf4jStream.of(getClass()).asInfo()).readOutput(true).execute().outputUTF8();

      System.out.println(" Output of System Call is " + output);
    } catch (InvalidExitValueException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (TimeoutException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Set<Account> accounts1 = new HashSet<Account>();
    accounts1.add(new Account(1111, 321045, "CHECKING", 10000, 10));
    accounts1.add(new Account(1112, 321045, "SAVING", 100000, 20));
    Customer customer1 = new Customer("ID-4242", 4242, "Joe", "Smith", DateTime.parse("1982-01-10").toDate(),
            "123-45-3456", "000111222", "981-110-0101", "408-123-1233", new Address("High Street", "", "Santa Clara",
            "CA", "95054"), accounts1);

    Set<Account> accounts2 = new HashSet<Account>();
    accounts2.add(new Account(2111, 421045, "CHECKING", 20000, 10));
    accounts2.add(new Account(2112, 421045, "MMA", 200000, 20));
    Customer customer2 = new Customer("ID-4243", 4343, "Paul", "Jones", DateTime.parse("1973-01-03").toDate(),
            "321-67-3456", "222665436", "981-110-0100", "302-767-8796", new Address("Main Street", "", "Sunnyvale",
            "CA", "94086"), accounts2);

    Set<Account> accounts3 = new HashSet<Account>();
    accounts3.add(new Account(3111, 421045, "SAVING", 30000, 10));
    accounts3.add(new Account(3112, 421045, "MMA", 300000, 20));
    Customer customer3 = new Customer("ID-4244", 4244, "Steve", "Toale", DateTime.parse("1979-03-08").toDate(),
            "769-12-9987", "888225436", "981-110-0101", "650-897-2366", new Address("Main Street", "", "Redwood City",
            "CA", "95058"), accounts3);

    return Arrays.asList(customer1, customer2, customer3);
  }

  public List<Patient> createPatients() {
    Patient patient1 = new Patient(42, "Suchakra", "Sharma", DateTime.parse("1970-01-01").toDate(), 42, 42, "linuxol", 42, 42, 42, 42);
    return Arrays.asList(patient1);
  }
}
