package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class FilterTest {

    /*
     * Testing strategy
     *
     * writtenBy:
     *   - matches: 0, 1, many
     *   - username case differs from author case
     *   - result keeps input order
     *
     * inTimespan:
     *   - tweets in span: none, some, all
     *   - tweet exactly on start / end boundary
     *   - empty input list
     *
     * containing:
     *   - words list: 1 word, many words
     *   - matches: none, some
     *   - case differs
     *   - word is only a substring of another word (no match)
     *   - result keeps input order
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "Hello World", d3);

    // ---------- writtenBy ----------

    @Test
    public void testWrittenBySingleResult() {
        List<Tweet> r = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        assertEquals(1, r.size());
        assertTrue(r.contains(tweet1));
    }

    @Test
    public void testWrittenByCaseInsensitiveAndOrder() {
        List<Tweet> r = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "ALYSSA");
        assertEquals(Arrays.asList(tweet1, tweet3), r);
    }

    @Test
    public void testWrittenByNoMatch() {
        assertTrue(Filter.writtenBy(Arrays.asList(tweet1, tweet2), "nobody").isEmpty());
    }

    // ---------- inTimespan ----------

    @Test
    public void testInTimespanSomeInside() {
        Timespan span = new Timespan(Instant.parse("2016-02-17T09:00:00Z"),
                                     Instant.parse("2016-02-17T10:30:00Z"));
        List<Tweet> r = Filter.inTimespan(Arrays.asList(tweet1, tweet2), span);
        assertEquals(Arrays.asList(tweet1), r);
    }

    @Test
    public void testInTimespanAllInside() {
        Timespan span = new Timespan(d1, d3);
        List<Tweet> r = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertEquals(Arrays.asList(tweet1, tweet2, tweet3), r);
    }

    @Test
    public void testInTimespanNoneInside() {
        Timespan span = new Timespan(Instant.parse("2020-01-01T00:00:00Z"),
                                     Instant.parse("2020-01-02T00:00:00Z"));
        assertTrue(Filter.inTimespan(Arrays.asList(tweet1, tweet2), span).isEmpty());
    }

    @Test
    public void testInTimespanBoundariesInclusive() {
        Timespan span = new Timespan(d1, d2);
        List<Tweet> r = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertEquals(Arrays.asList(tweet1, tweet2), r);
    }

    @Test
    public void testInTimespanEmptyInput() {
        assertTrue(Filter.inTimespan(new ArrayList<Tweet>(), new Timespan(d1, d2)).isEmpty());
    }

    // ---------- containing ----------

    @Test
    public void testContainingOneWord() {
        List<Tweet> r = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        assertEquals(Arrays.asList(tweet1, tweet2), r);
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> r = Filter.containing(Arrays.asList(tweet3), Arrays.asList("hello"));
        assertEquals(Arrays.asList(tweet3), r);
    }

    @Test
    public void testContainingMultipleWords() {
        List<Tweet> r = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3),
                                          Arrays.asList("world", "minutes"));
        assertEquals(Arrays.asList(tweet2, tweet3), r);
    }

    @Test
    public void testContainingNoMatch() {
        assertTrue(Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("zzz")).isEmpty());
    }

    @Test
    public void testContainingSubstringIsNotMatch() {
        Tweet t = new Tweet(20, "x", "talking all day", d1);
        assertTrue(Filter.containing(Arrays.asList(t), Arrays.asList("talk")).isEmpty());
    }
}