package com.cyber.online_books.utils;

/**
 * @author Cyber_Group
 */

public class ConstantsQueryUtils {
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
