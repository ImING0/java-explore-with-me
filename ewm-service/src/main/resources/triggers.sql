-- это триггер для обновления количества подтвержденных заявок в событии
CREATE OR REPLACE FUNCTION update_confirmed_requests()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE events
    SET confirmed_requests = (SELECT COUNT(*)
                              FROM requests
                              WHERE event_id = NEW.event_id AND status = 'CONFIRMED')
    WHERE id = NEW.event_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_confirmed_requests_after_insert
    AFTER INSERT OR UPDATE OR DELETE ON requests
    FOR EACH ROW
EXECUTE FUNCTION update_confirmed_requests();