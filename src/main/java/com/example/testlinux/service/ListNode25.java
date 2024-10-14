package com.example.testlinux.service;

import lombok.Data;

@Data
public class ListNode25 {
    int val;
    ListNode25 next;

    public ListNode25() {
    }

    public ListNode25(int val) {
        this.val = val;
    }

    public ListNode25(int val, ListNode25 next) {
        this.val = val;
        this.next = next;
    }
}
