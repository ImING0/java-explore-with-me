package ru.practicum.ewm.service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
