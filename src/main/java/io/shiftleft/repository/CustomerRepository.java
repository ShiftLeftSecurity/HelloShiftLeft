package io.shiftleft.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.shiftleft.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	public List<Customer> findByFirstName(String firstName);
}
