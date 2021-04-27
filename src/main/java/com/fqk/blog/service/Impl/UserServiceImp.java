package com.fqk.blog.service.Impl;

import com.fqk.blog.dao.UserRepository;
import com.fqk.blog.pojo.User;
import com.fqk.blog.service.UserService;
import com.fqk.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

        return user;
    }
}
