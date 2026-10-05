package com.howtodoinjava.interview.modern;

public sealed interface Payment permits Card, Cash, Voucher {}
