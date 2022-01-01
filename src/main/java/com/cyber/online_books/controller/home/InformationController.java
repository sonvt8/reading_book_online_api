package com.cyber.online_books.controller.home;

import com.cyber.online_books.response.InformationResponse;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.service.InformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/thong-tin")
public class InformationController {

    private final Logger logger = LoggerFactory.getLogger(InformationController.class);
    private final CategoryService categoryService;
    private final InformationService informationService;

    @Autowired
    public InformationController(CategoryService categoryService, InformationService informationService) {
        this.categoryService = categoryService;
        this.informationService = informationService;
    }


    @GetMapping("")
    public ResponseEntity< InformationResponse > getAllInformation() {
        InformationResponse informationResponse = new InformationResponse();
        informationResponse.setListCategoryOfMenu(categoryService.getListCategoryOfMenu(1));
        informationResponse.setInformation(informationService.getWebInfomation());

        return new ResponseEntity<>(informationResponse, HttpStatus.OK);
    }
}
