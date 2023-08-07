package com.service.EventMicroservice.repository;

import com.service.EventMicroservice.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findAllByLocation(String location);

    @Query(value = "SELECT e.id, e.name, e.description, e.user_id, e.location, e.date, e.duration FROM events e WHERE (:location IS NULL OR e.location = :location)"
            + " AND (CAST(:startDate AS date) IS NULL OR e.date >= :startDate)"
            + " AND (CAST(:endDate AS date) IS NULL OR e.date <= :endDate)", nativeQuery = true)
    Page<Event> findAllFiltered(@Param("location") String location,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                PageRequest pageRequest);

}
