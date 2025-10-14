package com.example.testlinux.unit.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class SolutionTest {
    public static void main(String[] args) {
        /*FileDescriptor fd = null;

        try {
            // Создание объекта FileOutputStream для записи в файл
            FileOutputStream fileOutputStream = new FileOutputStream(new File("example.txt"));
            // Получение файлового дескриптора
            fd = fileOutputStream.getFD();
            System.out.println("File descriptor for example.txt: " + fd.hashCode());

            // Запись данных в файл
            String data = "Hello, FileDescriptor!";
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close(); // Закрытие потока

            // Создание объекта FileInputStream для чтения из файла
            FileInputStream fileInputStream = new FileInputStream(new File("example.txt"));
            // Получение файлового дескриптора
            FileDescriptor fd2 = fileInputStream.getFD();
            System.out.println("File descriptor for example.txt: " + fd2.hashCode());

            // Чтение данных из файла
            int i;
            while ((i = fileInputStream.read()) != -1) {
                System.out.print((char) i);
            }
            fileInputStream.close(); // Закрытие потока

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fd != null && fd.valid()) {
                System.out.println("File descriptor is valid.");
            } else {
                System.out.println("File descriptor is invalid.");
            }
        }*/
    }

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
            if (openLetters.contains(s.charAt(i))) {
                if (letters.get(s.charAt(i)).equals(false)) {
                    if (++countOfOpenLetters > 3) {
                        return false;
                    }
                } else {
                    if (--countOfOpenLetters < 0) {
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


    public class ListNode {
        int val;
        ListNode next = null;

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

    public int removeDuplicates(int[] nums) {
        int length = nums.length;
        int indexCurrent = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[indexCurrent] != nums[i]) {
                nums[++indexCurrent] = nums[i];
            }
        }
        nums = Arrays.copyOf(nums, indexCurrent + 1);

        return length;
    }

    public int removeElement(int[] nums, int val) {
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[j] = nums[i];
                j++;
            }
        }
        return j;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode node1 = l1;
        ListNode node2 = l2;
        int sum = 0;
        while (node1 != null && node2 != null) {
            sum = node1.val + node2.val;
            if (sum >= 10) {
                node1.val = sum % 10;
                node2.val = sum % 10;

                if (node1.next != null) {
                    node1.next.val++;
                } else if (node2.next != null) {
                    node2.next.val++;
                } else {
                    node1.next = new ListNode(1, null);
                    return l1;
                }

            } else {
                node1.val = sum;
                node2.val = sum;
            }

            node1 = node1.next;
            node2 = node2.next;
        }

        ListNode nodeRemainder = node1;
        if (node1 == null) {
            nodeRemainder = node2;
        }

        while (nodeRemainder != null) {
            if (nodeRemainder.val >= 10) {
                nodeRemainder.val = nodeRemainder.val % 10;
                if (nodeRemainder.next != null) {
                    nodeRemainder.next.val++;
                } else {
                    nodeRemainder.next = new ListNode(1, null);
                }
            } else {
                break;
            }
            nodeRemainder = nodeRemainder.next;
        }

        if (node1 == null) {
            return l2;
        }

        return l1;
    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode tail = dummyHead;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int digit1 = (l1 != null) ? l1.val : 0;
            int digit2 = (l2 != null) ? l2.val : 0;

            int sum = digit1 + digit2 + carry;
            int digit = sum % 10;
            carry = sum / 10;

            ListNode newNode = new ListNode(digit);
            tail.next = newNode;
            tail = tail.next;

            l1 = (l1 != null) ? l1.next : null;
            l2 = (l2 != null) ? l2.next : null;
        }

        ListNode result = dummyHead.next;
        dummyHead.next = null;
        return result;
    }

    public int strStr(String haystack, String needle) {
        int indexNeedleBegin = -1;

        int indexFirstLetterOfHaystack = -1;
        int indexNeedle = 0;

        for (int i = 0; i < haystack.length(); i++) {
            if (needle.charAt(0) == haystack.charAt(i) && indexNeedle > 0 && indexFirstLetterOfHaystack == -1) {
                indexFirstLetterOfHaystack = i;
            }

            if (needle.charAt(indexNeedle) == haystack.charAt(i)) {
                if (indexNeedle < needle.length()) {
                    indexNeedle++;
                }
                if (indexNeedle == needle.length()) {
                    indexNeedleBegin = i + 1 - indexNeedle;
                    return indexNeedleBegin;
                }
            } else {
                if (indexFirstLetterOfHaystack != -1) {
                    i = indexFirstLetterOfHaystack - 1;
                    indexFirstLetterOfHaystack = -1;
                }
                indexNeedle = 0;
            }

        }

        return indexNeedleBegin;
    }

    public String addBinary(String a, String b) {
        StringBuilder sumBinary = new StringBuilder();

        int carry = 0;
        int i = 1;
        while (carry != 0 || i <= a.length() || i <= b.length()) {
            int digit1 = (i <= a.length()) ? Character.getNumericValue(a.charAt(a.length() - i)) : 0;
            int digit2 = (i <= b.length()) ? Character.getNumericValue(b.charAt(b.length() - i)) : 0;

            int sum = digit1 + digit2 + carry;

            sumBinary.append(sum % 2);
            carry = (sum >= 2) ? 1 : 0;
            i++;
        }

        return sumBinary.reverse().toString();
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    private List<Integer> result = new ArrayList<>();

    public void inorderTraversal() {
        TreeNode root = new TreeNode(1, null, new TreeNode(2, new TreeNode(3), null));
        inorderTraversal(root);
        System.out.println("result; " + result);
    }

    private Integer inorderTraversal(TreeNode root) {
        if (root != null) {
            inorderTraversal(root.left);
            result.add(root.val);
            inorderTraversal(root.right);
        }

        return 0;
    }

    public List<Integer> inorderTraversalN(TreeNode root) {
        //Когда вставляется значение в лист, то нод удаляется из стека и становится нал

        //1. если левый нод не нал,то текущий нод вставляется в конец стека нодов и становится следующим левым нодом , continue - нет вставки в лист
        //2. если левый нод нал, то а) вставка значения текущего нода в лист , б) получение правого и левого нодов в) если размер стека > 1 то получение предыдущего значения нода,обнудение через него текущего нода и удаление теущего нода из конца стека нодов  - есть вставка в лист
        //3. если правый нод не нал, то вставка правого нода в конец стека и присваевание текущего нода правому continue
        //4. если правый нод - нал, то текущий нод равен последнему значению стека

        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        stack.add(root);

        while(!stack.isEmpty() && root != null) {
            TreeNode nodeCurrent = stack.get(stack.size() - 1);
            if (nodeCurrent.left != null) {
                stack.add(nodeCurrent.left);
                continue;
            }

            result.add(nodeCurrent.val);

            if (stack.size() > 1) {
                TreeNode nodePrevious = stack.get(stack.size() - 2);
                nodePrevious.left = null;
            }

            stack.remove(nodeCurrent);

            if (nodeCurrent.right != null) {
                stack.add(nodeCurrent.right);
            }
        }

        return result;
    }

    public List<Integer> inorderTraversalStack(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }

            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }

        return res;
    }

    public boolean isSymmetric(TreeNode root) {
        TreeNode leftNode = root.left;
        TreeNode rightNode = root.right;

        Stack<TreeNode> stackLeft = new Stack<>();
        Stack<TreeNode> stackRight = new Stack<>();

        stackLeft.push(leftNode);
        stackRight.push(rightNode);

        while (!stackLeft.isEmpty()) {
            while (leftNode != null) {
                if (rightNode == null) {
                    return false;
                }
                if (leftNode.val != rightNode.val) {
                    return false;
                }
                if (leftNode.right != null && rightNode.left == null || leftNode.right == null && rightNode.left != null) {
                    return false;
                }
                if (leftNode.right != null) {
                    stackLeft.push(leftNode.right);
                }
                if (rightNode.left != null) {
                    stackRight.push(rightNode.left);
                }
                leftNode = leftNode.left;
                rightNode = rightNode.right;
            }
            //если  правая ветка длиннее левой
            if (leftNode == null && rightNode != null) {
                return false;
            }

            leftNode = stackLeft.pop();
            rightNode = stackRight.pop();
        }
        return true;
    }

    public boolean isSymmetricRecursively(TreeNode root) {
        return isMirror(root.left, root.right);
    }

    private boolean isMirror(TreeNode node1, TreeNode node2) {
        if (node1 != null && node2 != null) {
            if (node1.val != node2.val) {
                return false;
            }

            if (isMirror(node1.left, node2.right)){
                return isMirror(node1.right, node2.left);
            }

        }

        return node1 == node2;
    }


    public boolean isSymmetric3(TreeNode root) {
        List<TreeNode> nodesHorizont = new ArrayList<>();// List<TreeNode> nodesHorizont = new ArrayList<>(List.of(root.left, root.right));
        nodesHorizont.add(root.left);
        nodesHorizont.add(root.right);
        boolean nodeExist = true;
        int iBegin = 0;
        int iEnd = 2;

        while (nodeExist) {
            nodeExist = false;
            iEnd = nodesHorizont.size();

            for (int i = iBegin; i < nodesHorizont.size(); i += 2){

                if (nodesHorizont.get(i) != null && nodesHorizont.get(i + 1) != null) {
                    nodeExist = true;
                    if (nodesHorizont.get(i).val != nodesHorizont.get(i + 1).val) {
                        return false;
                    }
                    if (nodesHorizont.get(i).left != null && nodesHorizont.get(i + 1).right != null) {
                        nodesHorizont.add(nodesHorizont.get(i).left);
                        nodesHorizont.add(nodesHorizont.get(i + 1).right);
                    }else if (nodesHorizont.get(i).left != nodesHorizont.get(i + 1).right) {
                        return false;
                    }
                    if (nodesHorizont.get(i).right != null && nodesHorizont.get(i + 1).left != null) {
                        nodesHorizont.add(nodesHorizont.get(i).right);
                        nodesHorizont.add(nodesHorizont.get(i + 1).left);
                    }else if (nodesHorizont.get(i).right != nodesHorizont.get(i + 1).left) {
                        return false;
                    }
                }else if (nodesHorizont.get(i) == null && nodesHorizont.get(i + 1) != null ||
                        nodesHorizont.get(i) != null && nodesHorizont.get(i + 1) == null) {
                    return false;
                }

            }
            iBegin = iEnd;
        }

        return true;
    }

    public boolean isSymmetric75(TreeNode root) {
        if (root == null) {
            return true; // An empty tree is symmetric
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root.left);
        queue.offer(root.right);

        while (!queue.isEmpty()) {
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();

            // Both nodes are null, continue to next pair
            if (left == null && right == null) {
                continue;
            }
            // One of the nodes is null, or values are different
            if (left == null || right == null || left.val != right.val) {
                return false; // Not symmetric
            }

            // Add children in reverse order for symmetry checking
            queue.offer(left.left);
            queue.offer(right.right);
            queue.offer(left.right);
            queue.offer(right.left);
        }

        return true; // All checks passed, tree is symmetric
    }

    @Test
    public void test1() {
        int[] nums = new int[]{3,2,1,8,6};
        Map<Integer, Integer> mapOfNums = IntStream.range(0, nums.length)
                .boxed()
                .sorted(Comparator.comparingInt(i -> nums[i]))
                .collect(Collectors.toMap(i -> i, i -> nums[i], (a, b) -> a, LinkedHashMap::new));

        mapOfNums.values().forEach(System.out::println);
    }


    public boolean isSymmetric123(TreeNode root) {
        if (root == null) {
            return true; // An empty tree is symmetric
        }

        Queue<TreeNode> queue = new LinkedList<>(List.of(root.left, root.right));

        while (!queue.isEmpty()) {
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();

            // Both nodes are null, continue to next pair
            if (left == null && right == null) {
                continue;
            }
            // One of the nodes is null, or values are different
            if (left == null || right == null || left.val != right.val) {
                return false; // Not symmetric
            }

            // Add children in reverse order for symmetry checking
            queue.offer(left.left);
            queue.offer(right.right);
            queue.offer(left.right);
            queue.offer(right.left);
        }

        return true; // All checks passed, tree is symmetric
    }

    @Test
    public void test2() {
        int[] nums = new int[]{3,2,1,8,6};
        Map<Integer, Integer> mapOfNums = IntStream.range(0, nums.length)
                .boxed()
                .sorted(Comparator.comparingInt(i -> nums[i]))
                .collect(Collectors.toMap(i -> i, i -> nums[i], (a, b) -> a, LinkedHashMap::new));

        mapOfNums.values().forEach(System.out::println);
    }

}
