package ru.practicum.ewm.event.service.admin;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventAdminReqDtoIn;
import ru.practicum.ewm.event.dto.EventFullDtoOut;

@Service
public interface IEventAdminService {
    EventFullDtoOut update(EventAdminReqDtoIn eventAdminReqDtoIn,
                           Long eventId);
}
