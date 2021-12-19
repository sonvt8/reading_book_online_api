package com.cyber.online_books.utils;

import com.cyber.online_books.entity.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstantsListUtils {
    public static final List<Status> LIST_STORY_STATUS_VIP = Collections.unmodifiableList(
            new ArrayList< Status >() {{
                add(new Status(ConstantsStatusUtils.STORY_NOT_VIP , "Miễn Phí"));
                add(new Status(ConstantsStatusUtils.STORY_VIP, "Trả Phí"));
            }}
    );
    public static final List< Status > LIST_STORY_STATUS_CONVERTER = Collections.unmodifiableList(
            new ArrayList< Status >() {{
                add(new Status(ConstantsStatusUtils.STORY_STATUS_HIDDEN, "Khóa"));
                add(new Status(ConstantsStatusUtils.STORY_STATUS_GOING_ON, "Đang ra"));
                add(new Status(ConstantsStatusUtils.STORY_STATUS_STOP, "Tạm Dừng"));
                add(new Status(ConstantsStatusUtils.STORY_STATUS_COMPLETED, "Hoàn Thành"));
            }}
    );

    public static final List< Status > LIST_STORY_STATUS = Collections.unmodifiableList(
            new ArrayList< Status >() {{
                add(new Status(ConstantsStatusUtils.STORY_STATUS_GOING_ON, "Đang ra"));
                add(new Status(ConstantsStatusUtils.STORY_STATUS_COMPLETED, "Hoàn Thành"));
                add(new Status(ConstantsStatusUtils.STORY_STATUS_STOP, "Tạm Dừng"));
                add(new Status(ConstantsStatusUtils.STORY_STATUS_HIDDEN, "Ẩn"));
            }}
    );

    public static final List< Status > LIST_CHAPTER_STATUS_VIEW_ALL = Collections.unmodifiableList(
            new ArrayList< Status >() {{
                add(new Status(ConstantsStatusUtils.CHAPTER_DENIED, "Khóa"));
                add(new Status(ConstantsStatusUtils.CHAPTER_ACTIVED, "Free"));
                add(new Status(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED, "Vip"));
            }}
    );

    //List Status của chapter có trạng thái hiển thị
    public static final List< Integer > LIST_STORY_DISPLAY = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsStatusUtils.STORY_STATUS_COMPLETED);
                add(ConstantsStatusUtils.STORY_STATUS_GOING_ON);
                add(ConstantsStatusUtils.STORY_STATUS_STOP);
            }}
    );

    public static final List< Integer > LIST_ROLE_CON = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsUtils.ROLE_ADMIN);
                add(ConstantsUtils.ROLE_CONVERTER);
            }}
    );
    //List Status của Story có trạng thái hoàn thành
    public static final List< Integer > LIST_STORY_COMPLETE = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsStatusUtils.STORY_STATUS_COMPLETED);
            }}
    );

    //List Status của chapter có trạng thái hiển thị
    public static final List< Integer > LIST_CHAPTER_DISPLAY = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsStatusUtils.CHAPTER_ACTIVED);
                add(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
            }}
    );

    //List Status của chapter có trạng thái hiển thị
    public static final List< Integer > LIST_CHAPTER_STATUS_ALL = Collections.unmodifiableList(
            new ArrayList< Integer >() {{
                add(ConstantsStatusUtils.CHAPTER_ACTIVED);
                add(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
                add(ConstantsStatusUtils.CHAPTER_DENIED);
            }}
    );
    public static final Object LIST_CATEGORY_STATUS_VIEW_ALL = Collections.unmodifiableList(
            new ArrayList< Status >() {{
                add(new Status(ConstantsStatusUtils.CATEGORY_ACTIVED, "Mở"));
                add(new Status(ConstantsStatusUtils.CATEGORY_DENIED, "Khóa"));
            }}
    );;
}
