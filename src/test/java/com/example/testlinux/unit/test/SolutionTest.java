package com.example.testlinux.unit.test;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Slf4j
public class SolutionTest {
    public boolean isPalindrome(int x) {
        if (x < 0 || (x != 0 && x % 10 == 0)) {
            return false;
        }

        int reversed_num = 0;

        while (x > reversed_num) {
            reversed_num = reversed_num * 10 + (x % 10);
            x /= 10;
        }

        return x == reversed_num || x == reversed_num / 10;
    }

    public boolean isPalindrome2(int x) {
        int log10 = (int) Math.log10(x);

        while (log10 > 0) {
            int firstDigit = (int) (x / (Math.pow(10, log10)));
            int lastDigit = x - (x / 10) * 10;
            if (firstDigit != lastDigit) {
                return false;
            }
            x = (x - firstDigit * (int) (Math.pow(10, log10))) / 10;
            log10 -= 2;
        }

        return true;
    }

    public int romanToInt(String s) {
        Map<Character, Integer> romanDigits = Map.of(
                'I', 1,
                'V', 5,
                'X', 10,
                'L', 50,
                'C', 100,
                'D', 500,
                'M', 1000
        );

        int resultValue = 0;

        for (int i = 0; i < s.length(); i++) {
            char c1 = s.charAt(i);
            int intByChar1 = romanDigits.get(c1);

            if (i + 1 < s.length()) {
                char c2 = s.charAt(i + 1);
                int intByChar2 = romanDigits.get(c2);
                // проверяем вычитает ли следующее число предыдущее
                if (intByChar1 < intByChar2) {
                    resultValue += (intByChar2 - intByChar1);
                    i++;
                } else {
                    resultValue += intByChar1;
                }
            } else {
                resultValue += intByChar1;
            }
        }

        return resultValue;
    }

    public String longestCommonPrefix(String[] strs) {
        StringBuilder findString = new StringBuilder();
        String charCommon = "";
        int indexBegin = 0;
        int indexEnd = 1;
        String str1 = strs[0];
        while (indexEnd <= str1.length()) {
            charCommon = str1.substring(indexBegin, indexBegin + 1);
            boolean charCommonIsFound = true;
            for (int j = 1; j < strs.length; j++) {
                if (!strs[j].contains(charCommon)) {
                    charCommonIsFound = false;
                    break;
                }
            }
            if (charCommonIsFound) {
                findString.append(charCommon);
            } else {
                if (!findString.isEmpty()) {
                    return findString.toString();
                }

            }
            indexBegin++;
        }

        return findString.toString();
    }

    public boolean isValidMy(String s) {
        Map<Character, Boolean> letters = Map.of(
                '(', false,
                '[', false,
                '{', false,
                ')', false,
                ']', false,
                '}', false
        );
        List<Character> openLetters = List.of(
                '(',
                '[',
                '{'
        );
        List<Character> closeLetters = List.of(
                ')',
                ']',
                '}'
        );

        int countOfOpenLetters = 0;
        for (int i = 0; i < s.length(); i++) {
            if (openLetters.contains(s.charAt(i))){
                if (letters.get(s.charAt(i)).equals(false)){
                    if (++countOfOpenLetters > 3){
                        return false;
                    }
                }else {
                    if (--countOfOpenLetters < 0){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {

            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                        (c == '}' && top != '{') ||
                        (c == ']' && top != '[')) {
                    return false; // Mismatched brackets
                }
            }
        }

        return stack.isEmpty();
    }
    /*public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if(list1 == null || list2 == null) {
            return new ListNode();
        }

        if (list1.getVal() > list2.getVal()) {

        }

        while () {

        }

        if (list1.getNext() != null) {
            if (list1.getVal() > list2.getVal()) {

            }
        }
    }*/

    public static class ListNode{
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

         ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }

        if (list1 != null) {
            current.next = list1;
        } else if (list2 != null) {
            current.next = list2;
        }

        return dummy.next;
    }
}
