package ru.practicum.ewm.event.Util;

import ru.practicum.ewm.event.model.EventState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EventStateConverter implements AttributeConverter<EventState, String> {

    @Override
    public String convertToDatabaseColumn(EventState attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EventState convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EventState.valueOf(dbData);
    }
}