package redhatamphitheater.events.parser.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for DateParser.
 * Verifies date parsing with multiple format strategies including formats with and without commas.
 */
class DateParserTest {

    private DateParser parser;

    @BeforeEach
    void setUp() {
        parser = new DateParser();
    }

    // Tests for parse() method - Full month name formats with comma

    @Test
    void parse_withFullMonthFormatSingleDigitDayWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("December 5, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 5));
    }

    @Test
    void parse_withFullMonthFormatDoubleDigitDayWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("December 15, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withFullMonthFormatJanuaryWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("January 1, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void parse_withFullMonthFormatFebruaryWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("February 28, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 2, 28));
    }

    // Tests for parse() method - Full month name formats without comma

    @Test
    void parse_withFullMonthFormatSingleDigitDayNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("December 5 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 5));
    }

    @Test
    void parse_withFullMonthFormatDoubleDigitDayNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("December 15 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withFullMonthFormatJanuaryNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("January 1 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void parse_withFullMonthFormatSeptemberNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("September 20 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 9, 20));
    }

    // Tests for parse() method - Abbreviated month formats with comma

    @Test
    void parse_withAbbreviatedMonthFormatSingleDigitDayWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("Dec 5, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 5));
    }

    @Test
    void parse_withAbbreviatedMonthFormatDoubleDigitDayWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("Dec 15, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withAbbreviatedMonthFormatJanWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("Jan 1, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void parse_withAbbreviatedMonthFormatMarWithComma_returnsLocalDate() {
        LocalDate result = parser.parse("Mar 31, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 31));
    }

    // Tests for parse() method - Abbreviated month formats without comma

    @Test
    void parse_withAbbreviatedMonthFormatSingleDigitDayNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("Dec 5 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 5));
    }

    @Test
    void parse_withAbbreviatedMonthFormatDoubleDigitDayNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("Dec 15 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withAbbreviatedMonthFormatJanNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("Jan 1 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void parse_withAbbreviatedMonthFormatSepNoComma_returnsLocalDate() {
        LocalDate result = parser.parse("Sep 20 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 9, 20));
    }

    // Tests for parse() method - Slash formats

    @Test
    void parse_withSlashFormatSingleDigits_returnsLocalDate() {
        LocalDate result = parser.parse("1/5/2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 5));
    }

    @Test
    void parse_withSlashFormatDoubleDigits_returnsLocalDate() {
        LocalDate result = parser.parse("12/15/2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withSlashFormatMixedDigits_returnsLocalDate() {
        LocalDate result = parser.parse("1/15/2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 15));
    }

    @Test
    void parse_withSlashFormatLeapYear_returnsLocalDate() {
        LocalDate result = parser.parse("2/29/2024");

        assertThat(result).isEqualTo(LocalDate.of(2024, 2, 29));
    }

    // Tests for parse() method - ISO and RFC formats

    @Test
    void parse_withIsoFormat_returnsLocalDate() {
        LocalDate result = parser.parse("2025-12-15");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withRfc1123Format_returnsLocalDate() {
        LocalDate result = parser.parse("Mon, 15 Dec 2025 10:00:00 GMT");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withRfc1123FormatDifferentDay_returnsLocalDate() {
        LocalDate result = parser.parse("Wed, 01 Jan 2025 12:30:00 GMT");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    // Tests for parse() method - Null and empty inputs

    @Test
    void parse_withNull_returnsNull() {
        LocalDate result = parser.parse(null);

        assertThat(result).isNull();
    }

    @Test
    void parse_withEmptyString_returnsNull() {
        LocalDate result = parser.parse("");

        assertThat(result).isNull();
    }

    @Test
    void parse_withBlankString_returnsNull() {
        LocalDate result = parser.parse("   ");

        assertThat(result).isNull();
    }

    @Test
    void parse_withTabsAndSpaces_returnsNull() {
        LocalDate result = parser.parse("\t  \t");

        assertThat(result).isNull();
    }

    // Tests for parse() method - Invalid inputs

    @Test
    void parse_withInvalidFormat_returnsNull() {
        LocalDate result = parser.parse("not a date");

        assertThat(result).isNull();
    }

    @Test
    void parse_withPartialDate_returnsNull() {
        LocalDate result = parser.parse("December 2025");

        assertThat(result).isNull();
    }

    @Test
    void parse_withInvalidMonth_returnsNull() {
        LocalDate result = parser.parse("13/15/2025");

        assertThat(result).isNull();
    }

    @Test
    void parse_withTwoDigitYear_returnsNull() {
        LocalDate result = parser.parse("12/15/25");

        assertThat(result).isNull();
    }

    // Tests for parse() method - Whitespace handling

    @Test
    void parse_withLeadingWhitespace_trimsAndParses() {
        LocalDate result = parser.parse("  December 15, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withTrailingWhitespace_trimsAndParses() {
        LocalDate result = parser.parse("December 15, 2025  ");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withSurroundingWhitespace_trimsAndParses() {
        LocalDate result = parser.parse("  December 15, 2025  ");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withTabsAndSpacesAroundDate_trimsAndParses() {
        LocalDate result = parser.parse("\t  December 15, 2025  \t");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withWhitespaceAroundNoCommaFormat_trimsAndParses() {
        LocalDate result = parser.parse("  December 15 2025  ");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    // Tests for parse() method - Edge cases

    @Test
    void parse_withFirstDayOfYear_returnsLocalDate() {
        LocalDate result = parser.parse("January 1, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    void parse_withLastDayOfYear_returnsLocalDate() {
        LocalDate result = parser.parse("December 31, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 12, 31));
    }

    @Test
    void parse_withLeapYearFebruary29_returnsLocalDate() {
        LocalDate result = parser.parse("February 29, 2024");

        assertThat(result).isEqualTo(LocalDate.of(2024, 2, 29));
    }

    // Tests for parse() method - All supported formats verification

    @Test
    void parse_withFullMonthSingleDigitDayCommaFormat_returnsLocalDate() {
        // Format: "MMMM d, yyyy"
        LocalDate result = parser.parse("March 5, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 5));
    }

    @Test
    void parse_withFullMonthSingleDigitDayNoCommaFormat_returnsLocalDate() {
        // Format: "MMMM d yyyy"
        LocalDate result = parser.parse("March 5 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 5));
    }

    @Test
    void parse_withAbbreviatedMonthSingleDigitDayCommaFormat_returnsLocalDate() {
        // Format: "MMM d, yyyy"
        LocalDate result = parser.parse("Mar 5, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 5));
    }

    @Test
    void parse_withAbbreviatedMonthSingleDigitDayNoCommaFormat_returnsLocalDate() {
        // Format: "MMM d yyyy"
        LocalDate result = parser.parse("Mar 5 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 5));
    }

    @Test
    void parse_withFullMonthDoubleDigitDayCommaFormat_returnsLocalDate() {
        // Format: "MMMM dd, yyyy"
        LocalDate result = parser.parse("March 25, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void parse_withFullMonthDoubleDigitDayNoCommaFormat_returnsLocalDate() {
        // Format: "MMMM dd yyyy"
        LocalDate result = parser.parse("March 25 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void parse_withAbbreviatedMonthDoubleDigitDayCommaFormat_returnsLocalDate() {
        // Format: "MMM dd, yyyy"
        LocalDate result = parser.parse("Mar 25, 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void parse_withAbbreviatedMonthDoubleDigitDayNoCommaFormat_returnsLocalDate() {
        // Format: "MMM dd yyyy"
        LocalDate result = parser.parse("Mar 25 2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void parse_withSingleDigitSlashFormat_returnsLocalDate() {
        // Format: "M/d/yyyy"
        LocalDate result = parser.parse("3/5/2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 5));
    }

    @Test
    void parse_withDoubleDigitSlashFormat_returnsLocalDate() {
        // Format: "MM/dd/yyyy"
        LocalDate result = parser.parse("03/25/2025");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void parse_withIsoLocalDateFormat_returnsLocalDate() {
        // Format: ISO_LOCAL_DATE (yyyy-MM-dd)
        LocalDate result = parser.parse("2025-03-25");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void parse_withRfc1123DateTimeFormat_returnsLocalDate() {
        // Format: RFC_1123_DATE_TIME
        LocalDate result = parser.parse("Tue, 25 Mar 2025 14:30:00 GMT");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    // Tests for parse() method - Verify comma vs no-comma format distinction

    @Test
    void parse_withCommaAndNoCommaFormatsForSameDate_returnsSameResult() {
        LocalDate withComma = parser.parse("April 10, 2025");
        LocalDate withoutComma = parser.parse("April 10 2025");

        assertThat(withComma).isEqualTo(withoutComma);
        assertThat(withComma).isEqualTo(LocalDate.of(2025, 4, 10));
    }

    @Test
    void parse_withAllMonthsUsingCommaFormat_returnsCorrectDates() {
        assertThat(parser.parse("January 15, 2025")).isEqualTo(LocalDate.of(2025, 1, 15));
        assertThat(parser.parse("February 15, 2025")).isEqualTo(LocalDate.of(2025, 2, 15));
        assertThat(parser.parse("March 15, 2025")).isEqualTo(LocalDate.of(2025, 3, 15));
        assertThat(parser.parse("April 15, 2025")).isEqualTo(LocalDate.of(2025, 4, 15));
        assertThat(parser.parse("May 15, 2025")).isEqualTo(LocalDate.of(2025, 5, 15));
        assertThat(parser.parse("June 15, 2025")).isEqualTo(LocalDate.of(2025, 6, 15));
        assertThat(parser.parse("July 15, 2025")).isEqualTo(LocalDate.of(2025, 7, 15));
        assertThat(parser.parse("August 15, 2025")).isEqualTo(LocalDate.of(2025, 8, 15));
        assertThat(parser.parse("September 15, 2025")).isEqualTo(LocalDate.of(2025, 9, 15));
        assertThat(parser.parse("October 15, 2025")).isEqualTo(LocalDate.of(2025, 10, 15));
        assertThat(parser.parse("November 15, 2025")).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(parser.parse("December 15, 2025")).isEqualTo(LocalDate.of(2025, 12, 15));
    }

    @Test
    void parse_withAllMonthsUsingNoCommaFormat_returnsCorrectDates() {
        assertThat(parser.parse("January 15 2025")).isEqualTo(LocalDate.of(2025, 1, 15));
        assertThat(parser.parse("February 15 2025")).isEqualTo(LocalDate.of(2025, 2, 15));
        assertThat(parser.parse("March 15 2025")).isEqualTo(LocalDate.of(2025, 3, 15));
        assertThat(parser.parse("April 15 2025")).isEqualTo(LocalDate.of(2025, 4, 15));
        assertThat(parser.parse("May 15 2025")).isEqualTo(LocalDate.of(2025, 5, 15));
        assertThat(parser.parse("June 15 2025")).isEqualTo(LocalDate.of(2025, 6, 15));
        assertThat(parser.parse("July 15 2025")).isEqualTo(LocalDate.of(2025, 7, 15));
        assertThat(parser.parse("August 15 2025")).isEqualTo(LocalDate.of(2025, 8, 15));
        assertThat(parser.parse("September 15 2025")).isEqualTo(LocalDate.of(2025, 9, 15));
        assertThat(parser.parse("October 15 2025")).isEqualTo(LocalDate.of(2025, 10, 15));
        assertThat(parser.parse("November 15 2025")).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(parser.parse("December 15 2025")).isEqualTo(LocalDate.of(2025, 12, 15));
    }
}
