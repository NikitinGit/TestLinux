package com.example.testlinux.unit.test;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.service.UserService;


import com.example.testlinux.service.type.extend.TestExtendsMethodTypes;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application.properties") //not working
//@TestPropertySource(locations = "classpath:application.properties")
@Slf4j
public class UnitTest {

    @InjectMocks
    private UserService userService;

    private SolutionTest solutionTest = new SolutionTest();

    @Test
    void sum(){
        assertEquals(8, userService.getSum(5, 3));
        userService.getUsers();
    }

    @Test
    void addUser() {
        userService.addUser(new UserNew("Nikitin 2","nikitin2.@gmail.com"));
        userService.getUsers();
    }

    @Test
    void getValuesOfResponseITWORK(){
        try {
            String url = "https://api.yookassa.ru/v3/payments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            String auth = "465439:test_Jr8CqgCR_H777fgC41j4SSfmZM5GFpVXEuAHMSTqgxI";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes("UTF-8"));
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            con.setRequestProperty("Idempotence-Key", UUID.randomUUID().toString());

            String jsonInputString = "{\"amount\":{\"value\":\"1000.00\",\"currency\":\"RUB\"},"
                    + "\"capture\":true,"
                    + "\"confirmation\":{\"type\":\"redirect\",\"return_url\":\"https://example.com/success\"},"
                    + "\"description\":\"Заказ №1\"}";

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in;
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void isPalindrome(){
        solutionTest.isPalindrome(1357897531);
    }
    @Test
    void romanToInt(){
        solutionTest.romanToInt("III");
    }

    @Test
    void longestCommonPrefix(){
        String [] names = new String[]{"flower","flow","flight"};
        log.info("solutionTest.longestCommonPrefix(names); {}", solutionTest.longestCommonPrefix(names));
    }

    @Test
    void isValid(String s) {
        log.info("isValid: {}", solutionTest.isValid("[]"));
    }

    @Test
    void mergeTwoLists(){
        /*SolutionTest.ListNode list1 = new SolutionTest.ListNode(1, new SolutionTest.ListNode(2, new SolutionTest.ListNode(4)));

        // Create second linked list: 1 -> 3 -> 4
        SolutionTest.ListNode list2 = new SolutionTest.ListNode(1, new SolutionTest.ListNode(3, new SolutionTest.ListNode(4)));

        SolutionTest.ListNode mergedList = solutionTest.mergeTwoLists(list1, list2);

        // Print merged linked list
        while (mergedList != null) {
            System.out.print(mergedList.val + " ");
            mergedList = mergedList.next;
        }*/
    }

    @Test
    void removeDuplicates(){
        int nums[] = {1,1,2};
        solutionTest.removeDuplicates(nums);
    }

    @Test
    void removeElement(){
        int nums[] = {0,1,2,2,3,0,4,2};
        solutionTest.removeElement(nums, 2);
        float f =  100000.0f + 0.3f - 100000.0f;
        System.out.println(f); // prints 0.296875

        double d = 100000.0  + 0.3  - 100000.0;
        System.out.println(d); // prints 0.3000000000029104
    }

    @Test
    void addTwoNumbers(){
        //solutionTest.addTwoNumbers(); // prints 0.3000000000029104
    }
    @Test
    void addBinary(){
        System.out.println(solutionTest.addBinary("1010","1011"));
    }

    @Test
    void testGeneric(){
        TestExtendsMethodTypes testExtendsMethodTypes = new TestExtendsMethodTypes();
        testExtendsMethodTypes.someType(true);
    }

    @Test
    void testInorderTraversal(){
        solutionTest.inorderTraversal();
    }

    @Test
    void testRemainder(){
        int i = 170;
        int max = 169;
        int remainsder = i % max;
        log.info("remainder: {}", remainsder);
    }
}
