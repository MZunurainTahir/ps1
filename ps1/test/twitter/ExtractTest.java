package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class ExtractTest {

    /*
     * Testing strategy
     *
     * getTimespan:
     *   - list size: 1, 2+
     *   - order: sorted, unsorted
     *   - timestamps: distinct, equal
     *
     * getMentionedUsers:
     *   - mentions per tweet: 0, 1, 2+
     *   - same user in different cases
     *   - invalid: email address, bare "@"
     *   - mention at start / end of text
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "carol", "hello", d3);

    private static Set<String> lower(Set<String> s) {
        Set<String> r = new HashSet<>();
        for (String x : s) r.add(x.toLowerCase());
        return r;
    }

    // ---------- getTimespan ----------

    @Test
    public void testGetTimespanSingleTweet() {
        Timespan ts = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals(d1, ts.getStart());
        assertEquals(d1, ts.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan ts = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals(d1, ts.getStart());
        assertEquals(d2, ts.getEnd());
    }

    @Test
    public void testGetTimespanUnordered() {
        Timespan ts = Extract.getTimespan(Arrays.asList(tweet3, tweet1, tweet2));
        assertEquals(d1, ts.getStart());
        assertEquals(d3, ts.getEnd());
    }

    @Test
    public void testGetTimespanEqualTimestamps() {
        Tweet a = new Tweet(4, "x", "a", d1);
        Tweet b = new Tweet(5, "y", "b", d1);
        Timespan ts = Extract.getTimespan(Arrays.asList(a, b));
        assertEquals(d1, ts.getStart());
        assertEquals(d1, ts.getEnd());
    }

    // ---------- getMentionedUsers ----------

    @Test
    public void testNoMention() {
        assertTrue(Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2)).isEmpty());
    }

    @Test
    public void testOneMention() {
        Tweet t = new Tweet(6, "a", "hi @alyssa how are you", d1);
        Set<String> r = Extract.getMentionedUsers(Arrays.asList(t));
        assertEquals(new HashSet<>(Arrays.asList("alyssa")), lower(r));
    }

    @Test
    public void testMultipleMentions() {
        Tweet t = new Tweet(7, "a", "@bob and @carol_1 met @dave-x.", d1);
        Set<String> r = Extract.getMentionedUsers(Arrays.asList(t));
        assertEquals(new HashSet<>(Arrays.asList("bob", "carol_1", "dave-x")), lower(r));
    }

    @Test
    public void testCaseInsensitiveDuplicates() {
        Tweet t1 = new Tweet(8, "a", "hey @Alyssa", d1);
        Tweet t2 = new Tweet(9, "b", "yo @ALYSSA", d2);
        Set<String> r = Extract.getMentionedUsers(Arrays.asList(t1, t2));
        assertEquals(1, lower(r).size());
        assertTrue(lower(r).contains("alyssa"));
    }

    @Test
    public void testEmailIsNotMention() {
        Tweet t = new Tweet(10, "a", "mail me bitdiddle@mit.edu", d1);
        assertTrue(Extract.getMentionedUsers(Arrays.asList(t)).isEmpty());
    }

    @Test
    public void testBareAt() {
        Tweet t = new Tweet(11, "a", "meet @ noon, @ home", d1);
        assertTrue(Extract.getMentionedUsers(Arrays.asList(t)).isEmpty());
    }

    @Test
    public void testMentionAtStartAndEnd() {
        Tweet t = new Tweet(12, "a", "@first said hi to @last", d1);
        Set<String> r = Extract.getMentionedUsers(Arrays.asList(t));
        assertEquals(new HashSet<>(Arrays.asList("first", "last")), lower(r));
    }
}