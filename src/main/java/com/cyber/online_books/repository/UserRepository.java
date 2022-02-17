package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Role;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.response.ConveterSummary;
import com.cyber.online_books.response.InfoSummary;
import com.cyber.online_books.response.TopConverter;
import com.cyber.online_books.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Tìm kiếm User theo username
     *
     * @param username
     * @return Optional<User>
     */
    User findUserByUsername(String username);

    /**
     * Tìm kiếm User theo email
     *
     * @param email
     * @return Optional<User>
     */
    User findUserByEmail(String email);

    /**
     * Tìm User theo id
     *
     * @param id
     * @return InfoSummary
     */
    Optional<InfoSummary> findUsersById(Long id);

    /**
     * Kiểm Tra Có tồn tại Display Name với điều kiện Khác userId không
     *
     * @param userId
     * @param newNick
     * @return true - nếu tồn tại user/ false - nếu không tồn tại user
     */
    boolean existsByIdNotAndDisplayName(Long userId, String newNick);

    /**
     * Lấy danh sách toàn bộ user theo phương thức paging
     *
     * @param pageable
     * @return Users - danh sách toàn bộ user/ null - nếu không có user nào
     */
    Page<User> findAll(Pageable pageable);

    @Query(value = ConstantsQueryUtils.TOP_CONVERTER,
            countQuery = ConstantsQueryUtils.COUNT_TOP_CONVERTER,
            nativeQuery = true)
    Page<TopConverter> getTopConverter(@Param("chapterStatus") List< Integer > listChapterStatus,
                                       @Param("storyStatus") List< Integer > listStoryStatus,
                                       @Param("userStatus") Integer uStatus, @Param("roleList") List< Integer > listRole, Pageable pageable);

    Page<User> findByRoleList(Role role, Pageable pageable);

    Page< User > findByUsernameContainingAndRoleList(String search, Role role, Pageable pageable);

    /**
     * Tìm User theo id
     *
     * @param id
     * @return ConveterSummary
     */
    ConveterSummary findUserById(Long id);
}
