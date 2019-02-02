package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException{

      String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
      userEntity.setSalt(encryptedText[0]);
      userEntity.setPassword(encryptedText[1]);

      if(userDao.getUserByName((userEntity.getUsername())) !=null){
          throw new SignUpRestrictedException("SGR-001" , "Try any other Username, this Username has already been taken" );
      }else if(userDao.getUserByEmail(userEntity.getEmail()) != null){
          throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
      }
      return userDao.createUser(userEntity);
    }

}
