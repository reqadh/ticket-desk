package com.example.ticketdesk.address;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("""
            select a from Address a
            where lower(a.district) like lower(concat('%', :q, '%'))
               or lower(a.buildingNumber) like lower(concat('%', :q, '%'))
               or lower(a.shortCode) like lower(concat('%', :q, '%'))
            order by a.shortCode
            """)
    List<Address> search(@Param("q") String query, Pageable page);
}
