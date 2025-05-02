package com.trimbleCars.repository;

import com.trimbleCars.model.Lease;
import com.trimbleCars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {

    List<Lease> findByCustomer(User user);

    long countByCustomerAndEndDateIsNull(User user);

}
