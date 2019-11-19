package com.tencent.ui;

public class PermissonDeniedException extends Exception {
    public PermissonDeniedException(String no_permisson) {
        super(no_permisson);
    }
}
