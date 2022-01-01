package com.cyber.online_books.utils;

public class ConstantsQueryUtils {

    public static final String STORY_NEW_UPDATE_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.author, s.update_date,"
            + " c.id as chapter_id, c.chapter_number,"
            + " u.display_name, u.username, s.deal_status"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.story_id GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.story_id "
            + " LEFT JOIN user u on c.user_posted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " WHERE  sc.category_id = :categoryId AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.update_date DESC";

    public static final String COUNT_STORY_NEW_UPDATE_BY_CATEGORY = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.story_id GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.story_id "
            + " LEFT JOIN user u on c.user_posted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " WHERE  sc.category_id = :categoryId AND s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.update_date DESC";

    public static final String STORY_TOP_VIEW_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.infomation, s.deal_status, s.author,"
            + " COALESCE(d.countTopView,0) AS cnt, ca.id as category_id, ca.name as categoryName FROM Story s"
            + " LEFT JOIN (SELECT c.story_id, COUNT(c.story_id) AS countTopView FROM Chapter c"
            + " LEFT JOIN `history` f ON  c.id = f.chapter_id"
            + " LEFT JOIN Story st on c.story_id = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.date_view BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.story_id) d ON s.id = d.story_id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.category_id = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.count_view DESC";

    public static final String COUNT_STORY_TOP_VIEW_BY_CATEGORY = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.count_view,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.story_id, COUNT(c.story_id) AS countTopView FROM Chapter c"
            + " LEFT JOIN `history` f ON  c.id = f.chapter_id"
            + " LEFT JOIN Story st on c.story_id = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.date_view BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.story_id) d ON s.id = d.story_id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.category_id = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.count_view DESC) rs";

    public static final String STORY_TOP_VOTE_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.infomation, s.deal_status, s.author,"
            + " COALESCE(d.countVote,0) AS cnt, ca.id as category_id, ca.name as categoryName FROM Story s"
            + " LEFT JOIN (SELECT p.story_id, COALESCE(SUM(p.vote), 0) AS countVote FROM Pay p"
            + " WHERE p.create_date BETWEEN :startDate AND :endDate "
            + " AND p.type = :payType "
            + " AND p.status = :payStatus"
            + " GROUP BY p.story_id) d ON s.id = d.story_id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.category_id = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.status, s.count_appoint DESC";

    public static final String COUNT_STORY_TOP_VOTE_BY_CATEGORY = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countVote,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT p.story_id, COALESCE(SUM(p.vote), 0) AS countVote FROM Pay p"
            + " WHERE p.create_date BETWEEN :startDate AND :endDate "
            + " AND p.type = :payType "
            + " AND p.status = :payStatus"
            + " GROUP BY p.story_id) d ON s.id = d.story_id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE  s.status IN :storyStatus AND sc.category_id = :categoryID"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.status, s.count_appoint DESC) rs";

    public static final String STORY_TOP_VIEW_BY_STATUS = "SELECT s.id, s.name, s.images, s.author, s.infomation, s.deal_status, "
            + " COALESCE(d.countTopView,0) AS cnt, ca.id as category_id, ca.name as categoryName"
            + " FROM Story s "
            + " LEFT JOIN (SELECT c.id as chapter_id, c.story_id, COUNT(c.story_id) AS countTopView FROM Chapter c"
            + " LEFT JOIN History f ON c.id = f.chapter_id"
            + " LEFT JOIN Story st on c.story_id = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.date_view BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.story_id) d ON s.id = d.story_id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.count_view DESC";

    public static final String COUNT_STORY_TOP_VIEW_BY_STATUS = "SELECT COUNT(*) FROM (SELECT s.id, COALESCE(d.countTopView,0) AS cnt FROM Story s"
            + " LEFT JOIN (SELECT c.story_id, COUNT(c.story_id) AS countTopView FROM Chapter c"
            + " LEFT JOIN History f ON c.id = f.chapter_id"
            + " LEFT JOIN Story st on c.story_id = st.id"
            + " WHERE st.status IN :storyStatus"
            + " AND f.date_view BETWEEN :startDate AND :endDate"
            + " AND f.status = :historyStatus"
            + " GROUP BY c.story_id) d ON s.id = d.story_id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE  s.status IN :storyStatus"
            + " GROUP BY s.id"
            + " ORDER BY cnt DESC, s.count_view DESC) rs";

    public static final String VIP_STORY_NEW_UPDATE = "SELECT s.id, s.name, s.images, s.author, s.update_date, s.infomation,"
            + " c.id as chapter_id, c.chapter_number,"
            + " u.display_name, u.username, s.deal_status"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.story_id GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.story_id "
            + " LEFT JOIN user u on c.user_posted = u.id"
            + " LEFT JOIN `story_category` sc on s.id = sc.story_id"
            + " LEFT JOIN Category ca on sc.category_id = ca.id"
            + " WHERE s.status IN :storyStatus AND s.deal_status = :storyDealStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.update_date DESC";

    public static final String COUNT_VIP_STORY_NEW_UPDATE = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.story_id GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.story_id "
            + " LEFT JOIN user u on c.user_posted = u.id"
            + " WHERE s.status IN :storyStatus AND s.deal_status = :storyDealStatus"
            + " GROUP BY s.id"
            + " ORDER BY s.update_date DESC";

    public static final String STORY_NEW_UPDATE_BY_STATUS = "SELECT s.id, s.name, s.images, s.author, s.update_date,"
            + " c.id as chapter_id, c.chapter_number,"
            + " u.display_name, u.username, s.deal_status"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.story_id GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.story_id "
            + " LEFT JOIN user u on c.user_posted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " ORDER BY s.update_date DESC";

    public static final String COUNT_STORY_NEW_UPDATE_BY_STATUS = "SELECT COUNT(*)"
            + " FROM Story s LEFT JOIN (SELECT c.* FROM Chapter c INNER JOIN"
            + " (SELECT MAX(c.id) AS maxChapterID FROM Story s"
            + " LEFT JOIN Chapter c"
            + " ON s.id = c.story_id GROUP BY s.id) d"
            + " ON c.id = d.maxChapterID "
            + " WHERE c.status IN :chapterStatus) c "
            + " ON s.id = c.story_id "
            + " LEFT JOIN user u on c.user_posted = u.id"
            + " WHERE s.status IN :storyStatus"
            + " ORDER BY s.update_date DESC";

    public static final String STORY_TOP_APPOIND = "SELECT s.id, s.name, s.images, s.author, COALESCE(d.sumVote, 0) AS cnt, ca.id as category_id, ca.name as categoryName, s.infomation , s.deal_status" +
            " FROM Story s" +
            " LEFT JOIN (SELECT pa.story_id, COALESCE(SUM(pa.vote), 0) AS sumVote FROM pay pa" +
            " WHERE pa.create_date BETWEEN :startDate AND :endDate" +
            " AND pa.type = :payType AND pa.status = :payStatus" +
            " GROUP BY pa.story_id) d ON s.id = d.story_id" +
            " LEFT JOIN `story_category` sc on s.id = sc.story_id" +
            " LEFT JOIN Category ca on sc.category_id = ca.id" +
            " WHERE s.status IN :storyStatus" +
            " GROUP BY s.id" +
            " ORDER BY cnt DESC, s.count_appoint DESC";

    public static final String COUNT_STORY_TOP_APPOIND = "SELECT COUNT(*) FROM Story s" +
            " LEFT JOIN (SELECT pa.story_id, COALESCE(SUM(pa.vote), 0) AS sumVote FROM Pay pa" +
            " WHERE pa.create_date BETWEEN :startDate AND :endDate" +
            " AND pa.type = :payType AND pa.status = :payStatus" +
            " GROUP BY pa.story_id) d ON s.id = d.storyId" +
            " LEFT JOIN `story_category` sc on s.id = sc.story_id" +
            " LEFT JOIN Category ca on sc.category_id = ca.id" +
            " WHERE s.status IN :storyStatus" +
            " GROUP BY s.id" +
            " ORDER BY d.countVote DESC, s.count_appoint DESC";
}
