package com.cyber.online_books.response;

import com.cyber.online_books.entity.Information;
import lombok.Data;

import java.util.List;

@Data
public class InformationResponse {

    private List<CategorySummary> listCategoryOfMenu;
    private Information information;
}
