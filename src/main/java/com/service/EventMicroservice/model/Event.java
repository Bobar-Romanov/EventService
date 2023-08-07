package com.service.EventMicroservice.model;

import com.service.EventMicroservice.utils.DurationConverter;
import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    @Size(max = 100)
    private String name;

    @Column(name = "description")
    @NotBlank
    @Size(max = 255)
    private String description;

    @Column(name = "user_id")
    @Positive
    private Long userId;

    @Column(name = "location")
    @NotBlank
    @Size(max = 255)
    private String location;

    @Column(name = "date")
    @FutureOrPresent
    private LocalDateTime date;

    @Column(name = "duration",  columnDefinition = "varchar")
    @Convert(converter = DurationConverter.class)
    private Duration duration;

}
