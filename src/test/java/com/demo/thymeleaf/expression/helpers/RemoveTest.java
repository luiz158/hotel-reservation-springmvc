package com.demo.thymeleaf.expression.helpers;

import com.demo.thymeleaf.expression.QueryStringHelper;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RemoveTest {

    /**
     * When the key is not found, return the original query string.
     */
    @Test
    public void removeFirst_KeyNotFound_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeFirst(mockHttpRequest, "missing");
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the argument is null, return the original query string.
     */
    @Test
    public void removeFirst_HandlesNull_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeFirst(mockHttpRequest, null);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Only the relative index = 0 should be removed. Normally a query string has unique keys
     * so removeFirst has the same effect has removing the entire key.
     * If there are multiple duplicate keys, all but the first value will remain.
     */
    @Test
    public void removeFirst_RemovesFirstOccurrenceOnly() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        // The second occurrence still exists.
        String expected = "key4=yy&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeFirst(mockHttpRequest, "key2");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the key is not found, return the original query string.
     */
    @Test
    public void removeAll_KeyNotFound_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAll(mockHttpRequest, Arrays.asList("missing"));
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When there is a null argument, return the original query string.
     */
    @Test
    public void removeAll_HandlesNullArg() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAll(mockHttpRequest, null);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When there argument is an empty list, return the original query string.
     */
    @Test
    public void removeAll_HandlesEmptyList_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAll(mockHttpRequest, Arrays.asList());
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When there is only 1 key to delete, that key should be completely removed.
     */
    @Test
    public void removeAll_RemoveSingleKey_AllKeyValuePairsRemoved() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "key4=yy&key3=value3&key4=value5";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAll(mockHttpRequest, Arrays.asList("key2"));
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When there are many keys to delete, only those keys should be completely removed.
     * In this test, key2 and key4 are deleted leaving only key3.
     */
    @Test
    public void removeAll_RemoveManyKeys_AllKeyValuePairsRemoved() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "key3=value3";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAll(mockHttpRequest, Arrays.asList("key2", "key4"));
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Sanity check - if all keys are removed then an empty string is expected.
     */
    @Test
    public void removeAll_RemoveAllKeys_ResultsInEmptyString() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAll(mockHttpRequest, Arrays.asList("key2", "key4", "key3"));
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the key is not found, return the original query string.
     */
    @Test
    public void removeN_KeyNotFound_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "missing", 10);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the key is null, return the original query string.
     */
    @Test
    public void removeN_HandlesNull_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, null, 10);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When n is 0 it should return the original query string.
     */
    @Test
    public void removeN_NIsZero_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key2", 0);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * n < 0 is an error which should be ignored and have no effect
     */
    @Test
    public void removeN_NIsNegative_HasNoEffect() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key2", -1);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Given key2 appears 4 times, removing n=1 shall only remove the first occurrence.
     */
    @Test
    public void removeN_RemovesFirstOccurrence_WithDuplicateKeys() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key2", 1);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * key4 only exists once, therefore the entire key shall be removed.
     */
    @Test
    public void removeN_RemovesEntireKey_WithOnlySingleKey() {
        String query = "key2=aa&key4=yy&key5=bb";
        String expected = "key2=aa&key5=bb";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key4", 1);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When n is greater than the total occurrences of the given key, the entire key
     * is deleted. Eg: key2 occurs 4 times but n=5 so delete the whole key.
     */
    @Test
    public void removeN_NIsGreaterThanKeyOccurrences_RemovesEntireKey() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "key4=yy&key3=value3&key4=value5";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key2", 5);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When n is equal to the total occurrences of the given key, the entire key
     * is deleted. Eg: key2 occurs 4 times but n=4 so delete the whole key.
     */
    @Test
    public void removeN_NIsEqualToKeyOccurrences_RemovesEntireKey() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "key4=yy&key3=value3&key4=value5";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key2", 4);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When n is less than the total occurrences of the given key, only the first n
     * keys shall be removed. Eg: key2 appears 4 times and n is 3 so only the last key2 value remains.
     */
    @Test
    public void removeN_NIsLessThanKeyOccurrences_RemovesEntireKey() {
        String query = "key2=aa&key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";
        String expected = "key4=yy&key3=value3&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeN(mockHttpRequest, "key2", 3);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the key is not found, return the original query string.
     */
    @Test
    public void removeNth_KeyNotFound_HasNoEffect() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, "missing", 0);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the key is null, return the original query string.
     */
    @Test
    public void removeNth_HandlesNull_HasNoEffect() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, null, 0);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Visualise all key2 values being in a list [bb, cc, dd]. The original query string should be returned
     * if the index is outside array bounds.
     */
    @Test
    public void removeNth_InvalidIndex_LowerBound_HasNoEffect() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, "key2", -1);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Visualise all key2 values being in a list [bb, cc, dd]. The original query string should be returned
     * if the index is outside array bounds.
     */
    @Test
    public void removeNth_InvalidIndex_UpperBound_HasNoEffect() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd&key9=abcd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, "key2", 3);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * 'nth=0' should remove the first occurrence of given key.
     */
    @Test
    public void removeNth_ValidIndex_RemovesFirstOccurrence() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd&key9=abcd";
        String expected = "key4=yy&key3=value3&key2=cc&key4=value5&key2=dd&key9=abcd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, "key2", 0);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * 'nth=1' should remove the second occurrence of given key.
     */
    @Test
    public void removeNth_ValidIndex_RemovesMiddleOccurrence() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd&key9=abcd";
        String expected = "key4=yy&key2=bb&key3=value3&key4=value5&key2=dd&key9=abcd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, "key2", 1);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * 'nth=2' should remove the third occurrence of given key.
     */
    @Test
    public void removeNth_ValidIndex_RemovesLastOccurrence() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd&key9=abcd";
        String expected = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key9=abcd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeNth(mockHttpRequest, "key2", 2);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * null arguments should return original query string.
     */
    @Test
    public void removeManyNth_HandlesNull_HasNoEffect() {
        String query = "key4=yy&key2=bb&key3=value3&key2=cc&key4=value5&key2=dd&key9=abcd";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, null, null);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * empty relative indexes argument should return original query string since there will be nothing to delete.
     */
    @Test
    public void removeManyNth_HandlesEmptyIndexList_HasNoEffect() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Collections.emptyList();

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "key2", relativeIndexes);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When key does not exist, the original query string should be returned.
     */
    @Test
    public void removeManyNth_KeyNotFound_HasNoEffect() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Arrays.asList(0, 3);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "missing", relativeIndexes);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Imagine key2 is a list containing the following values. key2 = [aa, dd, ff, hh].
     * If an index outside the array bound is provided, it should be ignored with no effect.
     */
    @Test
    public void removeManyNth_InvalidIndexes() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Arrays.asList(-1, 4, 20);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "key2", relativeIndexes);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Imagine key2 is a list containing the following values. key2 = [aa, dd, ff, hh]
     *
     * When removeManyNth is provided every index in the range of keys, all corresponding
     * key value pairs should be removed.
     */
    @Test
    public void removeManyNth_RemovesAllIndexesForKey() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&key9=ii";
        String expected = "key3=bb&key4=cc&key5=gg&key4=ff&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Arrays.asList(0, 1, 2, 3);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "key2", relativeIndexes);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Imagine key2 is a list containing the following values. key2 = [aa, dd, ff, hh]
     *
     * When removeManyNth is provided every index in the range of keys, all corresponding
     * key value pairs should be removed. The order of indexes to remove should not matter.
     */
    @Test
    public void removeManyNth_RemovesAllIndexesForKey_OrderDoesNotMatter() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&key9=ii";
        String expected = "key3=bb&key4=cc&key5=gg&key4=ff&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Arrays.asList(3, 2, 1, 0);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "key2", relativeIndexes);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Imagine key2 is a list containing the following values. key2 = [aa, dd, ff, hh]
     * <p>The removeManyNth method should only remove the values at the supplied indexes.
     * If relative indexes 1 and 2 were removed, aa and hh would remain.</p>
     */
    @Test
    public void removeManyNth_RemovesOnlySelectedIndexes_InnerRange() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&key9=ii";
        String expected = "key2=aa&key3=bb&key4=cc&key5=gg&key4=ff&key2=hh&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Arrays.asList(1, 2);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "key2", relativeIndexes);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Imagine key2 is a list containing the following values. key2 = [aa, dd, ff, hh]
     * <p>The removeManyNth method should only remove the values at the supplied indexes.
     * If relative indexes 0 and 3 were removed, dd and ff would remain.</p>
     */
    @Test
    public void removeManyNth_RemovesOnlySelectedIndexes_OuterRange() {
        String query = "key2=aa&key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key2=hh&key9=ii";
        String expected = "key3=bb&key4=cc&key2=dd&key2=ff&key5=gg&key4=ff&key9=ii";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        List<Integer> relativeIndexes = Arrays.asList(0, 3);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeManyNth(mockHttpRequest, "key2", relativeIndexes);
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When no key is found, return the original query string.
     */
    @Test
    public void removeKeyMatchingValue_KeyNotFound_HasNoEffect() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueF";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, "missing", "Value");
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the arguments are null, return the original query string.
     */
    @Test
    public void removeKeyMatchingValue_HandlesNull_HasNoEffect() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueF";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, null, null);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When no key has the matching value, the original query string should be returned.
     */
    @Test
    public void removeKeyMatchingValue_NoMatchingValueFound_HasNoEffect() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueB";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, "key2", "ValueNotFound");
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * All target keys having the matching value should be removed.
     * Eg all key2 keys having 'ValueB' are removed.
     */
    @Test
    public void removeKeyMatchingValue_MatchingValueFound_RemovesExpectedMatches() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueB";
        String expected = "key4=ValueA&key3=ValueC&key2=ValueD&key4=ValueE";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, "key2", "ValueB");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Ensure values are matched without encoded query string. This is important to check as its possible for
     * the implementation to keep the query string encoded which would mean the 'valueMatch' argument would never
     * match an existing value.
     */
    @Test
    public void removeKeyMatchingValue_MatchingValueFoundWithSpaces_RemovesExpectedMatches() {
        String query = "key4=Value%20A&key2=Value%20B&key3=Value%20C&key2=Value%20D&key4=Value%20E&key2=Value%20B";
        String expected = "key4=Value%20A&key3=Value%20C&key2=Value%20D&key4=Value%20E";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, "key2", "Value B");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * As a sanity check, if all keys have the same value, ONLY the target key of 'key2' should be removed.
     * This is because 'key2' is used to control which key is eligible to removed when the value matches.
     */
    @Test
    public void removeKeyMatchingValue_MatchingValueFound_RemovesAll() {
        String query = "key4=ValueA&key2=ValueA&key3=ValueA&key2=ValueA&key4=ValueA&key2=ValueA";
        String expected = "key4=ValueA&key3=ValueA&key4=ValueA";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, "key2", "ValueA");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * The equality check should not take case into consideration.
     */
    @Test
    public void removeKeyMatchingValue_MatchingValueFound_CaseInsensitive() {
        String query = "key4=ALL%20SORTED%202%2055_t3.7&key2=ValueA&key3=ValueA&key4=ALL%20SORTED%203&key2=ALL%20SORTED%202%2055_t3.7";
        String expected = "key4=ALL%20SORTED%202%2055_t3.7&key2=ValueA&key3=ValueA&key4=ALL%20SORTED%203";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeKeyMatchingValue(mockHttpRequest, "key2", "alL sOrtEd 2 55_t3.7");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When no key has the matching value, the original query string should be returned.
     */
    @Test
    public void removeAnyKeyMatchingValue_NoValueMatches_HasNoEffect() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueF";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "MissingValue");
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * When the value to match argument is null, the original query string should be returned.
     */
    @Test
    public void removeAnyKeyMatchingValue_HandlesNull_HasNoEffect() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueF";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, null);
        assertThat(result).isEqualTo(query);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Only the keys having the matching value should be removed.
     *
     * key2 appears twice having ValueB, therefore only those key2 instances should be removed. Notice
     * how key2=ValueD still remains, this is because it does not have ValueB.
     */
    @Test
    public void removeAnyKeyMatchingValue_ValueMatches_AllMatchingKeysRemoved() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueB";
        String expected = "key4=ValueA&key3=ValueC&key2=ValueD&key4=ValueE";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "ValueB");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * key4 appears twice having ValueA and ValueE. Since the removal is based on ValueA, key4=ValueE should
     * remain.
     */
    @Test
    public void removeAnyKeyMatchingValue_ValueMatches_SameKeyDifferentValue() {
        String query = "key4=ValueA&key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueB";
        String expected = "key2=ValueB&key3=ValueC&key2=ValueD&key4=ValueE&key2=ValueB";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "ValueA");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * Ensure escape characters are not used in query string implementation which would mess up the matching.
     * Eg %20 whitespace should be removed prior to matching.
     */
    @Test
    public void removeAnyKeyMatchingValue_ValueMatchesWithEscaped_AllMatchingKeysRemoved() {
        String query = "key4=ValueA&key2=Value%20B&key3=ValueC&key2=ValueD&key4=ValueE&key2=Value%20B";
        String expected = "key4=ValueA&key3=ValueC&key2=ValueD&key4=ValueE";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "Value B");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * key3 only exists once, therefore it should be removed when the value is matched.
     */
    @Test
    public void removeAnyKeyMatchingValue_SingleKeyOnly_SingleKeyRemoved() {
        String query = "key4=ValueA&key2=Value%20B&key3=ValueC&key2=ValueD&key4=ValueE&key2=Value%20B";
        String expected = "key4=ValueA&key2=Value%20B&key2=ValueD&key4=ValueE&key2=Value%20B";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "ValueC");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }


    /**
     * As a sanity check, if all keys have the same value the result should be an empty string.
     */
    @Test
    public void removeAnyKeyMatchingValue_MatchingValueFound_RemovesAll() {
        String query = "key4=ValueA&key2=ValueA&key3=ValueA&key2=ValueA&key4=ValueA&key2=ValueA";
        String expected = "";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "ValueA");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }

    /**
     * The equality check should not take case into consideration.
     */
    @Test
    public void removeAnyKeyMatchingValue_MatchingValueFound_CaseInsensitive() {
        String query = "key4=ALL%20SORTED%202%20-%2099.6-12&key2=ValueA&key3=ALL%20SORTED%202%20-%2099.6-12&key2=ValueA&key4=ValueA&key2=ALL%20SORTED%202%20-%2099.6-12";
        String expected = "key2=ValueA&key2=ValueA&key4=ValueA";

        HttpServletRequest mockHttpRequest = mock(HttpServletRequest.class);
        when(mockHttpRequest.getQueryString()).thenReturn(query);

        QueryStringHelper helper = new QueryStringHelper();
        String result = helper.removeAnyKeyMatchingValue(mockHttpRequest, "aLL sOrTed 2 - 99.6-12");
        assertThat(result).isEqualTo(expected);

        verify(mockHttpRequest, times(1)).getQueryString();
        verifyNoMoreInteractions(mockHttpRequest);
    }
}
