package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
@RestController
@CrossOrigin
@RequestMapping("/profile")
public class ProfileController {

    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    public ProfileDao getProfileDao() {
        return profileDao;
    }

    public void setProfileDao(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @GetMapping("")
    public Profile getProfile(Principal principal) {

        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        return profileDao.getByUserId(userId);
    }

    @PutMapping("")
    public void update(Principal principal, @RequestBody Profile profile) {

        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        profileDao.update(userId, profile);
    }
}
