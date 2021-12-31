package com.cyber.online_books.service.impl;

import com.cyber.online_books.domain.UserPrincipal;
import com.cyber.online_books.entity.Mail;
import com.cyber.online_books.entity.Role;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;
import com.cyber.online_books.repository.RoleRepository;
import com.cyber.online_books.repository.UserRepository;
import com.cyber.online_books.service.EmailService;
import com.cyber.online_books.service.LoginAttemptService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsRoleUtils;
import static com.cyber.online_books.utils.UserImplContant.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.cyber.online_books.utils.ConstantsUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, LoginAttemptService loginAttemptService
            ,EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(userName);
        if (user == null) {
            LOGGER.error(NO_USER_FOUND_BY_USERNAME + userName);
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + userName);
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
     * Tìm kiếm User theo username
     *
     * @param userName
     * @return User - Nếu tồn tại user với userName / null - nếu không tồn tại user với userName
     */
    @Override
    public User findUserAccount(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    /**
     * Tìm kiếm User theo username
     *
     * @param email
     * @return User - Nếu tồn tại user với email / null - nếu không tồn tại user với userName
     */
    @Override
    public User findUserEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * Tìm user theo Id
     *
     * @param id
     * @return User - nếu tồn tại / null- nếu không tồn tại user
     */
    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }

    /**
     * Kiểm tra DisplayName đã tồn tại chưa
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
     * Cập nhật ngoại hiệu
     *
     * @param newNick
     */
    @Override
    public User updateDisplayName(Principal principal, String newNick) throws HttpMyException {
        String currentUsername = principal.getName();
        User currentUser = userRepository.findUserByUsername(currentUsername);
        if (currentUser == null)
            throw new HttpMyException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        if (newNick.equalsIgnoreCase(currentUser.getDisplayName()))
            throw new HttpMyException("Ngoại hiệu này bạn đang sử dụng");
        if (userRepository.existsByIdNotAndDisplayName(currentUser.getId(), newNick))
            throw new HttpMyException("Ngoại hiệu đã tồn tại!");
        Double money = Double.valueOf(0);
        if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
            //Không trừ đậu nếu chưa có ngoại hiệu
            money = ConstantsUtils.PRICE_UPDATE_NICK;
            if (currentUser.getGold() < money)
                throw new HttpMyException("Số dư của bạn không đủ để thanh toán!");
        }
        currentUser.setDisplayName(newNick);
        currentUser.setGold(currentUser.getGold() - money);
        userRepository.save(currentUser);
        return currentUser;
    }

    /**
     * Cập Nhật User
     *
     * @param user
     * @return User
     */
    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User deleteUser) {
        userRepository.delete(deleteUser);
    }

    /**
     * Đăng ký người dùng mới
     *
     * @param user
     * @return true - nếu đăng ký thành công / false - nếu có lỗi xảy ra
     */
    @Override
    @org.springframework.transaction.annotation.Transactional
    public User registerUser(User user) throws UserNotFoundException, UsernameExistException, EmailExistException, HttpMyException {
        validateNewUsernameAndEmail(EMPTY, user.getUsername(), user.getEmail());
        String password = generatePassword();
        List<Role> roleList = new ArrayList<>();
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
            throw new HttpMyException("Có lỗi xảy ra trong quá trình gửi mail đăng ký, vui lòng quay lại sau!");
        }
    }

    /**
     * Quên mật khẩu
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
            throw new HttpMyException("Có lỗi xảy ra trong quá trình gửi mail đăng ký, vui lòng quay lại sau!");
        }
    }

    /**
     * Cập nhật mật khẩu
     *
     * @param newPassword
     */
    @Override
    public void updatePassword(String newPassword, Principal principal) throws HttpMyException {
        String currentUsername = principal.getName();
        User currentUser = userRepository.findUserByUsername(currentUsername);
        if (currentUser == null) {
            throw new HttpMyException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        currentUser.setPassword(encodePassword(newPassword));
        userRepository.save(currentUser);
        LOGGER.info("New user password: " + newPassword);
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

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User userByNewUsername = findUserAccount(newUsername);
        User userByNewEmail = findUserEmail(newEmail);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserAccount(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
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
