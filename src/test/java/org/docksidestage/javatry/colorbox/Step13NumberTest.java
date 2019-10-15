/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.javatry.colorbox;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.docksidestage.bizfw.colorbox.ColorBox;
import org.docksidestage.bizfw.colorbox.color.BoxColor;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of Number with color-box. <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author your_name_here
 */
public class Step13NumberTest extends PlainTestCase {

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    static final List<ColorBox> BOXES = new YourPrivateRoom().getColorBoxList();

    static Stream<BoxSpace> getBoxStream() {
        return BOXES.stream().flatMap(colorBox -> colorBox.getSpaceList().stream());
    }

    static BigInteger toIntegerType(Object o) {
        if (o instanceof BigDecimal) {
            try {
                return ((BigDecimal) o).toBigIntegerExact();
            } catch (ArithmeticException e) {
                return null;
            }
        }
        if (o instanceof Long) {
            return BigInteger.valueOf((Long) o);
        }
        if (o instanceof Integer) {
            return BigInteger.valueOf((Integer) o);
        }
        if (o instanceof BigInteger) {
            return (BigInteger) o;
        }
        return null;
    }

    static BigDecimal toDecimalType(Object o, boolean strict) {
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        if (strict) {
            return null;
        }
        if (o instanceof Float) {
            return BigDecimal.valueOf((Float) o);
        }
        if (o instanceof Double) {
            return BigDecimal.valueOf((Double) o);
        }
        try {
            return new BigDecimal(toIntegerType(o));
        } catch (NullPointerException e) {
            return null;
        }
    }

    static BigDecimal toDecimalType(Object o) {
        return toDecimalType(o, false);
    }

    static Stream<Object> extractValues() {
        Stream<Object> rawVals = getBoxStream()
                .map(BoxSpace::getContent);
        Stream<Object> mapVals = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof Map)
                .map(Map.class::cast)
                .flatMap(map -> map.values().stream());
        Stream<Object> listVals = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof List)
                .map(List.class::cast)
                .flatMap(List::stream);
        return Stream.concat(Stream.concat(rawVals, mapVals), listVals);
    }

    static Stream<BigInteger> extractIntegers() {
        return extractValues()
                .map(Step13NumberTest::toIntegerType)
                .filter(Objects::nonNull);
    }

    static Stream<BigDecimal> extractDecimals() {
        return extractValues()
                .map(Step13NumberTest::toDecimalType)
                .filter(Objects::nonNull);
    }

    /**
     * How many integer-type values in color-boxes are between 0 and 54? <br>
     * (カラーボックの中に入っているInteger型で、0から54までの値は何個ある？)
     */
    public void test_countZeroToFiftyFour_IntegerOnly() {
        long count = extractIntegers()
                .filter(i -> i.compareTo(BigInteger.valueOf(0)) > -1 && i.compareTo(BigInteger.valueOf(54)) < 1)
                .count();
        log(count);
    }

    /**
     * How many number values in color-boxes are between 0 and 54? <br>
     * (カラーボックの中に入っている数値で、0から54までの値は何個ある？)
     */
    public void test_countZeroToFiftyFour_Number() {
        long count = extractDecimals()
                .filter(i -> i.compareTo(BigDecimal.valueOf(0)) > -1 && i.compareTo(BigDecimal.valueOf(54)) < 1)
                .count();
        log(count);
    }

    /**
     * What color name is used by color-box that has integer-type content and the biggest width in them? <br>
     * (カラーボックスの中で、Integer型の Content を持っていてBoxSizeの幅が一番大きいカラーボックスの色は？)
     */
    public void test_findColorBigWidthHasInteger() {
        String name = new YourPrivateRoom().getColorBoxList().stream()
                .filter(colorBox -> colorBox.getSpaceList().stream().anyMatch(obj -> toIntegerType(obj.getContent()) != null))
                .max(Comparator.comparingInt(boxSpace -> boxSpace.getSize().getWidth()))
                .map(ColorBox::getColor)
                .map(BoxColor::getColorName)
                .orElse("*no colors found");
        log(name);
    }

    /**
     * What is total of BigDecimal values in List in color-boxes? <br>
     * (カラーボックスの中に入ってる List の中の BigDecimal を全て足し合わせると？)
     */
    public void test_sumBigDecimalInList() {
        BigDecimal total = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof List)
                .map(obj -> (List<Object>) obj)
                .flatMap(List::stream)
                .filter(obj -> obj instanceof BigDecimal)
                .map(BigDecimal.class::cast)
                .reduce(new BigDecimal(0), BigDecimal::add);
        log(total);
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * What key is related to value that is max number in Map that has only number in color-boxes? <br>
     * (カラーボックスに入ってる、valueが数値のみの Map の中で一番大きいvalueのkeyは？)
     */
    public void test_findMaxMapNumberValue() {
        Object key = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof Map)
                .map(Map.class::cast)
                .filter(map -> map.values().stream().allMatch(v -> v instanceof Integer))
                .map(map -> (Map<Object, Integer>) map)
                .map(Map::entrySet)
                .map(entries -> entries.stream().max(Comparator.comparingInt(e -> e.getValue())).orElse(null))
                .filter(Objects::nonNull)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse("*no entry found");
        log(key);
    }

    /**
     * What is total of number or number-character values in Map in purple color-box? <br> 
     * (purpleのカラーボックスに入ってる Map の中のvalueの数値・数字の合計は？)
     */
    public void test_sumMapNumberValue() {
    }
}
