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

import java.io.File;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.docksidestage.bizfw.colorbox.ColorBox;
import org.docksidestage.bizfw.colorbox.color.BoxColor;
import org.docksidestage.bizfw.colorbox.impl.StandardColorBox;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.DevilBox;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of String with color-box, not using Stream API. <br>
 * Show answer by log() for question of javadoc. <br>
 * <pre>
 * addition:
 * o e.g. "string in color-boxes" means String-type content in space of color-box
 * o don't fix the YourPrivateRoom class and color-box classes
 * </pre>
 * @author jflute
 * @author your_name_here
 */
public class Step11ClassicStringTest extends PlainTestCase {

    // ===================================================================================
    //                                                                            length()
    //                                                                            ========
    /**
     * How many lengths does color name of first color-boxes have? <br>
     * (最初のカラーボックスの色の名前の文字数は？)
     */
    public void test_length_basic() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        if (!colorBoxList.isEmpty()) {
            ColorBox colorBox = colorBoxList.get(0);
            BoxColor boxColor = colorBox.getColor();
            String colorName = boxColor.getColorName();
            int answer = colorName.length();
            log(answer + " (" + colorName + ")"); // also show name for visual check
        } else {
            log("*not found");
        } // 5
    }

    /**
     * Which string has max length in color-boxes? <br>
     * (カラーボックスに入ってる文字列の中で、一番長い文字列は？)
     */
    public void test_length_findMax() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String str = "";
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                // done TODO mattori space.toString()だと必ずしもString型じゃなくても当てはまってしまうので, instanceofを使って型をチェックしてあげよう by もってぃ
                //              （ここだけじゃなくて他のところも直してあげて！）
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String && content.toString().length() > str.length()) {
                        str = content.toString();
                    }
                }
            }
        }
        log(str == "" ? "*no strings found" : str);
    }

    /**
     * How many characters are difference between max and min length of string in color-boxes? <br>
     * (カラーボックスに入ってる文字列の中で、一番長いものと短いものの差は何文字？)
     */
    public void test_length_findMaxMinDiff() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String min = null, max = null;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null && space.getContent() instanceof String) {
                    String s = space.getContent().toString();
                    if (min == null) {
                        min = s;
                    } else {
                        if (s.length() < min.length()) {
                            min = s;
                        }
                    }
                    if (max == null) {
                        max = s;
                    } else {
                        if (s.length() > max.length()) {
                            max = s;
                        }
                    }
                }
            }
        }
        log(max.length() - min.length());
    }

    /**
     * Which value (toString() if non-string) has second-max length in color-boxes? (without sort) <br>
     * (カラーボックスに入ってる値 (文字列以外はtoString()) の中で、二番目に長い文字列は？ (ソートなしで))
     */
    public void test_length_findSecondMax() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String max = "", sec = "";
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null && space.getContent() instanceof String) {
                    String s = space.getContent().toString();
                    if (s.length() > max.length()) {
                        sec = max;
                        max = s;
                    } else if (s.length() > sec.length()) {
                        sec = s;
                    }
                }
            }
        }
        log(sec.length());
    }

    /**
     * How many total lengths of strings in color-boxes? <br>
     * (カラーボックスに入ってる文字列の長さの合計は？)
     */
    public void test_length_calculateLengthSum() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int totalSum = 0;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null && space.getContent() instanceof String) {
                    totalSum += space.getContent().toString().length();
                }
            }
        }
        log(totalSum);
    }

    /**
     * Which color name has max length in color-boxes? <br>
     * (カラーボックスの中で、色の名前が一番長いものは？)
     */
    public void test_length_findMaxColorSize() {
        Set<String> result = new HashSet<>();
        int maxLen = 0;
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        for (ColorBox box : colorBoxList) {
            String colorName = box.getColor().getColorName();
            int length = colorName.length();
            if (result.size() == 0) {
                result.add(colorName);
                maxLen = length;
                continue;
            }
            if (length == maxLen) {
                result.add(colorName);
            }
            if (length > maxLen) {
                result.clear();
                result.add(colorName);
                maxLen = length;
            }
        }
        log(result);
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
        String color = "*no color with string starting with water";
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String && content.toString().startsWith("Water")) {
                        color = box.getColor().getColorName();
                    }
                }
            }
        }
        log(color);
    }

    /**
     * What is color in the color-box that has string ending with "front"? <br>
     * ("front" で終わる文字列をしまっているカラーボックスの色は？)
     */
    public void test_endsWith_findLastWord() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String color = "*no color with string ending with front";
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String && content.toString().endsWith("front")) {
                        color = box.getColor().getColorName();
                    }
                }
            }
        }
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
        int pos = -1;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String && content.toString().endsWith("front")) {
                        pos = content.toString().indexOf("front");
                    }
                }
            }
        }
        log(pos);
    }

    /**
     * What number character is starting with the late "ど" of string containing plural "ど"s in color-boxes? (e.g. "どんどん" => 3) <br>
     * (カラーボックスに入ってる「ど」を二つ以上含む文字列で、最後の「ど」は何文字目から始まる？ (e.g. "どんどん" => 3))
     */
    public void test_lastIndexOf_findIndex() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        int pos = 0;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space.getContent() instanceof String) {
                    String s = space.getContent().toString();
                    int last = s.lastIndexOf("ど");
                    int first = s.indexOf("ど");
                    if (last > -1 && first > -1 && last != first) {
                        pos = last;
                    }
                }
            }
        }

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
        Character c = null;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String) {
                        if (content.toString().endsWith("front")) {
                            c = content.toString().charAt(0);
                        }
                    }
                }
            }
        }

        log(c);
    }

    /**
     * What character is last of string starting with "Water" in color-boxes? <br>
     * (カラーボックスに入ってる "Water" で始まる文字列の最後の一文字は？)
     */
    public void test_substring_findLastChar() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        Character c = null;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String) {
                        if (content.toString().startsWith("Water")) {
                            c = content.toString().charAt(content.toString().length() - 1);
                        }
                    }
                }
            }
        }

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
        int count = -1;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null) {
                    Object content = space.getContent();
                    if (content instanceof String && content.toString().contains("o")) {
                        count = content.toString().replace("o", "").length();
                        break;
                    }
                }
            }
        }

        log(count);
    }

    /**
     * What string is path string of java.io.File in color-boxes, which is replaced with "/" to Windows file separator? <br>
     * カラーボックスに入ってる java.io.File のパス文字列のファイルセパレーターの "/" を、Windowsのファイルセパレーターに置き換えた文字列は？
     */
    public void test_replace_fileseparator() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String path = "*no file found";
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space.getContent() instanceof File) {
                    path = space.getContent().toString().replace("/", "\\");
                }
            }
        }

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
        int len = 0;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space != null && space.getContent() instanceof DevilBox) {
                    DevilBox db = (DevilBox) space.getContent();
                    db.wakeUp();
                    db.allowMe();
                    db.open();
                    try {
                        len += db.getText().length();
                    } catch (DevilBoxTextNotFoundException e) {

                    }
                 }
            }
        }

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
        String str = null;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space.getContent() instanceof Map) {
                    Map<Object, Object> m = (Map) space.getContent();
                    // TODO mattori 最後以外のMapが出力されてないよー　Contentの中身に、Mapは複数個入っているので、毎回loggingした方がいいと思うよ。by ちーかま
                    str = convertMapToString(m);
                }
            }
        }

        log(str);
    }

    /**
     * What string is converted to style "map:{ key = value ; key = map:{ key = value ; ... } ; ... }" from java.util.Map in color-boxes? <br>
     * (カラーボックスの中に入っている java.util.Map を "map:{ key = value ; key = map:{ key = value ; ... } ; ... }" という形式で表示すると？)
     */
    public void test_showMap_nested() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        String str = null;
        for (ColorBox box : colorBoxList) {
            for (BoxSpace space : box.getSpaceList()) {
                if (space.getContent() instanceof Map) {
                    Map<Object, Object> m = (Map) space.getContent();
                    boolean isNested = false;
                    for (Object o : m.values()) {
                        if (o instanceof Map) {
                            isNested = true;
                            break;
                        }
                    }
                    // TODO isNestedがfalseの場合にも、loggingされるようにしよう by ちーかま
                    if (isNested) {
                        // TODO mattori 上のと一緒で、最後以外のMapが出力されてないよー　全てのMapがloggingされるようにしよう。by ちーかま
                        str = convertMapToString(m);
                    }
                }
            }
        }

        log(str);
    }

    static String convertMapToString(Set<Map.Entry<Object, Object>> entrySet) {
        return entrySet.stream()
                .map(e -> e.getKey() + " = " + (e.getValue() instanceof Map ? convertMapToString((Map<Object, Object>) e.getValue()) : e.getValue()))
                .collect(Collectors.joining(" ; ", "map:{ ", " }"));
    }

    static String convertMapToString(Map<Object, Object> map) {
        return convertMapToString(map.entrySet());
    }

    // ===================================================================================
    //                                                                           Good Luck
    //                                                                           =========
    /**
     * What string of toString() is converted from text of SecretBox class in upper space on the "white" color-box to java.util.Map? <br>
     * (whiteのカラーボックスのupperスペースに入っているSecretBoxクラスのtextをMapに変換してtoString()すると？)
     */
    public void test_parseMap_flat() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        for (ColorBox box : colorBoxList) {
            if (box instanceof StandardColorBox) {
                BoxSpace space = ((StandardColorBox) box).getUpperSpace();
                if (space != null && space.getContent() instanceof SecretBox) {
                    SecretBox sb = (SecretBox) space.getContent();
                    log(sb.getText());
                    log(convertStringToMap(sb.getText()));
                }
            }
        }
    }

    /**
     * What string of toString() is converted from text of SecretBox class in both middle and lower spaces on the "white" color-box to java.util.Map? <br>
     * (whiteのカラーボックスのmiddleおよびlowerスペースに入っているSecretBoxクラスのtextをMapに変換してtoString()すると？)
     */
    public void test_parseMap_nested() {
        List<ColorBox> colorBoxList = new YourPrivateRoom().getColorBoxList();
        for (ColorBox box : colorBoxList) {
            if (box instanceof StandardColorBox) {
                StandardColorBox scb = (StandardColorBox) box;
                if (scb.getMiddleSpace() != null && scb.getMiddleSpace().getContent() instanceof SecretBox) {
                    log(((SecretBox) scb.getMiddleSpace().getContent()).getText());
                    log(convertStringToMap(((SecretBox) scb.getMiddleSpace().getContent()).getText()));
                }
                if (scb.getLowerSpace() != null && scb.getLowerSpace().getContent() instanceof SecretBox) {
                    log(((SecretBox) scb.getLowerSpace().getContent()).getText());
                    log(convertStringToMap(((SecretBox) scb.getLowerSpace().getContent()).getText()));
                }
            }
        }
    }

    public void test_tom() {
        log(convertStringToMap("map:{ x = y ; k = q ; a = map:{ b = map:{ t = f ; poop = brown ; tea = tasty } } ; z = map:{ c = d } }"));
    }

    static Map<Object, Object> convertStringToMap(String str) {
        return convertStringToMap(str.split("\\{ | = | ; | }"));
    }

    static Map<Object, Object> convertStringToMap(String[] parts) {
        return convertStringToMap(parts, 0).getMap();
    }

    private static class ConversionResult {
        private Map<Object, Object> map;
        private int endIndex;

        public ConversionResult(Map<Object, Object> map, int endIndex) {
            this.map = map;
            this.endIndex = endIndex;
        }

        public Map<Object, Object> getMap() {
            return map;
        }

        public void setMap(Map<Object, Object> map) {
            this.map = map;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }
    }

    static ConversionResult convertStringToMap(String[] parts, int start) {
//        System.out.println("converting " + Arrays.toString(parts));
        Map<Object, Object> map = new HashMap<>();
        boolean started = false;
        boolean finished = false;
        Object key = null;

        for (int i = start; i < parts.length && !finished; i++) {
            Object item = null;
//            System.out.println("on " + i + ": " + parts[i]);
            if (parts[i].equals("map:")) { // start of a map
//                System.out.println("found map");
                if (!started) {
//                    System.out.println("starting");
                    started = true;
                } else {
//                    System.out.println("it's a sub-map");
                    ConversionResult result = convertStringToMap(parts, i);
                    item = result.getMap();
//                    System.out.println("sub-map: " + item);
//                    System.out.println("moving i from " + i + " to " + result.getEndIndex());
                    i = result.getEndIndex();
                    if (result.getEndIndex() == -1) {
                        finished = true;
                    }
                }
            } else if (parts[i].equals("") || parts[i].equals(" ")) { // end of a map
//                System.out.println("end of map that started at " + start);
                return new ConversionResult(map, i);
            } else {
                item = parts[i];
//                System.out.println("normal part " + item);
            }
            if (key == null) {
                key = item;
            } else {
//                System.out.println("putting " + key + " = " + item);
                map.put(key, item);
                key = null;
            }
        }

        return new ConversionResult(map, -1);
    }
}
