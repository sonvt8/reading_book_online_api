package com.cyber.online_books.utils;

public class ConstantsQueryUtils {

    public static final String STORY_NEW_UPDATE_BY_CATEGORY = "SELECT s.id, s.name, s.images, s.author, s.update_date,"
            + " c.id as chapterId, c.chapter_number,"
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
}
