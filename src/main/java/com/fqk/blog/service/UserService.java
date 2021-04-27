package com.fqk.blog.service;

import com.fqk.blog.pojo.User;

public interface UserService {

    User checkUser(String username,String password);
}
