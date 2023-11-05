package com.auth.service;

import com.auth.dto.UserEntityDto;

import java.util.List;

public interface UserEntityService {

    UserEntityDto createUser(UserEntityDto userEntityDto, int roleId);

    UserEntityDto getUserById(int userId);

    List<UserEntityDto> getAllUsers();

}
