package utilities;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDateTime unmarshal(final String s) throws Exception {
        try {
            // try to remove the zone
            final String alteredS = StringUtils.remove(s, "+00:00");
            return LocalDateTime.parse(alteredS);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(s).atTime(0, 0);
        }
    }

    @Override
    public String marshal(final LocalDateTime localDateTime) throws Exception {
        return localDateTime.format(DATE_TIME_FORMAT);
    }
}
