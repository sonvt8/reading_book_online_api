package com.cyber.online_books.utils;

public class ConstantsQueryUtils {

    public static final String STORY_NEW_UPDATE_BY_STATUS = "SELECT s.id, s.name, s.images, s.author, s.updateDate,"
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_STORY_NEW_UPDATE_BY_STATUS = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_NEW_UPDATE_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.author, s.updateDate,"
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " WHERE  sc.categoryId = :categoryId AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_STORY_NEW_UPDATE_BY_CATEGORY = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " WHERE  sc.categoryId = :categoryId AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_TOP_VIEW_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.infomation, s.dealStatus, s.author,"
            + " COALESCE(d.countTopView,0) AS cnt, ca.id as categoryId, ca.name as categoryName FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN `history` f ON  c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.categoryId = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC";

    public static final String COUNT_STORY_TOP_VIEW_BY_CATEGORY = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countView,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN `history` f ON  c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.categoryId = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC) rs";

    public static final String STORY_TOP_VOTE_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.infomation, s.dealStatus, s.author,"
            + " COALESCE(d.countVote,0) AS cnt, ca.id as categoryId, ca.name as categoryName FROM Story s"
            + " LEFT JOIN (SELECT p.storyId, COALESCE(SUM(p.vote), 0) AS countVote FROM Pay p"
            + " WHERE p.createDate BETWEEN :startDate AND :endDate "
            + " AND p.type = :payType "
            + " AND p.status = :payStatus"
            + " GROUP BY p.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.categoryId = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.status, s.countAppoint DESC";

    public static final String COUNT_STORY_TOP_VOTE_BY_CATEGORY = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countVote,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT p.storyId, COALESCE(SUM(p.vote), 0) AS countVote FROM Pay p"
            + " WHERE p.createDate BETWEEN :startDate AND :endDate "
            + " AND p.type = :payType "
            + " AND p.status = :payStatus"
            + " GROUP BY p.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.categoryId = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.status, s.countAppoint DESC) rs";

    public static final String SEARCH_STORY = "SELECT s.id, s.name, s.images, s.author, s.updateDate, "
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterId FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId"
            + " WHERE c.status IN :chapterStatus"
            + " AND LOWER(s.name) LIKE %:search%"
            + " AND s.status IN :storyStatus"
            + " GROUP BY s.id) d"
            + " ON c.id = d.maxChapterId ) c "
            + " ON s.id = c.storyId "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " AND LOWER(s.name) LIKE %:search%"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_SEARCH_STORY = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterId FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId"
            + " WHERE c.status IN :chapterStatus"
            + " AND LOWER(s.name) LIKE %:search%"
            + " AND s.status IN :storyStatus"
            + " GROUP BY s.id) d"
            + " ON c.id = d.maxChapterId ) c "
            + " ON s.id = c.storyId "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " AND LOWER(s.name) LIKE %:search%"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_TOP_VIEW_BY_STATUS = "SELECT s.id, s.name, s.images, s.author, s.infomation, s.dealStatus, "
            + " COALESCE(d.countTopView,0) AS cnt, ca.id as categoryId, ca.name as categoryName"
            + " FROM Story s "
            + " LEFT JOIN (SELECT c.id as chapterId, c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN History f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC";

    public static final String COUNT_STORY_TOP_VIEW_BY_STATUS = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countTopView,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.storyId, COUNT(c.storyId) AS countTopView FROM Chapter c"
            + " LEFT JOIN History f ON c.id = f.chapterId"
            + " LEFT JOIN Story st on c.storyId = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.dateView BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.storyId) d ON s.id = d.storyId"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.countView DESC) rs";

    public static final String CHAPTER_HEAD = "SELECT ch.* FROM Chapter ch"
            + " WHERE ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial ASC"
            + " LIMIT 1";

    public static final String CHAPTER_NEW = "SELECT ch.* FROM chapter ch"
            + " WHERE ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial DESC"
            + " LIMIT 1";

    public static final String NEXT_CHAPTER = "SELECT ch.id FROM chapter ch"
            + " WHERE ch.serial > :chapterSerial"
            + " AND ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial ASC"
            + " LIMIT 1";

    public static final String PREVIOUS_CHAPTER = "SELECT ch.id FROM chapter ch"
            + " WHERE ch.serial < :chapterSerial"
            + " AND ch.storyId = :storyId"
            + " AND ch.status IN :chapterStatus"
            + " ORDER BY ch.serial DESC"
            + " LIMIT 1";

    public static final String VIP_STORY_NEW_UPDATE = "SELECT s.id, s.name, s.images, s.author, s.updateDate, s.infomation,"
            + " c.id as chapterId, c.chapterNumber,"
            + " u.displayName, u.username, s.dealStatus"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.storyId"
            + " LEFT JOIN Category ca on sc.categoryId = ca.id"
            + " WHERE s.status IN :storyStatus AND s.dealStatus = :storyDealStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String COUNT_VIP_STORY_NEW_UPDATE = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.storyId GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.storyID "
            + " LEFT JOIN user u on c.userPosted = u.id"
            + " WHERE s.status IN :storyStatus AND s.dealStatus = :storyDealStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.updateDate DESC";

    public static final String STORY_TOP_APPOIND = "SELECT s.id, s.name, s.countAppoint, s.images, s.author, COALESCE(d.sumVote, 0) AS cnt, ca.id as categoryId, ca.name as categoryName, s.infomation , s.dealStatus" +
            " FROM Story s" +
            " LEFT JOIN (SELECT pa.storyId, COALESCE(SUM(pa.vote), 0) AS sumVote FROM pay pa" +
            " WHERE pa.createDate BETWEEN :startDate AND :endDate" +
            " AND pa.type = :payType AND pa.status = :payStatus" +
            " GROUP BY pa.storyId) d ON s.id = d.storyId" +
            " LEFT JOIN `story_category` sc on s.id = sc.storyId" +
            " LEFT JOIN Category ca on sc.categoryId = ca.id" +
            " WHERE s.status IN :storyStatus" +
            " GROUP BY s.id" +
            " ORDER BY cnt DESC, s.countAppoint DESC";

    public static final String COUNT_STORY_TOP_APPOIND = "SELECT COUNT(*) FROM Story s" +
            " LEFT JOIN (SELECT pa.storyId, COALESCE(SUM(pa.vote), 0) AS sumVote FROM Pay pa" +
            " WHERE pa.createDate BETWEEN :startDate AND :endDate" +
            " AND pa.type = :payType AND pa.status = :payStatus" +
            " GROUP BY pa.storyId) d ON s.id = d.storyId" +
            " LEFT JOIN `story_category` sc on s.id = sc.storyId" +
            " LEFT JOIN Category ca on sc.categoryId = ca.id" +
            " WHERE s.status IN :storyStatus" +
            " GROUP BY s.id" +
            " ORDER BY s.countAppoint DESC";
    public static final String TOP_CONVERTER = "SELECT u.id, u.username, u.displayName, u.avatar,"
            + " COALESCE(d.cntc ,0) AS cnt, COALESCE(e.cnts ,0) AS scnt"
            + " FROM User u"
            + " LEFT JOIN (SELECT c.userPosted as userChapterPosted, COUNT(c.userPosted) as cntc FROM Chapter c"
            + " WHERE c.status IN :chapterStatus"
            + " GROUP BY c.userPosted) d ON u.id = d.userChapterPosted"
            + " LEFT JOIN (SELECT s.userPosted as userStoryPosted, COUNT(s.userPosted) as cnts FROM Story s"
            + " WHERE s.status IN :storyStatus"
            + " GROUP BY s.userPosted) e ON u.id = e.userStoryPosted"
            + " LEFT JOIN user_role ur ON u.id = ur.userId"
            + " WHERE u.status = :userStatus AND ur.roleId IN :roleList"
            + " ORDER BY cnt DESC, scnt DESC";

    public static final String COUNT_TOP_CONVERTER = "SELECT COUNT(*) FROM User u"
            + " LEFT JOIN (SELECT c.userPosted as userChapterPosted, COALESCE(COUNT(c.userPosted),0) as cnt FROM Chapter c"
            + " WHERE c.status IN :chapterStatus"
            + " GROUP BY c.userPosted) d ON u.id = d.userChapterPosted"
            + " LEFT JOIN (SELECT s.userPosted as userStoryPosted, COALESCE(COUNT(s.userPosted),0) as scnt FROM Story s"
            + " WHERE s.status IN :storyStatus"
            + " GROUP BY s.userPosted) e ON u.id = e.userStoryPosted"
            + " LEFT JOIN user_role ur ON u.id = ur.userId"
            + " WHERE u.status = :userStatus AND ur.roleId IN :roleList"
            + " ORDER BY cnt DESC, scnt DESC";
}
