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

import static org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.docksidestage.bizfw.colorbox.ColorBox;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of String with color-box, using Stream API you can. <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author your_name_here
 */
public class Step12StreamStringTest extends PlainTestCase {

    // ===================================================================================
    //                                                                            length()
    //                                                                            ========
    /**
     * What is color name length of first color-box? <br>
     * (最初のカラーボックスの色の名前の文字数は？)
     */
    public void test_length_basic() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String answer = colorBoxList.stream()
                .findFirst()
                .map(colorBox -> colorBox.getColor().getColorName())
                .map(colorName -> colorName.length() + " (" + colorName + ")")
                .orElse("*not found");
        log(answer);
    }

    /**
     * Which string has max length in color-boxes? <br>
     * (カラーボックスに入ってる文字列の中で、一番長い文字列は？)
     */
    public void test_length_findMax() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String str = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .collect(Collectors.maxBy(Comparator.comparing(String::length)))
                .orElse("*no strings found");
        log(str);
    }

    /**
     * How many characters are difference between max and min length of string in color-boxes? <br>
     * (カラーボックスに入ってる文字列の中で、一番長いものと短いものの差は何文字？)
     */
    public void test_length_findMaxMinDiff() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String min = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .collect(Collectors.minBy(Comparator.comparing(String::length)))
                .orElseThrow(() -> new IllegalStateException("*max not found"));
        String max = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .collect(Collectors.maxBy(Comparator.comparing(String::length)))
                .orElseThrow(() -> new IllegalStateException("*max not found"));
        log(max.length() - min.length());
    }

    // has small #adjustmemts from ClassicStringTest
    //  o sort allowed in Stream
    /**
     * Which value (toString() if non-string) has second-max length in color-boxes? (sort allowed in Stream)<br>
     * (カラーボックスに入ってる値 (文字列以外はtoString()) の中で、二番目に長い文字列は？ (Streamでのソートありで))
     */
    public void test_length_findSecondMax() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int secondMax = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .map(String::length)
                .collect(Collector.of(() -> new IntPair(),
                        (acc, val) -> acc.pushLarger(val),
                        (pair1, pair2) -> IntPair.pushLarger(pair1, pair2),
                        pair -> pair.getSmaller()));
        log(secondMax);
    }

    static class IntPair {
        private int smaller;
        private int larger;

        public IntPair() { this(Integer.MIN_VALUE, Integer.MIN_VALUE + 1); }

        public IntPair(int smaller, int larger) {
            if (smaller == larger) {
                throw new IllegalStateException("both ints are the same size");
            }
            this.smaller = Math.min(smaller, larger);
            this.larger = Math.max(smaller, larger);
        }

        public int getSmaller() {
            return smaller;
        }

        public int getLarger() {
            return larger;
        }

        public IntPair pushLarger(int i) {
            if (i > larger) {
                smaller = larger;
                larger = i;
            } else if (i > smaller && i < larger) {
                smaller = i;
            }
            return this;
        }

        public IntPair pushSmaller(int i) {
            if (i < smaller) {
                larger = smaller;
                smaller = i;
            } else if (i < larger && i > smaller) {
                larger = i;
            }
            return this;
        }

        public static IntPair pushLarger(IntPair pair1, IntPair pair2) {
            IntPair largerPair, smallerPair;
            if (pair1.getLarger() > pair2.getLarger()) {
                largerPair = pair1;
                smallerPair = pair2;
            } else {
                largerPair = pair2;
                smallerPair = pair1;
            }
            return new IntPair(Math.max(largerPair.getSmaller(), smallerPair.getLarger()), largerPair.getLarger());
        }

        public static IntPair pushSmaller(IntPair pair1, IntPair pair2) {
            IntPair largerPair, smallerPair;
            if (pair1.getLarger() > pair2.getLarger()) {
                largerPair = pair1;
                smallerPair = pair2;
            } else {
                largerPair = pair2;
                smallerPair = pair1;
            }
            return new IntPair(smallerPair.getSmaller(), Math.min(smallerPair.getLarger(), largerPair.getSmaller()));
        }
    }

    /**
     * How many total lengths of strings in color-boxes? <br>
     * (カラーボックスに入ってる文字列の長さの合計は？)
     */
    public void test_length_calculateLengthSum() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int totalSum = colorBoxList.stream()
                .mapToInt(colorBox -> colorBox.toString().length())
                .sum();
        log(totalSum);
    }

    /**
     * Which color name has max length in color-boxes? <br>
     * (カラーボックスの中で、色の名前が一番長いものは？)
     */
    public void test_length_findMaxColorSize() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String name = colorBoxList.stream()
                .map(colorBox -> colorBox.getColor().toString())
                .max(Comparator.comparing(String::length))
                .orElseThrow(() -> new IllegalStateException("*no results"));
        log(name);
    }

    // ===================================================================================
    //                                                            startsWith(), endsWith()
    //                                                            ========================
    /**
     * What is color in the color-box that has string starting with "Water"? <br>
     * ("Water" で始まる文字列をしまっているカラーボックスの色は？)
     */
    public void test_startsWith_findFirstWord() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String color = colorBoxList.stream()
                .filter(colorBox -> colorBox.getSpaceList().stream()
                        .map(BoxSpace::getContent)
                        .filter(object -> object.toString().startsWith("Water"))
                        .findFirst()
                        .isPresent())
                .map(colorBox -> colorBox.getColor().toString())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*couldn't find color starting with water"));
        log(color);
    }

    /**
     * What is color in the color-box that has string ending with "front"? <br>
     * ("front" で終わる文字列をしまっているカラーボックスの色は？)
     */
    public void test_endsWith_findLastWord() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String color = colorBoxList.stream()
                .filter(colorBox -> colorBox.getSpaceList().stream()
                        .map(BoxSpace::getContent)
                        .filter(object -> object.toString().endsWith("front"))
                        .findFirst()
                        .isPresent())
                .map(colorBox -> colorBox.getColor().toString())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*couldn't find color ending with front"));
        log(color);
    }

    // ===================================================================================
    //                                                            indexOf(), lastIndexOf()
    //                                                            ========================
    /**
     * What number character is starting with first "front" of string ending with "front" in color-boxes? <br>
     * (カラーボックスに入ってる "front" で終わる文字列で、最初の "front" は何文字目から始まる？)
     */
    public void test_indexOf_findIndex() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int pos = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .map(str -> str.indexOf("front"))
                .filter(ind -> ind > -1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no elements contain front"));

        log(pos);
    }

    /**
     * What number character is starting with the late "ど" of string containing plural "ど"s in color-boxes? (e.g. "どんどん" => 3) <br>
     * (カラーボックスに入ってる「ど」を二つ以上含む文字列で、最後の「ど」は何文字目から始まる？ (e.g. "どんどん" => 3))
     */
    public void test_lastIndexOf_findIndex() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int pos = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .filter(str -> str.indexOf("ど") > -1 && str.lastIndexOf("ど") > -1 && str.indexOf("ど") != str.lastIndexOf("ど"))
                .map(str -> str.lastIndexOf("ど"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no elements contain ど"));

        log(pos);
    }

    // ===================================================================================
    //                                                                         substring()
    //                                                                         ===========
    /**
     * What character is first of string ending with "front" in color-boxes? <br>
     * (カラーボックスに入ってる "front" で終わる文字列の最初の一文字は？)
     */
    public void test_substring_findFirstChar() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        char c = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .filter(str -> str.endsWith("front"))
                .map(str -> str.charAt(0))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no elements contain front"));

        log(c);
    }

    /**
     * What character is last of string starting with "Water" in color-boxes? <br>
     * (カラーボックスに入ってる "Water" で始まる文字列の最後の一文字は？)
     */
    public void test_substring_findLastChar() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        char c = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .filter(str -> str.startsWith("Water"))
                .map(str -> str.charAt(str.length() - 1))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no elements contain Water"));

        log(c);
    }

    // ===================================================================================
    //                                                                           replace()
    //                                                                           =========
    /**
     * How many characters does string that contains "o" in color-boxes and removing "o" have? <br>
     * (カラーボックスに入ってる "o" (おー) を含んだ文字列から "o" を全て除去したら何文字？)
     */
    public void test_replace_remove_o() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int count = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .map(Object::toString)
                .filter(str -> str.contains("o"))
                .map(str -> str.replace("o", ""))
                .map(str -> str.length())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no elements contain o"));

        log(count);
    }

    /**
     * What string is path string of java.io.File in color-boxes, which is replaced with "/" to Windows file separator? <br>
     * カラーボックスに入ってる java.io.File のパス文字列のファイルセパレーターの "/" を、Windowsのファイルセパレーターに置き換えた文字列は？
     */
    public void test_replace_fileseparator() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String path = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(content -> content instanceof java.io.File)
                .map(Object::toString)
                .map(str -> str.replace("/", "\\"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no file elements"));

        log(path);
    }

    // ===================================================================================
    //                                                                    Welcome to Devil
    //                                                                    ================
    /**
     * What is total length of text of DevilBox class in color-boxes? <br>
     * (カラーボックスの中に入っているDevilBoxクラスのtextの長さの合計は？)
     */
    public void test_welcomeToDevil() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int len = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(content -> content instanceof DevilBox)
                .map(boxSpace -> (DevilBox) boxSpace)
                .peek(devilBox -> { devilBox.wakeUp(); devilBox.allowMe(); devilBox.open(); })
                .map(db -> {
                    try {
                        return db.getText();
                    } catch (DevilBoxTextNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .mapToInt(String::length)
                .sum();

        log(len);
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * What string is converted to style "map:{ key = value ; key = value ; ... }" from java.util.Map in color-boxes? <br>
     * (カラーボックスの中に入っている java.util.Map を "map:{ key = value ; key = value ; ... }" という形式で表示すると？)
     */
    public void test_showMap_flat() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String str = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(content -> content instanceof Map)
                .map(content -> (Map) content)
                .map(m -> (Set<Map.Entry<Object, Object>>) m.entrySet())
                .map(Step11ClassicStringTest::convertMapToString)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no maps found"));

        log(str);
    }

    /**
     * What string is converted to style "map:{ key = value ; key = map:{ key = value ; ... } ; ... }" from java.util.Map in color-boxes? <br>
     * (カラーボックスの中に入っている java.util.Map を "map:{ key = value ; key = map:{ key = value ; ... } ; ... }" という形式で表示すると？)
     */
    public void test_showMap_nested() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String str = colorBoxList.stream()
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(content -> content instanceof Map)
                .map(content -> (Map) content)
                .filter(m -> m.values().stream().allMatch(v -> v instanceof Map))
                .map(Step11ClassicStringTest::convertMapToString)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("*no nested maps found"));

        log(str);
    }

    // ===================================================================================
    //                                                                           Good Luck
    //                                                                           =========
    // has small #adjustmemts from ClassicStringTest
    //  o comment out because of too difficult to be stream?
    ///**
    // * What string of toString() is converted from text of SecretBox class in upper space on the "white" color-box to java.util.Map? <br>
    // * (whiteのカラーボックスのupperスペースに入っているSecretBoxクラスのtextをMapに変換してtoString()すると？)
    // */
    //public void test_parseMap_flat() {
    //}
    //
    ///**
    // * What string of toString() is converted from text of SecretBox class in both middle and lower spaces on the "white" color-box to java.util.Map? <br>
    // * (whiteのカラーボックスのmiddleおよびlowerスペースに入っているSecretBoxクラスのtextをMapに変換してtoString()すると？)
    // */
    //public void test_parseMap_nested() {
    //}
}
