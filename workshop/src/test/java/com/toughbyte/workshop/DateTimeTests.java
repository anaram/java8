package com.toughbyte.workshop;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeTests {

    private static final Logger LOG = LoggerFactory
            .getLogger(DateTimeTests.class);

    @Rule
    public DocumentationRule documentationRule = new DocumentationRule();

    @Test
    public void year() {
        // tag::year[]
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy");
        Year year = parser.parse("2016", Year::from);
        LOG.info("year: " + year);
        // end::year[]
    }

    @Test
    public void yearMonth() {
        // tag::yearMonth[]
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = parser.parse("2016-06", YearMonth::from);
        LOG.info("yearMonth: " + yearMonth);
        // tag::yearMonth[]
    }

    @Test
    public void localDate() {
        // tag::localDate[]
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = parser.parse("2016-06-03", LocalDate::from);
        LOG.info("localDate: " + localDate);
        // tag::localDate[]
    }

    @Test
    public void localTime() {
        // tag::localTime[]
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = parser.parse("17:43:22", LocalTime::from);
        LOG.info("localTime: " + localTime);
        // tag::localTime[]
    }

    @Test
    public void localDateTime() {
        // tag::localDateTime[]
        DateTimeFormatter parser = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = parser.parse("2016-06-03T17:43:22",
                LocalDateTime::from);
        LOG.info("localDateTime: " + localDateTime);
        LOG.info("year: " + Year.from(localDateTime));
        LOG.info("localTime: " + LocalTime.from(localDateTime));
        // tag::localDateTime[]
    }

    @Test(expected = DateTimeException.class)
    public void localDateTimeToInstantFails() {
        // tag::localDateTimeToInstantFails[]
        DateTimeFormatter parser = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = parser.parse("2016-06-03T17:43:22",
                LocalDateTime::from);
        try {
            Instant.from(localDateTime);
        } catch (DateTimeException e) {
            LOG.error(e.toString());
            throw e;
        }
        // tag::localDateTimeToInstantFails[]
    }

    @Test
    public void localDateTimeToInstantSuccess() {
        // tag::localDateTimeToInstantSuccess[]
        DateTimeFormatter parser = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = parser.parse("2016-06-03T17:43:22",
                LocalDateTime::from);
        LOG.info("Zoned: " + localDateTime.atZone(ZoneId.of("EET")));
        LOG.info("Instant: "
                + localDateTime.atZone(ZoneId.of("EET")).toInstant());
        LOG.info("epochMillis: " + localDateTime.atZone(ZoneId.of("EET"))
                .toInstant().toEpochMilli());
        // tag::localDateTimeToInstantSuccess[]
    }

    @Test
    public void helsinki1917() {
        DateTimeFormatter parser = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // tag::helsinki1917[]
        ZoneId zone = ZoneId.of("Europe/Helsinki");
        ZonedDateTime when = parser.withZone(zone).parse("1917-12-06T12:00:00",
                ZonedDateTime::from);
        LOG.info("When: " + when);
        LOG.info("When: " + when.withZoneSameInstant(ZoneId.of("GMT")));
        // tag::helsinki1917[]
    }

    @Test
    public void helsinki2017() {
        DateTimeFormatter parser = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // tag::helsinki2017[]
        ZoneId zone = ZoneId.of("Europe/Helsinki");
        ZonedDateTime when = parser.withZone(zone).parse("2017-12-06T12:00:00",
                ZonedDateTime::from);
        LOG.info("When: " + when);
        LOG.info("When: " + when.withZoneSameInstant(ZoneId.of("GMT")));
        // tag::helsinki2017[]
    }

    @Test
    public void adjusting() {
        DateTimeFormatter parser = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // tag::adjusting[]
        ZonedDateTime when = parser.withZone(ZoneId.of("GMT"))
                .parse("2017-12-06T12:00:00", ZonedDateTime::from);
        LOG.info("Adjusted: " + when.plus(-4, ChronoUnit.HOURS));
        LOG.info("Original: " + when);
        LOG.info("Reyear: " + Year.of(1917).adjustInto(
                when.withZoneSameLocal(ZoneId.of("Europe/Helsinki"))));
        // tag::adjusting[]
    }

    @Test
    public void lenient() {
        // tag::lenient[]
        DateTimeFormatter lenient = DateTimeFormatter
                .ofPattern("yyyy[-MM[-dd['T'HH[:mm[:ss[.SSS]]]]]][ ][Z]")
                .withZone(ZoneId.of("GMT"));
        lenient.parse("2017");
        // end::lenient[]
    }
}
