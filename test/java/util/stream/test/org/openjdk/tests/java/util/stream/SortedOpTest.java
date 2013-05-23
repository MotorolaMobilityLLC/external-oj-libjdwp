/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.tests.java.util.stream;

import org.testng.annotations.Test;

import java.util.*;
import java.util.Spliterators;
import java.util.stream.*;

import static java.util.stream.LambdaTestHelpers.*;

/**
 * SortedOpTest
 *
 * @author Brian Goetz
 */
@Test
public class SortedOpTest extends OpTestCase {
    public void testSorted() {
        assertCountSum(countTo(0).stream().sorted(), 0, 0);
        assertCountSum(countTo(10).stream().sorted(), 10, 55);
        assertCountSum(countTo(10).stream().sorted(cInteger.reverseOrder()), 10, 55);

        List<Integer> to10 = countTo(10);
        assertSorted(to10.stream().sorted(cInteger.reverseOrder()).iterator(), cInteger.reverseOrder());

        Collections.reverse(to10);
        assertSorted(to10.stream().sorted().iterator());

        Spliterator<Integer> s = to10.stream().sorted().spliterator();
        assertTrue(s.hasCharacteristics(Spliterator.SORTED));

        s = to10.stream().sorted(cInteger.reverseOrder()).spliterator();
        assertFalse(s.hasCharacteristics(Spliterator.SORTED));
    }

    @Test(groups = { "serialization-hostile" })
    public void testSequentialShortCircuitTerminal() {
        // The sorted op for sequential evaluation will buffer all elements when accepting
        // then at the end sort those elements and push those elements downstream

        List<Integer> l = Arrays.asList(5, 4, 3, 2, 1);

        // Find
        assertEquals(l.stream().sorted().findFirst(), Optional.of(1));
        assertEquals(l.stream().sorted().findAny(), Optional.of(1));
        assertEquals(unknownSizeStream(l).sorted().findFirst(), Optional.of(1));
        assertEquals(unknownSizeStream(l).sorted().findAny(), Optional.of(1));

        // Match
        assertEquals(l.stream().sorted().anyMatch(i -> i == 2), true);
        assertEquals(l.stream().sorted().noneMatch(i -> i == 2), false);
        assertEquals(l.stream().sorted().allMatch(i -> i == 2), false);
        assertEquals(unknownSizeStream(l).sorted().anyMatch(i -> i == 2), true);
        assertEquals(unknownSizeStream(l).sorted().noneMatch(i -> i == 2), false);
        assertEquals(unknownSizeStream(l).sorted().allMatch(i -> i == 2), false);
    }

    private <T> Stream<T> unknownSizeStream(List<T> l) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(l.iterator(), 0));
    }

    @Test(dataProvider = "StreamTestData<Integer>", dataProviderClass = StreamTestDataProvider.class)
    public void testOps(String name, TestData.OfRef<Integer> data) {
        Collection<Integer> result = exerciseOpsInt(data, Stream::sorted, IntStream::sorted, LongStream::sorted, DoubleStream::sorted);
        assertSorted(result.iterator());
        assertContentsUnordered(data, result);

        result = exerciseOps(data, s -> s.sorted(cInteger.reverseOrder()));
        assertSorted(result.iterator(), cInteger.reverseOrder());
        assertContentsUnordered(data, result);
    }

    @Test(dataProvider = "StreamTestData<Integer>", dataProviderClass = StreamTestDataProvider.class)
    public void testSortSort(String name, TestData.OfRef<Integer> data) {
        // For parallel cases ensure the size is known
        Collection<Integer> result = withData(data)
                .stream(s -> s.sorted().sorted(),
                        new CollectorOps.TestParallelSizedOp<Integer>())
                .exercise();

        assertSorted(result);
        assertContentsUnordered(data, result);

        result = withData(data)
                .stream(s -> s.sorted(cInteger.reverseOrder()).sorted(cInteger.reverseOrder()),
                        new CollectorOps.TestParallelSizedOp<Integer>())
                .exercise();

        assertSorted(result, cInteger.reverseOrder());
        assertContentsUnordered(data, result);

        result = withData(data)
                .stream(s -> s.sorted().sorted(cInteger.reverseOrder()),
                        new CollectorOps.TestParallelSizedOp<Integer>())
                .exercise();

        assertSorted(result, cInteger.reverseOrder());
        assertContentsUnordered(data, result);

        result = withData(data)
                .stream(s -> s.sorted(cInteger.reverseOrder()).sorted(),
                        new CollectorOps.TestParallelSizedOp<Integer>())
                .exercise();

        assertSorted(result);
        assertContentsUnordered(data, result);
    }

    //

    @Test(groups = { "serialization-hostile" })
    public void testIntSequentialShortCircuitTerminal() {
        int[] a = new int[]{5, 4, 3, 2, 1};

        // Find
        assertEquals(Arrays.stream(a).sorted().findFirst(), OptionalInt.of(1));
        assertEquals(Arrays.stream(a).sorted().findAny(), OptionalInt.of(1));
        assertEquals(unknownSizeIntStream(a).sorted().findFirst(), OptionalInt.of(1));
        assertEquals(unknownSizeIntStream(a).sorted().findAny(), OptionalInt.of(1));

        // Match
        assertEquals(Arrays.stream(a).sorted().anyMatch(i -> i == 2), true);
        assertEquals(Arrays.stream(a).sorted().noneMatch(i -> i == 2), false);
        assertEquals(Arrays.stream(a).sorted().allMatch(i -> i == 2), false);
        assertEquals(unknownSizeIntStream(a).sorted().anyMatch(i -> i == 2), true);
        assertEquals(unknownSizeIntStream(a).sorted().noneMatch(i -> i == 2), false);
        assertEquals(unknownSizeIntStream(a).sorted().allMatch(i -> i == 2), false);
    }

    private IntStream unknownSizeIntStream(int[] a) {
        return StreamSupport.intStream(Spliterators.spliteratorUnknownSize(Spliterators.iteratorFromSpliterator(Arrays.spliterator(a)), 0));
    }

    @Test(dataProvider = "IntStreamTestData", dataProviderClass = IntStreamTestDataProvider.class)
    public void testIntOps(String name, TestData.OfInt data) {
        Collection<Integer> result = exerciseOps(data, s -> s.sorted());
        assertSorted(result);
        assertContentsUnordered(data, result);
    }

    @Test(dataProvider = "IntStreamTestData", dataProviderClass = IntStreamTestDataProvider.class)
    public void testIntSortSort(String name, TestData.OfInt data) {
        // For parallel cases ensure the size is known
        Collection<Integer> result = withData(data)
                .stream(s -> s.sorted().sorted(), new CollectorOps.TestParallelSizedOp.OfInt())
                .exercise();

        assertSorted(result);
        assertContentsUnordered(data, result);
    }

    //

    @Test(groups = { "serialization-hostile" })
    public void testLongSequentialShortCircuitTerminal() {
        long[] a = new long[]{5, 4, 3, 2, 1};

        // Find
        assertEquals(Arrays.stream(a).sorted().findFirst(), OptionalLong.of(1));
        assertEquals(Arrays.stream(a).sorted().findAny(), OptionalLong.of(1));
        assertEquals(unknownSizeLongStream(a).sorted().findFirst(), OptionalLong.of(1));
        assertEquals(unknownSizeLongStream(a).sorted().findAny(), OptionalLong.of(1));

        // Match
        assertEquals(Arrays.stream(a).sorted().anyMatch(i -> i == 2), true);
        assertEquals(Arrays.stream(a).sorted().noneMatch(i -> i == 2), false);
        assertEquals(Arrays.stream(a).sorted().allMatch(i -> i == 2), false);
        assertEquals(unknownSizeLongStream(a).sorted().anyMatch(i -> i == 2), true);
        assertEquals(unknownSizeLongStream(a).sorted().noneMatch(i -> i == 2), false);
        assertEquals(unknownSizeLongStream(a).sorted().allMatch(i -> i == 2), false);
    }

    private LongStream unknownSizeLongStream(long[] a) {
        return StreamSupport.longStream(Spliterators.spliteratorUnknownSize(Spliterators.iteratorFromSpliterator(Arrays.spliterator(a)), 0));
    }

    @Test(dataProvider = "LongStreamTestData", dataProviderClass = LongStreamTestDataProvider.class)
    public void testLongOps(String name, TestData.OfLong data) {
        Collection<Long> result = exerciseOps(data, s -> s.sorted());
        assertSorted(result);
        assertContentsUnordered(data, result);
    }

    @Test(dataProvider = "LongStreamTestData", dataProviderClass = LongStreamTestDataProvider.class)
    public void testLongSortSort(String name, TestData.OfLong data) {
        // For parallel cases ensure the size is known
        Collection<Long> result = withData(data)
                .stream(s -> s.sorted().sorted(), new CollectorOps.TestParallelSizedOp.OfLong())
                .exercise();

        assertSorted(result);
        assertContentsUnordered(data, result);
    }

    //

    @Test(groups = { "serialization-hostile" })
    public void testDoubleSequentialShortCircuitTerminal() {
        double[] a = new double[]{5.0, 4.0, 3.0, 2.0, 1.0};

        // Find
        assertEquals(Arrays.stream(a).sorted().findFirst(), OptionalDouble.of(1));
        assertEquals(Arrays.stream(a).sorted().findAny(), OptionalDouble.of(1));
        assertEquals(unknownSizeDoubleStream(a).sorted().findFirst(), OptionalDouble.of(1));
        assertEquals(unknownSizeDoubleStream(a).sorted().findAny(), OptionalDouble.of(1));

        // Match
        assertEquals(Arrays.stream(a).sorted().anyMatch(i -> i == 2.0), true);
        assertEquals(Arrays.stream(a).sorted().noneMatch(i -> i == 2.0), false);
        assertEquals(Arrays.stream(a).sorted().allMatch(i -> i == 2.0), false);
        assertEquals(unknownSizeDoubleStream(a).sorted().anyMatch(i -> i == 2.0), true);
        assertEquals(unknownSizeDoubleStream(a).sorted().noneMatch(i -> i == 2.0), false);
        assertEquals(unknownSizeDoubleStream(a).sorted().allMatch(i -> i == 2.0), false);
    }

    private DoubleStream unknownSizeDoubleStream(double[] a) {
        return StreamSupport.doubleStream(Spliterators.spliteratorUnknownSize(Spliterators.iteratorFromSpliterator(Arrays.spliterator(a)), 0));
    }

    @Test(dataProvider = "DoubleStreamTestData", dataProviderClass = DoubleStreamTestDataProvider.class)
    public void testDoubleOps(String name, TestData.OfDouble data) {
        Collection<Double> result = exerciseOps(data, s -> s.sorted());
        assertSorted(result);
        assertContentsUnordered(data, result);
    }

    @Test(dataProvider = "DoubleStreamTestData", dataProviderClass = DoubleStreamTestDataProvider.class)
    public void testDoubleSortSort(String name, TestData.OfDouble data) {
        // For parallel cases ensure the size is known
        Collection<Double> result = withData(data)
                .stream(s -> s.sorted().sorted(), new CollectorOps.TestParallelSizedOp.OfDouble())
                .exercise();

        assertSorted(result);
        assertContentsUnordered(data, result);
    }
}
