package com.cyber.online_books.service.impl;

import com.cyber.online_books.component.MyComponent;
import com.cyber.online_books.domain.UserPrincipal;
import com.cyber.online_books.entity.Mail;
import com.cyber.online_books.entity.Role;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;
import com.cyber.online_books.response.ConveterSummary;
import com.cyber.online_books.response.InfoSummary;
import com.cyber.online_books.response.TopConverter;
import com.cyber.online_books.repository.RoleRepository;
import com.cyber.online_books.repository.UserRepository;
import com.cyber.online_books.response.UserAdmin;
import com.cyber.online_books.service.*;
import com.cyber.online_books.utils.*;

import static com.cyber.online_books.utils.UserImplContant.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
@Transactional
@PropertySource(ignoreResourceNotFound = true, value = "classpath:messages.properties",encoding = "UTF-8")
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private LoginAttemptService loginAttemptService;
    private EmailService emailService;
    private CloudinaryService cloudinaryService;
    private PayService payService;
    private BCryptPasswordEncoder passwordEncoder;
    @Value("${Cyber.truyenonline.email.from}")
    private String emailForm;
    @Value("${Cyber.truyenonline.email.display}")
    private String emailDisplay;
    @Value("${Cyber.truyenonline.email.subject}")
    private String emailSubject;
    @Value("${Cyber.truyenonline.email.signature}")
    private String emailSignature;
    @Value("${Cyber.truyenonline.email.url}")
    private String emailUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           LoginAttemptService loginAttemptService,
                           EmailService emailService,
                           CloudinaryService cloudinaryService,
                           PayService payService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.loginAttemptService = loginAttemptService;
        this.cloudinaryService = cloudinaryService;
        this.emailService = emailService;
        this.payService = payService;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private MyComponent myComponent;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(userName);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);

            List<GrantedAuthority> grantList = new ArrayList< GrantedAuthority >();
            if (user.getRoleList() != null) {
                for (Role urole : user.getRoleList()) {
                    GrantedAuthority authority = new SimpleGrantedAuthority(urole.getName());
                    grantList.add(authority);
                }
            }
            UserPrincipal userPrincipal = new UserPrincipal(user, grantList);
            LOGGER.info(FOUND_USER_BY_USERNAME + userName);
            return userPrincipal;
        }
    }

    /**
     * L???y to??n b??? User
     *
     * @return List<User>
     */
    @Override
    public Page< User > getUsers(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page< User > users = userRepository.findAll(paging);
        return users;
    }

    /**
     * T??m ki???m User theo username
     *
     * @param userName
     * @return User - N???u t???n t???i user v???i userName / null - n???u kh??ng t???n t???i user v???i userName
     */
    @Override
    public User findUserAccount(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    /**
     * T??m ki???m User theo username
     *
     * @param email
     * @return User - N???u t???n t???i user v???i email / null - n???u kh??ng t???n t???i user v???i userName
     */
    @Override
    public User findUserEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * T??m user theo Id
     *
     * @param id
     * @return User - n???u t???n t???i / null- n???u kh??ng t???n t???i user
     */
    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }

    /**
     * L???y Th??ng Tin Ng?????i d??ng Theo id
     *
     * @param id
     * @return
     */
    @Override
    public InfoSummary findInfoUserById(Long id) {
        return userRepository.findUsersById(id).orElse(null);
    }

    @Override
    public List<TopConverter> findTopConverter(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page< TopConverter > result = userRepository
                .getTopConverter(ConstantsListUtils.LIST_CHAPTER_DISPLAY,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsStatusUtils.USER_ACTIVED, ConstantsListUtils.LIST_ROLE_CON, pageable);
        return result.getContent();
    }

    /**
     * L???y Th??ng Tin Converter
     *
     * @param id
     * @return ConverterSummary
     */
    @Override
    public ConveterSummary findConverterByID(Long id) {
        return userRepository.findUserById(id);
    }

    /**
     * Ki???m tra DisplayName ???? t???n t???i ch??a
     *
     * @param userId
     * @param newNick
     * @return boolean
     */
    @Override
    public boolean checkUserDisplayNameExits(Long userId, String newNick) {
        return userRepository.existsByIdNotAndDisplayName(userId, newNick);
    }

    /**
     * C???p nh???t ngo???i hi???u
     *
     * @param newNick
     * @return User - n???u t???n t???i / null- n???u kh??ng t???n t???i user
     */
    @Override
    public User updateDisplayName(Principal principal, String newNick, Double money) throws HttpMyException, UserNotFoundException {
        User currentUser = validatePricipal(principal);
        if (newNick.equalsIgnoreCase(currentUser.getDisplayName()))
            throw new HttpMyException("Ngo???i hi???u n??y b???n ??ang s??? d???ng");
        if (userRepository.existsByIdNotAndDisplayName(currentUser.getId(), newNick))
            throw new HttpMyException("Ngo???i hi???u ???? t???n t???i!");
        if (currentUser.getGold() < money)
            throw new HttpMyException("S??? d?? c???a b???n kh??ng ????? ????? thanh to??n!");
        currentUser.setDisplayName(newNick);
        currentUser.setGold(currentUser.getGold() - money);
        userRepository.save(currentUser);
        return currentUser;
    }

    /**
     * C???p nh???t th??ng b??o
     *
     * @param newMess
     * @return User - n???u t???n t???i / null- n???u kh??ng t???n t???i user
     */
    @Override
    public User updateNotification(Principal principal, String newMess) throws UserNotFoundException {
        User currentUser = validatePricipal(principal);
        currentUser.setNotification(newMess);
        userRepository.save(currentUser);
        return currentUser;
    }

    @Override
    public User updateAvatar(Principal principal, MultipartFile sourceFile) throws UserNotFoundException, NotAnImageFileException {
        if (!Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE).contains(sourceFile.getContentType())) {
            throw new NotAnImageFileException(sourceFile.getOriginalFilename() + " is not an image file");
        }
        User currentUser = validatePricipal(principal);
        String url = cloudinaryService.uploadAvatar(sourceFile, currentUser.getUsername());
        currentUser.setAvatar(url);
        userRepository.save(currentUser);
        return currentUser;
    }

    /**
     * C???p Nh???t User
     *
     * @param user
     * @return User
     */
    @Override
    public User updateUser(User user) throws UserNotFoundException, UsernameExistException, EmailExistException{
        User editedUser = validateNewUsernameAndEmail(user.getUsername(), null, null);
        editedUser.setStatus(user.getStatus());
        editedUser.setRoleList(user.getRoleList());
        return editedUser;
    }

    @Override
    public void topUp(Double money, Long id, Principal principal) throws UserNotFoundException, HttpMyException, UserNotLoginException {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User currentUser = validatePricipal(principal);
        User receivedUser = findUserById(id);
        if(receivedUser == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        }
        if (WebUtils.checkMoney(money))
            throw new HttpMyException("S??? ?????u n???p ph???i l???n h??n 0!");
        try {
            payService.savePayChange(currentUser, money, receivedUser);
        } catch (Exception e) {
            throw new HttpMyException("Ch??a th??? n???p ?????u! Vui l??ng th???c hi???n l???i sau");
        }
    }

    @Override
    public void deleteUser(Principal principal, Long id) throws HttpMyException, IOException, UserNotFoundException, UserNotLoginException {
        if (principal == null) {
           throw new UserNotLoginException();
        }

        User currentUser = validatePricipal(principal);
        User deletedUser = findUserById(id);
        if(currentUser.getId() == id){
            throw new HttpMyException("Kh??ng th??? delete t??i kho???n hi???n t???i c???a b???n");
        }
        if (deletedUser == null) {
            throw new HttpMyException("T??i kho???n kh??ng t???n t???i m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        boolean checkAdminLogin = myComponent.hasRole(currentUser, ConstantsUtils.ROLE_ADMIN);
        boolean checkAdminUser = myComponent.hasRole(deletedUser, ConstantsUtils.ROLE_ADMIN);
        boolean checkModLogin = myComponent.hasRole(currentUser, ConstantsUtils.ROLE_SMOD);
        boolean checkModnUser = myComponent.hasRole(deletedUser, ConstantsUtils.ROLE_SMOD);
        if ((checkAdminLogin == true && checkAdminUser == false) || (checkModLogin == true && checkModnUser == false && checkAdminUser == false)) {
            //Admin can not delete other Admins, Mod cannot delete Admin and other Mods
            try {
                cloudinaryService.delete(deletedUser.getUsername());
                userRepository.delete(deletedUser);
            } catch (Exception e) {
                throw new HttpMyException("Kh??ng Th??? X??a Ng?????i D??ng N??y");
            }
        } else
            throw new HttpMyException("B???n kh??ng ????? quy???n x??a ng?????i d??ng n??y");
    }

    @Override
    public Page< User > findByType(String search, Integer type, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        Role role = roleRepository.findById(type).orElse(null);
        if (search.trim().length() != 0)
            return userRepository.findByUsernameContainingAndRoleList(search, role, pageable);
        return userRepository.findByRoleList(role, pageable);

    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * ????ng k?? ng?????i d??ng m???i
     *
     * @param user
     * @return true - n???u ????ng k?? th??nh c??ng / false - n???u c?? l???i x???y ra
     */
    @Override
    @org.springframework.transaction.annotation.Transactional
    public User registerUser(User user) throws UserNotFoundException, UsernameExistException, EmailExistException, HttpMyException {
        validateNewUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());
        String password = generatePassword();
        Set<Role> roleList = new HashSet<>();
        Role role = roleRepository.findById(ConstantsRoleUtils.ROLE_USER_ID).get();
        roleList.add(role);
        user.setRoleList(roleList);
        user.setPassword(encodePassword(password));
        user.setUserId(generateUserId());
        user.setCreateDate(new Date());
        user.setNotLocked(true);
        User newUser = userRepository.save(user);
        if (sendMail(user, password, "mail/new-user")) {
            return newUser;
        } else {
            throw new HttpMyException("C?? l???i x???y ra trong qu?? tr??nh g???i mail ????ng k??, vui l??ng quay l???i sau!");
        }
    }

    /**
     * Qu??n m???t kh???u
     *
     * @param email
     */
    @Override
    public void resetPassword(String email) throws HttpMyException, EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        LOGGER.info("New user password: " + password);
        if (!sendMail(user, password, "mail/forgot-password")) {
            throw new HttpMyException("C?? l???i x???y ra trong qu?? tr??nh g???i mail ????ng k??, vui l??ng quay l???i sau!");
        }
    }

    /**
     * C???p nh???t m???t kh???u
     *
     * @param newPassword
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword, Principal principal) throws UserNotFoundException, HttpMyException {
        User currentUser = validatePricipal(principal);
        if (oldPassword != null && !WebUtils.equalsPassword(oldPassword, currentUser.getPassword())) {
            throw new HttpMyException("M???t kh???u ????ng nh???p c?? kh??ng ch??nh x??c");
        }
        currentUser.setPassword(encodePassword(newPassword));
        userRepository.save(currentUser);
        LOGGER.info("New user password: " + newPassword);
    }

    /**
     * @param date
     * @return
     */
    @Override
    public Long countUserNewInDate(Date date) {
        return userRepository.countByCreateDateGreaterThanEqual(date);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private User validatePricipal(Principal principal) throws UserNotFoundException {
        String currentUsername = principal.getName();
        User currentUser = findUserAccount(currentUsername);
        if(currentUser == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        }
        if (currentUser.getStatus() != 1){
            throw new DisabledException("T??i kho???n ???? b??? ????ng ho???c ch??a ???????c k??ch ho???t");
        }
        return currentUser;
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserAccount(newUsername);
        User userByNewEmail = findUserEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserAccount(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewUsername != null) {
                throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    private boolean sendMail(User user, String newPassword, String template) {
        Mail mail = new Mail();
        mail.setFrom(emailForm);
        mail.setTo(user.getEmail());
        mail.setSubject(emailSubject);
        mail.setFromDisplay(emailDisplay);
        Map< String, Object > modelMap = new HashMap< String, Object >();
        modelMap.put("name", user.getDisplayName() != null ? user.getDisplayName() : user.getUsername());
        modelMap.put("url", emailUrl);
        modelMap.put("signature", emailSignature);
        modelMap.put("password", newPassword);
        mail.setModel(modelMap);
        return emailService.sendSimpleMessage(mail, template);
    }
}
